package com.cspinformatique.kubik.purchase.service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder.Status;

public interface PurchaseOrderDetailService {
	Iterable<PurchaseOrder> findPurchaseOrdersByProduct(Product product);
	
	Iterable<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
