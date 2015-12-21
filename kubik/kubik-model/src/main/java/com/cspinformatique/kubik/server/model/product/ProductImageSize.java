package com.cspinformatique.kubik.server.model.product;

public enum ProductImageSize {
	THUMB("thumb", 1, 90), MEDIUM("medium", 2, 200), LARGE("large", 3, 360), FULL("full", 4, 0);

	ProductImageSize(String value, int order, int width) {
		this.value = value;
		this.order = order;
		this.width = width;
	}

	private String value;
	private int order;
	private int width;

	public String getValue() {
		return value;
	}

	public int getOrder() {
		return order;
	}

	public int getWidth() {
		return width;
	}
}
