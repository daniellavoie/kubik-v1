package com.cspinformatique.kubik.purchase.service;

import java.util.List;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.purchase.model.PurchaseOrder.Status;

public interface PurchaseOrderDetailService {
	List<PurchaseOrder> findPurchaseOrdersByProduct(Product product);
	
	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
