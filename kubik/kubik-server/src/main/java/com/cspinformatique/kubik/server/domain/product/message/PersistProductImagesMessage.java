package com.cspinformatique.kubik.server.domain.product.message;

public class PersistProductImagesMessage {
	private final int productId;

	public PersistProductImagesMessage(final int productId) {
		this.productId = productId;
	}

	public int getProductId() {
		return productId;
	}
}
