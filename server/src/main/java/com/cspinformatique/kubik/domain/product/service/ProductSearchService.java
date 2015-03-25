package com.cspinformatique.kubik.domain.product.service;

import com.cspinformatique.kubik.product.model.ProductSearch;

public interface ProductSearchService {
	public ProductSearch searchByEan13(String ean13);
	
	public ProductSearch searchByEan13AndImportedInKubik(String ean13, Boolean importedInKubik);
	
	public ProductSearch searchByEan13AndSupplierEan13(String ean13, String supplierEan13);
}
