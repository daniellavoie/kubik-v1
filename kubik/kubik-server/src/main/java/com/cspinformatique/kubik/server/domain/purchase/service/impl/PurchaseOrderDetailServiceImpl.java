package com.cspinformatique.kubik.server.domain.purchase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.purchase.repository.PurchaseOrderDetailRepository;
import com.cspinformatique.kubik.server.domain.purchase.service.PurchaseOrderDetailService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrderDetail;
import com.cspinformatique.kubik.server.model.purchase.PurchaseOrder.Status;

@Service
public class PurchaseOrderDetailServiceImpl implements
		PurchaseOrderDetailService {
	@Autowired private PurchaseOrderDetailRepository purchaseOrderDetailRepository;
	
	@Override
	public List<PurchaseOrderDetail> findByProduct(Product product){
		return this.purchaseOrderDetailRepository.findByProduct(product);
	}
	
	@Override
	public List<PurchaseOrder> findPurchaseOrdersByProduct(Product product){
		return this.purchaseOrderDetailRepository.findPurchaseOrdersByProduct(product);
	}
	
	@Override
	public List<PurchaseOrder> findPurchaseOrdersByProductAndStatus(
			Product product, Status status) {
		return this.purchaseOrderDetailRepository.findPurchaseOrdersByProductAndStatus(product, status);
	}
	
	@Override
	public PurchaseOrderDetail save(PurchaseOrderDetail purchaseOrderDetail){
		return this.purchaseOrderDetailRepository.save(purchaseOrderDetail);
	}
}
