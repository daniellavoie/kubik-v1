package com.cspinformatique.kubik.onlinesales.service.impl;

import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.cspinformatique.kubik.onlinesales.model.Product;
import com.cspinformatique.kubik.onlinesales.model.ProductImage;
import com.cspinformatique.kubik.onlinesales.model.ProductImageSize;
import com.cspinformatique.kubik.onlinesales.repository.ProductImageRepository;
import com.cspinformatique.kubik.onlinesales.service.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {
	@Resource
	AmazonS3 amazonS3;

	@Resource
	ProductImageRepository productImageRepository;

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	@Value("${aws.s3.endpoint}")
	private String endpoint;

	@Value("${kos.ean13}")
	private String ean13;

	private String calculateImageKey(Product product, ProductImageSize size) {
		return product.getId() + "-" + size.name() + ".jpg";
	}

	@Override
	public ProductImage findByProductAndSize(Product product, ProductImageSize size) {
		return productImageRepository.findByProductAndSize(product, size);
	}

	@Override
	public InputStream loadImageInputStream(Product product, ProductImageSize size) {
		return amazonS3.getObject(bucketName, calculateImageKey(product, size)).getObjectContent();
	}
}
