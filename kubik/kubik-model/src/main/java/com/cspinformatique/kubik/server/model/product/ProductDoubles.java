package com.cspinformatique.kubik.server.model.product;

import java.util.List;

public class ProductDoubles {
	private String ean13;
	private List<Product> products;
	
	public ProductDoubles(String ean13, List<Product> products) {
		this.ean13 = ean13;
		this.products = products;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
