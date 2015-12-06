package com.cspinformatique.kubik.server.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.Supplier;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.Reception;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder.Status;

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
