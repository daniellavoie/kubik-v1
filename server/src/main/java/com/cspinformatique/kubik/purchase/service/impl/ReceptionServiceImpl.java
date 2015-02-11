package com.cspinformatique.kubik.purchase.service.impl;

import java.util.Date;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.purchase.model.DiscountType;
import com.cspinformatique.kubik.purchase.model.Reception;
import com.cspinformatique.kubik.purchase.model.Reception.Status;
import com.cspinformatique.kubik.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.purchase.repository.ReceptionRepository;
import com.cspinformatique.kubik.purchase.service.ReceptionService;
import com.cspinformatique.kubik.warehouse.service.ProductInventoryService;

@Service
public class ReceptionServiceImpl implements ReceptionService {
	@Autowired private ProductInventoryService productInventoryService;
	@Autowired private ProductService productService;
	@Autowired private ReceptionRepository receptionRepository;

	private void calculateAmounts(Reception reception) {
		double totalAmountTaxOut = 0d;

		if (reception.getDetails() != null) {
			for (ReceptionDetail detail : reception.getDetails()) {
				Product product = productService.findOne(detail.getProduct()
						.getId());

				double quantity = detail.getQuantityReceived();

				// Calculates invoice details amounts
				DiscountType discountType = new DiscountType(
						DiscountType.Types.SUPPLIER.toString(), null);

				detail.setDiscountApplied(product.getSupplier().getDiscount());
				if (product.getDiscount() > detail.getDiscountApplied()) {
					detail.setDiscountApplied(product.getDiscount());
					discountType.setType(DiscountType.Types.PRODUCT.toString());
				}

				if (reception.getDiscount() > detail.getDiscountApplied()) {
					detail.setDiscountApplied(reception.getDiscount());
					discountType.setType(DiscountType.Types.ORDER.toString());
				}

				if (detail.getDiscount() > detail.getDiscountApplied()) {
					detail.setDiscountApplied(detail.getDiscount());
					discountType.setType(DiscountType.Types.ORDER_DETAIL
							.toString());
				}

				detail.setDiscountType(discountType);
				detail.setUnitPriceTaxOut(product.getPriceTaxOut1()
						* (1 - (detail.getDiscountApplied() / 100)));
				detail.setTotalAmountTaxOut(detail.getUnitPriceTaxOut()
						* quantity);

				// Increment invoice totals amount.
				totalAmountTaxOut += detail.getTotalAmountTaxOut();
			}
		}

		reception.setTotalAmountTaxOut(Precision
				.round(totalAmountTaxOut, 2));
	}
	
	@Override
	public Iterable<Reception> findAll(Pageable pageable) {
		return this.receptionRepository.findAll(pageable);
	}

	@Override
	public Reception findOne(int id) {
		return this.receptionRepository.findOne(id);
	}

	@Override
	public Reception save(Reception reception) {
		if(reception.getStatus().equals(Status.CLOSED)){
			Reception oldReception = this.findOne(reception.getId());
			
			if(oldReception == null || !oldReception.getStatus().equals(reception.getStatus())){
				reception.setDateReceived(new Date());
				this.updateInventory(reception);
			}
		}
		
		this.calculateAmounts(reception);
		
		return this.receptionRepository.save(reception);
	}
	
	private void updateInventory(Reception reception){
		for(ReceptionDetail detail : reception.getDetails()){
			productInventoryService.addInventory(detail.getProduct(), detail.getQuantityReceived());
		}
	}
}
