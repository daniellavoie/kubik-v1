package com.cspinformatique.kubik.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.product.model.ProductSearch;
import com.cspinformatique.kubik.product.service.ProductSearchService;

@Controller
@RequestMapping("/productSearch")
public class ProductSearchController {
	@Autowired private ProductSearchService productSearchService;
	
	@RequestMapping(value="/{ean13}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ProductSearch searchProduct(@PathVariable String ean13){
		return this.productSearchService.searchByEan13(ean13);
	}
	
	@RequestMapping(value="/{ean13}", method=RequestMethod.GET, params="importedInKubik", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ProductSearch searchProductInKubik(@PathVariable String ean13){
		return this.productSearchService.searchByEan13AndImportedInKubik(ean13, true);
	}
	
	@RequestMapping(value="/{ean13}/{supplierEan13}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ProductSearch searchProduct(@PathVariable String ean13, @PathVariable String supplierEan13){
		return this.productSearchService.searchByEan13AndSupplierEan13(ean13, supplierEan13);
	}
}
