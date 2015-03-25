package com.cspinformatique.kubik.domain.warehouse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.service.ReceptionService;
import com.cspinformatique.kubik.domain.sales.service.CustomerCreditService;
import com.cspinformatique.kubik.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.domain.warehouse.repository.ProductInventoryRepository;
import com.cspinformatique.kubik.domain.warehouse.service.ProductInventoryService;
import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.warehouse.model.ProductInventory;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);
	
	@Autowired private ProductInventoryRepository productInventoyRepository;
	
	@Autowired private CustomerCreditService customerCreditService;
	@Autowired private ReceptionService receptionService;
	@Autowired private InvoiceService invoiceService;
	
	@Override
	public ProductInventory findByProduct(Product product) {
		ProductInventory productInventory = this.productInventoyRepository.findByProduct(product);

		if(productInventory == null){
			productInventory = new ProductInventory(null, product, 0d, 0d);
		}
		
		return productInventory;
	}
	
	@Override
	public void updateInventory(Product product){
		int productId = product.getId();
		
		ProductInventory productInventory = this.findByProduct(product);
		
		double oldQuantityOnHand = productInventory.getQuantityOnHand();
		double quantityReceived = this.receptionService.findProductQuantityReceived(productId);
		double quantityCustomerReturned = this.customerCreditService.findProductQuantityReturned(productId);
		double quantitySold = this.invoiceService.findProductQuantitySold(productId);
		
		double quantityOnHand = quantityReceived + quantityCustomerReturned - quantitySold;
		
		productInventory.setQuantityOnHand(quantityOnHand);
		
		LOGGER.info("Updating inventory for product " + productId + " from " + oldQuantityOnHand + " to " + quantityOnHand + ".");
		
		this.save(productInventory);
	}
	
	@Override
	public ProductInventory save(ProductInventory productInventory) {
		return this.productInventoyRepository.save(productInventory);
	}

}
