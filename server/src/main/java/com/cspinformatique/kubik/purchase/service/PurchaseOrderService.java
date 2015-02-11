package com.cspinformatique.kubik.purchase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.purchase.model.Reception;

public interface PurchaseOrderService {
	
	void confirmAllOrders();
	
	Iterable<PurchaseOrder> findAll();
	
	Page<PurchaseOrder> findAll(Pageable pageable);
	
	Iterable<PurchaseOrder> findByProduct(Product product);
	
	Iterable<PurchaseOrder> findByProductAndStatus(Product product, Status status);
	
	PurchaseOrder findOne(long id);
	
	Reception generateReception(PurchaseOrder purchaseOrder);
	
	void initializeNonCalculatedOrders();
	
	void recalculateOpenPurchaseOrderFromSupplier(Supplier supplier);
	
	PurchaseOrder save(PurchaseOrder purchaseOrder);
	
	Iterable<PurchaseOrder> save(Iterable<PurchaseOrder> entities);
}
