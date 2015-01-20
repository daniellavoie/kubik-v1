package com.cspinformatique.kubik.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Supplier;
import com.cspinformatique.kubik.product.repository.SupplierRepository;
import com.cspinformatique.kubik.product.service.SupplierService;

@Service
public class SupplierServiceImpl implements SupplierService {
	@Autowired private SupplierRepository supplierRepository;
	
	@Override
	public Supplier findByEan13(String ean13) {
		return this.supplierRepository.findByEan13(ean13);
	}
	
	@Override
	public Supplier generateSupplierIfNotFound(String ean13){
		Supplier supplier = this.findByEan13(ean13);
		
		if(supplier == null){
			return this.save(new Supplier(null, ean13, "A défénir"));
		}
		
		return supplier;
	}
	
	@Override
	public Supplier save(Supplier supplier){
		return this.supplierRepository.save(supplier);
	}
}
