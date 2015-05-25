package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.math3.util.Precision;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.domain.purchase.repository.ReceptionRepository;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderService;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseSessionService;
import com.cspinformatique.kubik.domain.purchase.service.ReceptionService;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.DiscountType;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.model.purchase.PurchaseSessionDetail;
import com.cspinformatique.kubik.model.purchase.Reception;
import com.cspinformatique.kubik.model.purchase.ReceptionDetail;
import com.cspinformatique.kubik.model.purchase.Reception.Status;

@Service
@Transactional
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
	public double findProductQuantityReceived(int productId){
		Double result = this.receptionRepository.findProductQuantityReceived(productId);
		
		if(result == null){
			return 0d;
		}
		
		return result;
	}	

	@Override
	public void initialize() {
		List<Long> idsToFilter = Arrays.asList(new Long[] { 15000030l,
				15000031l, 15000062l, 15000065l });

		// Generates the reception for all the orders.
		List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

		// Filters order with list defined above.
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
				purchaseOrder.setPurchaseSession(matchingSession);
				
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
					this.purchaseOrderService.save(purchaseOrder);
					this.save(reception);
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
	@Transactional
	public Reception save(Reception reception) {
		return this.save(Arrays.asList(new Reception[]{reception})).iterator().next();
	}

	@Override
	public Iterable<Reception> save(Iterable<Reception> receptions) {
		Set<Reception> receptionsToUpdateInventory = new HashSet<Reception>();
		for(Reception reception : receptions){
			if(reception.getStatus().equals(Status.STANDBY) || reception.getStatus().equals(Status.SHIPPED)){
				reception.setEditable(true);
			}else{
				reception.setEditable(false);
			}
			
			if (reception.getStatus().equals(Status.CLOSED) && reception.getDateReceived() == null) {
				reception.setDateReceived(new Date());
				
				receptionsToUpdateInventory.add(reception);
			}

			this.calculateAmounts(reception);
		}

		receptions = this.receptionRepository.save(receptions);
		
		for(Reception reception : receptionsToUpdateInventory){
			this.updateInventory(reception);
		}
		
		return receptions;
		
	}

	private void updateInventory(Reception reception) {
		for (ReceptionDetail detail : reception.getDetails()) {
			productInventoryService.updateInventory(detail.getProduct());
		}
	}
	
	@Override
	@Transactional
	public void validate(){
		Pageable pageRequest = new PageRequest(0, 50);
		Page<Reception> page = null;
		do {
			page = this.receptionRepository.findByStatus(Status.STANDBY, pageRequest);
			for(Reception reception : page.getContent()){
				reception.setStatus(Status.CLOSED);
			}
			
			this.save(page.getContent());
			
			pageRequest = page.nextPageable();
		} while (page != null && page.getContent().size() != 0);
	}
}
