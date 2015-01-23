package com.cspinformatique.kubik.purchase.task;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.product.service.SupplierService;
import com.cspinformatique.kubik.purchase.model.DeliveryDateType;
import com.cspinformatique.kubik.purchase.model.Reception;
import com.cspinformatique.kubik.purchase.model.ShippingMode;
import com.cspinformatique.kubik.purchase.model.Reception.Status;
import com.cspinformatique.kubik.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.purchase.model.ShippingPackage;
import com.cspinformatique.kubik.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.purchase.service.ReceptionService;

@Component
public class FetchReceptionsTask implements InitializingBean{
	private static final Logger logger = LoggerFactory
			.getLogger(FetchReceptionsTask.class);

	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private ReceptionService receptionService;

	@Autowired
	private SupplierService supplierService;

	// private AbstractInboundFileSynchronizer<?> ftpInboundFileSynchronizer;
	// private String remoteDirectory = "/pub/O";

	@Value("${kubik.reference.dilicom.archive.folder}")
	private String archiveDirectoryPath;

	@Value("${kubik.reference.dilicom.receptions.folder}")
	private String receptionsDirectoryPath;

	private File archiveDirectory;
	private File receptionsDirectory;
	
	private DateFormat dateFormat;
	private DecimalFormat quantityNumberFormat;

	public FetchReceptionsTask() {
		this.dateFormat = new SimpleDateFormat("ddMMyy");
		this.quantityNumberFormat = new DecimalFormat("00000.000");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		archiveDirectory = new File(archiveDirectoryPath);
		receptionsDirectory = new File(receptionsDirectoryPath);

		if(!archiveDirectory.exists()) archiveDirectory.mkdirs();
		if(!receptionsDirectory.exists()) receptionsDirectory.mkdirs();
	};
	
	@Transactional
	@Scheduled(fixedDelay = 1000 * 60 * 30)
	public void fetchDilicomFiles() {
		// Fetch files from remote FTP server.

		// Process files in local directory.
		for (File file : receptionsDirectory.listFiles()) {
			this.processFile(file);
		}

		this.archiveFiles();
	}

	private void archiveFiles() {
		try {
			for (File file : receptionsDirectory.listFiles()) {
				logger.info("Archiving file " + file.getAbsolutePath());

				FileUtils.moveFileToDirectory(file, archiveDirectory,
						true);
			}
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private void processFile(File file) {
		logger.info("Processing reception file " + file.getAbsolutePath() + ".");

		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file);

			// int lineNumber = 0;
			Reception reception = null;

			Supplier supplier = null;
			Date creationDate = null;
			DeliveryDateType deliveryDateType = null;
			Date deliveryDate = null;

			while (it.hasNext()) {
				// ++lineNumber;
				String line = it.nextLine();

				if (line.startsWith("A")) {
					supplier = this.supplierService.generateSupplierIfNotFound(line.substring(14, 27));

					creationDate = dateFormat.parse(line.substring(35, 41));

					deliveryDateType = DeliveryDateType.parseByType(line
							.substring(54, 57));

					deliveryDate = dateFormat.parse(line.substring(57, 63));
				} else if (line.startsWith("K")) {
					reception.getShippingPackages().add(
							new ShippingPackage(null, line.substring(4, 22)));
				} else if (line.startsWith("R")) {
					// New reception encountered.
					if (reception != null) {
						// Save the previous reception.
						this.receptionService.save(reception);
					}

					reception = new Reception(null, supplier, ShippingMode.USUAL_METHOD, creationDate, null,
							deliveryDateType, deliveryDate,
							this.purchaseOrderService.findOne(Long
									.parseLong(line.substring(17, 25).trim())),
							new ArrayList<ReceptionDetail>(), Status.OPEN,
							new ArrayList<ShippingPackage>());
				}

				if (line.startsWith("L")) {
					String productEan13 = line.substring(5, 18);

					Product product = this.productService
							.generateProductIfNotFound(productEan13,
									supplier.getEan13());

					String quantityString = line.substring(18, 27);
					double quantity = quantityNumberFormat.parse(
							quantityString.subSequence(0, 6) + "."
									+ quantityString.substring(6))
							.doubleValue();

					ReceptionDetail detail = null;
					for (ReceptionDetail existingDetail : reception
							.getDetails()) {
						if (existingDetail.getProduct().getId() == product
								.getId()) {
							existingDetail.setQuantityToReceive(existingDetail.getQuantityToReceive() + quantity);

							detail = existingDetail;
						}
					}

					if (detail == null) {
						detail = new ReceptionDetail(null, reception, product,
								quantity, 0d);

						reception.getDetails().add(detail);
					}
				}
			}

			if (reception != null) {
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
}
