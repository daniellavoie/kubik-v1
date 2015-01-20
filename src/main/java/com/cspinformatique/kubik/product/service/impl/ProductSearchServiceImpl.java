package com.cspinformatique.kubik.product.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.model.Product;
import com.cspinformatique.kubik.product.model.ProductSearch;
import com.cspinformatique.kubik.product.service.ProductSearchService;
import com.cspinformatique.kubik.product.service.ProductService;
import com.cspinformatique.kubik.product.service.SupplierService;
import com.cspinformatique.kubik.reference.model.Reference;
import com.cspinformatique.kubik.reference.service.ReferenceService;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {
	@Autowired private ReferenceService referenceService;
	@Autowired private ProductService productService;
	@Autowired private SupplierService supplierService;
	
	@Override
	public ProductSearch searchByEan13(String ean13) {
		return this.searchByEan13AndImportedInKubik(ean13, null);
	}
	
	@Override
	public ProductSearch searchByEan13AndImportedInKubik(String ean13, Boolean importedInKubik) {
		ArrayList<Product> products = new ArrayList<Product>(); 
		
		Sort sort = new Sort("mainReference");
		Iterable<Reference> references = null;
		if(importedInKubik == null){
			references = this.referenceService.findByEan13(ean13, sort);
		}else{
			references = this.referenceService.findByEan13AndImportedInKubik(ean13, importedInKubik, sort);
		}
		
		for(Reference reference : references){
			this.supplierService.generateSupplierIfNotFound(reference.getSupplierEan13());
			
			products.add(productService.buildProductFromReference(reference));
		}
		
		return new ProductSearch(products);
	}

	@Override
	public ProductSearch searchByEan13AndSupplierEan13(String ean13,
			String supplierEan13) {
		ArrayList<Product> products = new ArrayList<Product>(); 
		
		Reference reference =  this.referenceService.findByEan13AndSupplierEan13(ean13, supplierEan13);
		
		this.supplierService.generateSupplierIfNotFound(supplierEan13);
		
		products.add(productService.buildProductFromReference(reference));
				
		return new ProductSearch(products);
	}

}
