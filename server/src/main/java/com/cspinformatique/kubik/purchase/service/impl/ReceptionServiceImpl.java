package com.cspinformatique.kubik.purchase.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.purchase.model.DiscountType;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrderDetail;
import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.model.PurchaseSessionDetail;
import com.cspinformatique.kubik.purchase.model.Reception;
import com.cspinformatique.kubik.purchase.model.Reception.Status;
import com.cspinformatique.kubik.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.purchase.repository.ReceptionRepository;
import com.cspinformatique.kubik.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.purchase.service.PurchaseSessionService;
import com.cspinformatique.kubik.purchase.service.ReceptionService;
import com.cspinformatique.kubik.warehouse.service.ProductInventoryService;

@Service
public class ReceptionServiceImpl implements ReceptionService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReceptionServiceImpl.class);

	@Autowired
	private ProductInventoryService productInventoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private PurchaseSessionService purchaseSessionService;
	@Autowired
	private ReceptionRepository receptionRepository;

	private void calculateAmounts(Reception reception) {
		double totalAmountTaxOut = 0d;

		if (reception.getDetails() != null) {
			for (ReceptionDetail detail : reception.getDetails()) {
				Product product = productService.findOne(detail.getProduct()
						.getId());

				double quantity = detail.getQuantityReceived();

				if (product.getPurchasePriceTaxOut() == null) {
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

					if (reception.getDiscount() > detail
							.getDiscountApplied()) {
						detail.setDiscountApplied(reception.getDiscount());
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
				}else{
					detail.setDiscount(0f);
					detail.setDiscountApplied(0f);
					detail.setDiscountType(null);
				}
				
				detail.setTotalAmountTaxOut(detail.getUnitPriceTaxOut()
						* quantity);

				// Increment invoice totals amount.
				totalAmountTaxOut += detail.getTotalAmountTaxOut();
			}
		}

		reception.setTotalAmountTaxOut(Precision.round(totalAmountTaxOut, 2));
	}

	@Override
	public Iterable<Reception> findAll(Pageable pageable) {
		return this.receptionRepository.findAll(pageable);
	}

	@Override
	public Reception findByPurchaseOrder(PurchaseOrder purchaseOrder) {
		return this.receptionRepository.findByPurchaseOrder(purchaseOrder);
	}

	@Override
	public Reception findOne(int id) {
		return this.receptionRepository.findOne(id);
	}

	@Override
	@Transactional
	public void initialize() {
		List<Long> idsToFilter = Arrays.asList(new Long[] { 15000030l,
				15000031l, 15000062l, 15000065l });

		// Generates the reception for all the orders.
		List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

		for (PurchaseOrder purchaseOrder : this.purchaseOrderService
				.findByStatus(PurchaseOrder.Status.DRAFT)) {
			if (!idsToFilter.contains(purchaseOrder.getId())) {
				LOGGER.info("Generating reception for purchase order "
						+ purchaseOrder.getId());

				purchaseOrder.setStatus(PurchaseOrder.Status.SUBMITED);
				purchaseOrders.add(purchaseOrder);

				this.purchaseOrderService.save(purchaseOrder);
			} else {
				LOGGER.info("Skipping purchase order " + purchaseOrder.getId());
			}
		}

		for (PurchaseOrder purchaseOrder : purchaseOrders) {
			int matchingCount = 0;
			PurchaseSession matchingSession = null;

			for (PurchaseSession purchaseSession : this.purchaseSessionService
					.findByProductAndStatus(purchaseOrder.getDetails().get(0)
							.getProduct(), PurchaseSession.Status.SUBMITED)) {
				Date sessionDate = LocalDate
						.fromDateFields(purchaseSession.getCloseDate())
						.toDateTimeAtStartOfDay().toDate();
				Date orderDate = LocalDate
						.fromDateFields(purchaseOrder.getDate())
						.toDateTimeAtStartOfDay().toDate();

				if (sessionDate.getTime() == orderDate.getTime()) {
					++matchingCount;
					matchingSession = purchaseSession;
				}
			}

			if (matchingCount == 1) {
				// Generate reception.
				boolean update = true;

				// retreive the reception.
				Reception reception = this.findByPurchaseOrder(purchaseOrder);

				for (ReceptionDetail receptionDetail : reception.getDetails()) {
					PurchaseOrderDetail matchingPurchaseOrderDetail = null;
					PurchaseSessionDetail matchingPurchasseSessionDetail = null;

					for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrder
							.getDetails()) {
						for (PurchaseSessionDetail purchaseSessionDetail : matchingSession
								.getDetails()) {
							if (receptionDetail.getProduct().getId() == purchaseOrderDetail
									.getProduct().getId()
									&& receptionDetail.getProduct().getId() == purchaseSessionDetail
											.getProduct().getId()) {
								matchingPurchaseOrderDetail = purchaseOrderDetail;
								matchingPurchasseSessionDetail = purchaseSessionDetail;

								receptionDetail
										.setQuantityReceived(purchaseOrderDetail
												.getQuantity());
								receptionDetail
										.setQuantityToReceive(purchaseSessionDetail
												.getQuantity());

								purchaseOrderDetail.setQuantity(receptionDetail
										.getQuantityToReceive());

								if (receptionDetail.getQuantityToReceive() != receptionDetail
										.getQuantityReceived()) {
									LOGGER.info("Updating reception details for reception "
											+ reception.getId()
											+ " on product "
											+ matchingPurchaseOrderDetail
													.getProduct().getId()
											+ ". To received : "
											+ receptionDetail
													.getQuantityToReceive()
											+ " | Reveived : "
											+ receptionDetail
													.getQuantityReceived());
								}
								break;
							}
						}

						if (matchingPurchaseOrderDetail != null) {
							break;
						}
					}

					if (matchingPurchaseOrderDetail == null
							|| matchingPurchasseSessionDetail == null) {
						update = false;
						break;
					}
				}

				if (update) {
					this.save(reception);
					this.purchaseOrderService.save(purchaseOrder);
				}
			} else {
				if (matchingCount == 0) {
					LOGGER.warn("Skipping purchase order "
							+ purchaseOrder.getId()
							+ ", no purchase session could be found.");
				} else {
					LOGGER.warn("Skipping purchase order "
							+ purchaseOrder.getId()
							+ ", multiple purchase sessions were found.");
				}
			}
		}

	}

	@Override
	public Reception save(Reception reception) {
		if (reception.getStatus().equals(Status.CLOSED)) {
			Reception oldReception = this.findOne(reception.getId());

			if (oldReception == null
					|| !oldReception.getStatus().equals(reception.getStatus())) {
				reception.setDateReceived(new Date());
				this.updateInventory(reception);
			}
		}

		this.calculateAmounts(reception);

		return this.receptionRepository.save(reception);
	}

	private void updateInventory(Reception reception) {
		for (ReceptionDetail detail : reception.getDetails()) {
			productInventoryService.addInventory(detail.getProduct(),
					detail.getQuantityReceived());
		}
	}
}
