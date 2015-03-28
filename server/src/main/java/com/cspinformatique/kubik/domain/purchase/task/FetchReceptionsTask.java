package com.cspinformatique.kubik.domain.purchase.task;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.remote.synchronizer.AbstractInboundFileSynchronizer;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.product.service.SupplierService;
import com.cspinformatique.kubik.domain.purchase.model.DeliveryDateType;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.Reception;
import com.cspinformatique.kubik.domain.purchase.model.Reception.Status;
import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.domain.purchase.model.ShippingPackage;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionService;
import com.cspinformatique.kubik.domain.purchase.task.exception.InvalidReceptionDetailException;
import com.cspinformatique.kubik.domain.purchase.task.exception.InvalidReceptionSupplierException;
import com.cspinformatique.kubik.domain.purchase.task.exception.ProductNotFoundException;
import com.cspinformatique.kubik.domain.purchase.task.exception.PurchaseOrderNotFoundException;
import com.cspinformatique.kubik.domain.purchase.task.exception.PurchaseOrderReceptionNotFoundException;
import com.cspinformatique.kubik.domain.purchase.task.exception.ReceptionAlreadyReceivedException;
import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;

@Component
public class FetchReceptionsTask implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FetchReceptionsTask.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private ReceptionService receptionService;

	@Autowired
	private SessionFactory<FTPFile> sessionFactory;

	@Autowired
	private SupplierService supplierService;

	@Value("${kubik.dilicom.archive.folder}")
	private String archiveDirectoryPath;

	@Value("${kubik.dilicom.receptions.folder}")
	private String receptionsDirectoryPath;

	@Value("${kubik.dilicom.ftp.out.path}")
	private String remoteDirectory;

	private File archiveDirectory;
	private File receptionsDirectory;

	private DateFormat dateFormat;
	private DecimalFormat quantityNumberFormat;

	private AbstractInboundFileSynchronizer<?> ftpInboundFileSynchronizer;

	public FetchReceptionsTask() {
		this.dateFormat = new SimpleDateFormat("ddMMyy");
		this.quantityNumberFormat = new DecimalFormat("00000.000");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		archiveDirectory = new File(archiveDirectoryPath);
		receptionsDirectory = new File(receptionsDirectoryPath);

		if (!archiveDirectory.exists())
			archiveDirectory.mkdirs();
		if (!receptionsDirectory.exists())
			receptionsDirectory.mkdirs();

		ftpInboundFileSynchronizer = new FtpInboundFileSynchronizer(
				sessionFactory);

		((FtpInboundFileSynchronizer) ftpInboundFileSynchronizer)
				.setFilter(new FtpSimplePatternFileListFilter("EXP*"));

		ftpInboundFileSynchronizer.setRemoteDirectory(remoteDirectory);
		ftpInboundFileSynchronizer.setDeleteRemoteFiles(true);
	};

	@Transactional
	@Scheduled(fixedDelay = 1000 * 60 * 60)
	public void fetchDilicomFiles() {
		// Fetch files from remote FTP server.
		ftpInboundFileSynchronizer
				.synchronizeToLocalDirectory(receptionsDirectory);

		// Process files in local directory.
		for (File file : receptionsDirectory.listFiles()) {
			this.processFile(file);
		}

		this.archiveFiles();
	}

	private void archiveFiles() {
		try {
			for (File file : receptionsDirectory.listFiles()) {
				LOGGER.info("Archiving file " + file.getAbsolutePath());

				FileUtils.deleteQuietly(new File(archiveDirectoryPath + "/"
						+ file.getName()));
				FileUtils.moveFileToDirectory(file, archiveDirectory, true);
			}
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private void processFile(File file) {
		LOGGER.info("Processing reception file " + file.getAbsolutePath() + ".");

		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file);

			// int lineNumber = 0;
			Reception reception = null;

			Supplier supplier = null;
			Date creationDate = null;
			DeliveryDateType deliveryDateType = null;
			Date deliveryDate = null;

			boolean skipReception = false;

			while (it.hasNext()) {
				// ++lineNumber;
				String line = it.nextLine();

				LOGGER.trace("Processing line : " + line);

				if (line.startsWith("A")) {
					supplier = this.supplierService
							.generateSupplierIfNotFound(line.substring(14, 27));

					creationDate = dateFormat.parse(line.substring(35, 41));

					deliveryDateType = DeliveryDateType.parseByType(line
							.substring(54, 57));

					deliveryDate = dateFormat.parse(line.substring(57, 63));
				} else if (!skipReception && line.startsWith("K")) {
					reception.getShippingPackages().add(
							new ShippingPackage(null, line.substring(4, 22)));
				} else if (line.startsWith("R")) {
					// New reception encountered.
					skipReception = false;

					// Save the previous reception if needed.
					if (reception != null) {
						this.receptionService.save(reception);
					}

					try {
						reception = this.loadAndUpdateReception(line, supplier,
								creationDate, deliveryDate, deliveryDateType);
					} catch (Exception ex) {
						if (ex instanceof PurchaseOrderNotFoundException) {
							LOGGER.info("Reception "
									+ file.getName()
									+ " kipped since no purchase order were found for id "
									+ ((PurchaseOrderNotFoundException) ex)
											.getPurchaseOrderId() + ".");
						} else if (ex instanceof ReceptionAlreadyReceivedException) {
							LOGGER.info(((ReceptionAlreadyReceivedException) ex)
									.getReceptionId()
									+ " has already been received. Shipping notification will be ignored.");
						} else {
							LOGGER.error(ex.getMessage(), ex);
						}

						skipReception = true;
						reception = null;
					}
				}

				if (!skipReception && line.startsWith("L")) {
					try {
						this.updateReceptionDetail(line, reception);
					} catch (InvalidReceptionDetailException invalidReceptionDetailException) {
						skipReception = true;
						LOGGER.error(
								"Error while generating reception detail from line "
										+ line, invalidReceptionDetailException);
					}
				}
			}

			if (!skipReception && reception != null) {
				this.receptionService.save(reception);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (it != null) {
				it.close();
			}
		}
	}

	private Reception loadAndUpdateReception(String line, Supplier supplier,
			Date creationDate, Date deliveryDate,
			DeliveryDateType deliveryDateType)
			throws PurchaseOrderNotFoundException,
			PurchaseOrderReceptionNotFoundException,
			InvalidReceptionSupplierException {
		// Loads the purchase order linked with the reception.

		String purchaseOrderIdString = line.substring(17, 25);
		long purchaseOrderId;
		try {
			purchaseOrderId = Long.parseLong(purchaseOrderIdString.trim());
		} catch (NumberFormatException numberFormatEx) {
			throw new PurchaseOrderNotFoundException(purchaseOrderIdString);
		}

		PurchaseOrder purchaseOrder = this.purchaseOrderService
				.findOne(purchaseOrderId);

		if (purchaseOrder == null) {
			throw new PurchaseOrderNotFoundException(purchaseOrderIdString);
		}

		Reception reception = purchaseOrder.getReception();

		if (reception == null) {
			throw new PurchaseOrderReceptionNotFoundException(purchaseOrderId);
		}

		if (reception.getStatus().equals(Status.CLOSED)) {
			throw new ReceptionAlreadyReceivedException(reception.getId());
		}

		if (!reception.getSupplier().getId().equals(supplier.getId())) {
			throw new InvalidReceptionSupplierException(reception.getSupplier()
					.getId(), supplier.getId());
		}

		reception.getShippingPackages().clear();
		reception.setDateCreated(creationDate);
		reception.setDeliveryDate(deliveryDate);
		reception.setDeliveryDateType(deliveryDateType);
		reception.setStatus(Status.SHIPPED);

		for (ReceptionDetail detail : reception.getDetails()) {
			detail.setQuantityReceived(0);
		}

		return reception;
	}

	private void updateReceptionDetail(String line, Reception reception) {
		String productEan13 = line.substring(5, 18);

		Product product = this.productService.findByEan13AndSupplier(
				productEan13, reception.getSupplier());

		if (product == null) {
			throw new ProductNotFoundException(productEan13, reception
					.getSupplier().getEan13());
		}

		double quantity;
		String quantityString = line.substring(18, 27);

		try {
			quantity = quantityNumberFormat.parse(
					quantityString.subSequence(0, 6) + "."
							+ quantityString.substring(6)).doubleValue();
		} catch (Exception ex) {
			throw new InvalidReceptionDetailException(
					"Quantity could not be parsed properly from " + line, ex);
		}

		float discount = 0f;
		String discountString = line.substring(51, 56);
		try {
			discount = quantityNumberFormat.parse(
					discountString.subSequence(0, 3) + "."
							+ discountString.substring(3)).floatValue();
		} catch (Exception ex) {
			throw new InvalidReceptionDetailException(
					"Discount could not be parsed properly from " + line, ex);
		}

		ReceptionDetail detail = null;
		for (ReceptionDetail existingDetail : reception.getDetails()) {
			if (existingDetail.getProduct().getId() == product.getId()) {
				existingDetail.setQuantityReceived(existingDetail
						.getQuantityToReceive() + quantity);
				existingDetail.setDiscount(discount);

				detail = existingDetail;
			}
		}

		if (detail == null) {
			detail = new ReceptionDetail(null, reception, product, quantity,
					0d, 0f, 0f, null, 0d, 0d);

			reception.getDetails().add(detail);
		}
	}
}
