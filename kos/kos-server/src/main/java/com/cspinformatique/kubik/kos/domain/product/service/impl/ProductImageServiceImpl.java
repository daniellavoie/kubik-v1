package com.cspinformatique.kubik.kos.domain.product.service.impl;

import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.cspinformatique.kubik.kos.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.kos.domain.product.repository.ProductImageRepository;
import com.cspinformatique.kubik.kos.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.kos.model.product.Product;
import com.cspinformatique.kubik.kos.model.product.ProductImage;
import com.cspinformatique.kubik.kos.model.product.ProductImageSize;

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
		return product.getKubikId() + "-" + size.name() + ".jpg";
	}

	@Override
	public ProductImage findByProductAndSize(Product product, ProductImageSize size) {
		return productImageRepository.findByProductAndSize(product, size);
	}

	@Override
	public InputStream loadImageInputStream(Product product, ProductImageSize size) {
		try {
			S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, calculateImageKey(product, size)));

			if (object == null) {
				throw new ImageNotFoundException();
			}

			return object.getObjectContent();
		} catch (AmazonS3Exception amazonS3Ex) {
			throw new ImageNotFoundException();
		}
	}
}
