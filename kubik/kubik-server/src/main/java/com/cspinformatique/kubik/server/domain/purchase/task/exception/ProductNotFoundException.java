package com.cspinformatique.kubik.server.domain.purchase.task.exception;

public class ProductNotFoundException extends InvalidReceptionDetailException {
	private static final long serialVersionUID = -6043299571434799785L;

	public ProductNotFoundException(String ean13, String supplierEan13) {
		super("Product with ean13 " + ean13 + " could not be found for supplier with ean13 " + supplierEan13);
	}
}
