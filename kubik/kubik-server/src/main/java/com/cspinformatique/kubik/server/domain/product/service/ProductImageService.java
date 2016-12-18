package com.cspinformatique.kubik.server.domain.product.service;

import java.io.InputStream;

import com.cspinformatique.kubik.server.model.product.ProductImageSize;

public interface ProductImageService {
	void initialLoad();

	InputStream loadInputStream(String ean13, ProductImageSize size, boolean preview);

	void persistProductImagesToAws(String ean13);

	void persistAmazonImages(String ean13);

	void persistDilicomImages(String ean13);

	void persistImageFromUrlToAws(String url, String ean13);

	void uploadImageToAws(byte[] imageBytes, String ean13);

	void validateImagesFromAws(String ean13);
}
