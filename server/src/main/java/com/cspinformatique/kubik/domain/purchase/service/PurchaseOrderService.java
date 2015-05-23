package com.cspinformatique.kubik.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.Reception;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.Supplier;

public interface PurchaseOrderService {
	
	void confirmAllOrders();
	
	Iterable<PurchaseOrder> findAll();
	
	Page<PurchaseOrder> findAll(Pageable pageable);
	
	List<PurchaseOrder> findByProduct(Product product);
	
	List<PurchaseOrder> findByProductAndStatus(Product product, Status status);
	
	List<PurchaseOrder> findByStatus(Status status);
	
	PurchaseOrder findOne(long id);
	
	Reception generateReception(PurchaseOrder purchaseOrder);
	
	void fixSubmitedDate();
	
	void recalculateOpenPurchaseOrderFromSupplier(Supplier supplier);
	
	PurchaseOrder save(PurchaseOrder purchaseOrder);
	
	Iterable<PurchaseOrder> save(Iterable<PurchaseOrder> entities);
}
