package com.cspinformatique.kubik.domain.purchase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder;
import com.cspinformatique.kubik.domain.purchase.model.PurchaseOrder.Status;
import com.cspinformatique.kubik.domain.purchase.repository.PurchaseOrderDetailRepository;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseOrderDetailService;
import com.cspinformatique.kubik.product.model.Product;

@Service
public class PurchaseOrderDetailServiceImpl implements
		PurchaseOrderDetailService {
	@Autowired private PurchaseOrderDetailRepository purchaseOrderDetailRepository;
	
	@Override
	public List<PurchaseOrder> findPurchaseOrdersByProduct(Product product){
		return this.purchaseOrderDetailRepository.findPurchaseOrdersByProduct(product);
	}
	
	@Override
	public List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(
			Product product, Status status) {
		return this.purchaseOrderDetailRepository.findPurchaseOrdersByProductAndStatus(product, status);
	}
}
