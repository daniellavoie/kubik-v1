package com.cspinformatique.kubik.server.model.product;

public enum ProductImageSize {
	THUMB("thumb", 1), MEDIUM("medium", 2), LARGE("large", 3), FULL("full", 4);

	ProductImageSize(String value, int order) {
		this.value = value;
		this.order = order;
	}

	private String value;
	private int order;

	public String getValue() {
		return value;
	}

	public int getOrder() {
		return order;
	}
}
