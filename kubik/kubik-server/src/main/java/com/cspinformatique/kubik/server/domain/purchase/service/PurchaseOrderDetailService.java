package com.cspinformatique.kubik.server.domain.purchase.service;

import java.util.List;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder.Status;

public interface PurchaseOrderDetailService {
	List<PurchaseOrderDetail> findByProduct(Product product);
	
	List<PurchaseOrder> findPurchaseOrdersByProduct(Product product);

	List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(Product product,
			Status status);
	
	PurchaseOrderDetail save(PurchaseOrderDetail purchaseOrderDetail);
}
