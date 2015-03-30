package com.cspinformatique.kubik.domain.purchase.service;

import java.util.List;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.product.Product;

public interface PurchaseOrderDetailService {
	List<PurchaseOrder> findPurchaseOrdersByProduct(Product product);
	
	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product, Status status);
}
