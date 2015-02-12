package com.cspinformatique.kubik.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.product.repository.SupplierRepository;
import com.cspinformatique.kubik.product.service.SupplierService;
import com.cspinformatique.kubik.purchase.service.PurchaseOrderService;

@Service
public class SupplierServiceImpl implements SupplierService {
	@Autowired private PurchaseOrderService purchaseOrderService;
	
	@Autowired private SupplierRepository supplierRepository;
	
	@Override
	public Iterable<Supplier> findAll(){
		return this.supplierRepository.findAll();
	}
	
	@Override
	public Supplier findByEan13(String ean13) {
		return this.supplierRepository.findByEan13(ean13);
	}
	
	@Override
	public Supplier generateSupplierIfNotFound(String ean13){
		Supplier supplier = this.findByEan13(ean13);
		
		if(supplier == null){
			return this.save(new Supplier(null, ean13, "A défénir", null, 0f));
		}
		
		return supplier;
	}
	
	@Override
	public Supplier save(Supplier supplier){
		if(supplier.getId() == null){
			if(supplier.getEan13().equals(this.findByEan13(supplier.getEan13()))){
				throw new RuntimeException("Ean13 already taken.");
			}
		}
		
		Supplier result = this.supplierRepository.save(supplier);
		
		this.purchaseOrderService.recalculateOpenPurchaseOrderFromSupplier(result);
		
		return result;
	}
}
