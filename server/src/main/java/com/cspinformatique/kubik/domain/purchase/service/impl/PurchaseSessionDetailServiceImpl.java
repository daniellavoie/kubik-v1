package com.cspinformatique.kubik.domain.purchase.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.purchase.repository.PurchaseSessionDetailRepository;
import com.cspinformatique.kubik.domain.purchase.service.PurchaseSessionDetailService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.PurchaseSession;
import com.cspinformatique.kubik.model.purchase.PurchaseSessionDetail;
import com.cspinformatique.kubik.model.purchase.PurchaseSession.Status;

@Service
public class PurchaseSessionDetailServiceImpl implements
		PurchaseSessionDetailService {
	@Autowired private PurchaseSessionDetailRepository purchaseSessionDetailRepository;
	
	@Override
	public Iterable<PurchaseSessionDetail> findByProduct(Product product){
		return this.purchaseSessionDetailRepository.findByProduct(product);
	}
	
	@Override
	public Iterable<PurchaseSession> findPurchaseOrdersByProduct(Product product) {
		return purchaseSessionDetailRepository.findPurchaseOrdersByProduct(product);
	}

	@Override
	public Iterable<PurchaseSession> findPurchaseOrdersByProductAndStatus(
			Product product, Status status) {
		return purchaseSessionDetailRepository.findPurchaseOrdersByProductAndStatus(product, status);
	}
	
	@Override
	public PurchaseSessionDetail save(PurchaseSessionDetail purchaseSessionDetail){
		return purchaseSessionDetailRepository.save(purchaseSessionDetail);
	}
}
