package com.cspinformatique.kubik.domain.purchase.service;

import java.util.List;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.model.purchase.PurchaseOrder.Status;
import com.cspinformatique.kubik.model.purchase.PurchaseOrderDetail;

public interface PurchaseOrderDetailService {
	List<PurchaseOrderDetail> findByProduct(Product product);
	
	List<PurchaseOrder> findPurchaseOrdersByProduct(Product product);

	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product,
			Status status);
	
	PurchaseOrderDetail save(PurchaseOrderDetail purchaseOrderDetail);
}
