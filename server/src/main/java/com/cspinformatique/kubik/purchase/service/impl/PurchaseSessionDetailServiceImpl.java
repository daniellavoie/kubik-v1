package com.cspinformatique.kubik.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.purchase.model.PurchaseSession;
import com.cspinformatique.kubik.purchase.model.PurchaseSession.Status;
import com.cspinformatique.kubik.purchase.repository.PurchaseSessionDetailRepository;
import com.cspinformatique.kubik.purchase.service.PurchaseSessionDetailService;

@Service
public class PurchaseSessionDetailServiceImpl implements
		PurchaseSessionDetailService {
	@Autowired private PurchaseSessionDetailRepository purchaseSessionDetailRepository;
	
	@Override
	public Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product) {
		return purchaseSessionDetailRepository.findPurchaseOrdersByProduct(product);
	}

	@Override
	public Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(
			Product product, Status status) {
		return purchaseSessionDetailRepository.findPurchaseOrdersByProductAndStatus(product, status);
	}
}
