package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.purchase.model.DeliveryDateType;
import com.cspinformatique.kubik.domain.purchase.model.DiscountType;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.domain.purchase.model.Reception;
import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.domain.purchase.model.ShippingPackage;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.domain.purchase.repository.PurchaseOrderRepository;
import com.cspinformatique.kubik.domain.purchase.service.DilicomOrderService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderDetailService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.Supplier;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
	@Autowired
	private DilicomOrderService dilicomOrderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private ReceptionService receptionService;

	private DateFormat dateFormat;

	public PurchaseOrderServiceImpl() {
		this.dateFormat = new SimpleDateFormat("yy");
	}

	private void calculateAmounts(PurchaseOrder purchaseOrder) {
		double totalAmountTaxOut = 0d;

		if (purchaseOrder.getDetails() != null) {
			for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
				Product product = productService.findOne(detail.getProduct()
						.getId());

				double quantity = detail.getQuantity();

				if (product.getPurchasePriceTaxOut() == null || product.getPurchasePriceTaxOut() == 0d) {
					// Calculates invoice details amounts
					DiscountType discountType = new DiscountType(
							DiscountType.Types.SUPPLIER.toString(), null);

					detail.setDiscountApplied(product.getSupplier()
							.getDiscount());
					if (product.getDiscount() > detail.getDiscountApplied()) {
						detail.setDiscountApplied(product.getDiscount());
						discountType.setType(DiscountType.Types.PRODUCT
								.toString());
					}

					if (purchaseOrder.getDiscount() > detail
							.getDiscountApplied()) {
						detail.setDiscountApplied(purchaseOrder.getDiscount());
						discountType.setType(DiscountType.Types.ORDER
								.toString());
					}

					if (detail.getDiscount() > detail.getDiscountApplied()) {
						detail.setDiscountApplied(detail.getDiscount());
						discountType.setType(DiscountType.Types.ORDER_DETAIL
								.toString());
					}

					detail.setDiscountType(discountType);
					detail.setUnitPriceTaxOut(product.getPriceTaxOut1()
							* (1 - (detail.getDiscountApplied() / 100)));
				} else {
					detail.setDiscount(0f);
					detail.setDiscountApplied(0f);
					detail.setDiscountType(null);
					detail.setUnitPriceTaxOut(product.getPurchasePriceTaxOut());
				}

				detail.setTotalAmountTaxOut(detail.getUnitPriceTaxOut()
						* quantity);

				// Increment invoice totals amount.
				totalAmountTaxOut += detail.getTotalAmountTaxOut();
			}
		}

		purchaseOrder.setTotalAmountTaxOut(Precision
				.round(totalAmountTaxOut, 2));
	}

	@Override
	public void confirmAllOrders() {
		List<PurchaseOrder> purchaseOrders = this.purchaseOrderRepository
				.findByStatus(Status.DRAFT);

		for (PurchaseOrder purchaseOrder : purchaseOrders) {
			purchaseOrder.setSentToDilicom(false);
			purchaseOrder.setStatus(Status.SUBMITED);
		}

		this.save(purchaseOrders);
	}

	@Override
	public Iterable<PurchaseOrder> findAll() {
		return this.purchaseOrderRepository.findAll();
	}

	@Override
	public Page<PurchaseOrder> findAll(Pageable pageable) {
		return this.purchaseOrderRepository.findAll(pageable);
	}

	@Override
	public List<PurchaseOrder> findByProduct(Product product) {
		return this.purchaseOrderDetailService
				.findPurchaseOrdersByProduct(product);
	}

	@Override
	public List<PurchaseOrder> findByProductAndStatus(Product product,
			Status status) {
		return this.purchaseOrderDetailService
				.findPurchaseOrdersByProductAndStatus(product, status);
	}

	@Override
	public List<PurchaseOrder> findByStatus(Status status) {
		return this.purchaseOrderRepository.findByStatus(status);
	}

	@Override
	public PurchaseOrder findOne(long id) {
		return this.purchaseOrderRepository.findOne(id);
	}

	private long generateId() {
		String dateString = dateFormat.format(new Date());

		Page<PurchaseOrder> page = this.purchaseOrderRepository
				.findAll(new PageRequest(0, 1, Direction.DESC, "id"));

		long number = Long.valueOf(dateString + "000001");
		if (page.getContent().size() > 0) {
			long lastNumber = page.getContent().get(0).getId();
			String lastDateString = String.valueOf(lastNumber).substring(0, 2);

			if (lastDateString.equals(dateString)) {
				number = lastNumber + 1;
			}
		}

		return number;
	}

	@Override
	public Reception generateReception(PurchaseOrder purchaseOrder) {
		Reception reception = new Reception(null, purchaseOrder.getSupplier(),
				purchaseOrder.getShippingMode(), new Date(), null,
				DeliveryDateType.SOONEST, purchaseOrder.getMinDeliveryDate(),
				purchaseOrder, new ArrayList<ReceptionDetail>(),
				Reception.Status.STANDBY, new ArrayList<ShippingPackage>(),
				purchaseOrder.getDiscount(),
				purchaseOrder.getTotalAmountTaxOut(), false);

		for (PurchaseOrderDetail detail : purchaseOrder.getDetails()) {
			reception.getDetails().add(
					new ReceptionDetail(null, reception, detail.getProduct(),
							detail.getQuantity(), detail.getQuantity(), detail
									.getDiscount(),
							detail.getDiscountApplied(), detail
									.getDiscountType(), detail
									.getUnitPriceTaxOut(), detail
									.getTotalAmountTaxOut()));
		}

		return this.receptionService.save(reception);
	}

	@Override
	public void initializeNonCalculatedOrders() {
		this.save(this.purchaseOrderRepository.findByTotalAmountTaxOut(0d));
	}

	@Override
	public void recalculateOpenPurchaseOrderFromSupplier(Supplier supplier) {
		this.save(this.purchaseOrderRepository.findBySupplierAndStatus(
				supplier, Status.DRAFT));
	}

	@Override
	public PurchaseOrder save(PurchaseOrder purchaseOrder) {
		return this.save(Arrays.asList(new PurchaseOrder[] { purchaseOrder }))
				.iterator().next();
	}

	@Override
	@Transactional
	public Iterable<PurchaseOrder> save(Iterable<PurchaseOrder> entities) {
		try {
			List<String> orderReferences = new ArrayList<String>();

			for (PurchaseOrder purchaseOrder : entities) {
				if (purchaseOrder.getId() == 0) {
					purchaseOrder.setId(this.generateId());
				}

				if (purchaseOrder.getStatus().equals(Status.SUBMITED)) {
					// Checks if the reception needs to be generated.
					if (purchaseOrder.getReception() == null) {
						purchaseOrder.setReception(this
								.generateReception(purchaseOrder));
					}

					// Checks if the purchase order needs to be sent to dilicom.
					if (purchaseOrder.isDilicomOrder()
							&& !purchaseOrder.isSentToDilicom()) {
						orderReferences.add(this.dilicomOrderService
								.sendOrderToDilicom(purchaseOrder));

						purchaseOrder.setSentToDilicom(true);
					}
				}

				this.calculateAmounts(purchaseOrder);
			}

			Iterable<PurchaseOrder> results = this.purchaseOrderRepository
					.save(entities);

			// Confirm the orders (rename the FTP files).
			for (String ediFile : orderReferences) {
				this.dilicomOrderService.confirmDilicomOrder(ediFile);
			}

			return results;
		} catch (Exception ex) {
			// Cancels the orders (delete the files from FTP).

			throw new RuntimeException(ex);
		}
	}
}
