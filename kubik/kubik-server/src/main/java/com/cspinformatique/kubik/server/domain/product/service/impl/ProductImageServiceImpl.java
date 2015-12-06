package com.cspinformatique.kubik.server.domain.product.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Region;
import com.cspinformatique.kubik.server.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.server.domain.product.repository.ProductImageRepository;
import com.cspinformatique.kubik.server.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductImage;
import com.cspinformatique.kubik.server.model.product.ProductImageSize;

@Service
public class ProductImageServiceImpl implements ProductImageService {
	private static Logger LOGGER = LoggerFactory.getLogger(ProductImageServiceImpl.class);

	@Resource
	private AmazonS3 amazonS3;

	@Resource
	private ProductImageRepository productImageRepository;

	@Resource
	private ProductService productService;

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	@Value("${aws.s3.endpoint}")
	private String endpoint;

	@Value("${kubik.ean13}")
	private String ean13;

	private String calculateImageKey(Product product, ProductImageSize size) {
		return product.getId() + "-" + size.name() + ".jpg";
	}

	@Override
	public ProductImage findByProductAndSize(Product product, ProductImageSize size) {
		return productImageRepository.findByProductAndSize(product, size);
	}

	@PostConstruct
	public void init() {
		if (!amazonS3.listBuckets().stream().filter(bucket -> bucket.getName().equals(bucketName)).findAny()
				.isPresent()) {
			amazonS3.createBucket(bucketName, Region.EU_Ireland);
		}
	}

	@Override
	public void initialLoad() {
		LOGGER.info("Starting product image initial load.");

		Page<Product> productPage = null;
		Pageable pageRequest = new PageRequest(0, 100, Direction.ASC, "id");

		do {
			productPage = productService.findAll(pageRequest);

			productPage.getContent().parallelStream().filter(product -> product.isDilicomReference())
					.forEach(product -> persistProductImagesToAws(product.getId()));

			pageRequest = pageRequest.next();
		} while (productPage.hasNext());

		LOGGER.info("Product image initial load completed.");
	}

	@Override
	public InputStream loadInputStream(Product product, ProductImageSize size) {
		return amazonS3.getObject(new GetObjectRequest(bucketName, calculateImageKey(product, size)))
				.getObjectContent();
	}

	@Override
	public void persistProductImagesToAws(int productId) {
		Product product = productService.findOne(productId);

		Arrays.asList(ProductImageSize.values()).parallelStream()
				.forEach(size -> persistProductImageToAws(product, size));

		LOGGER.info("Uploaded product images " + product.getId() + " to AWS S3.");
	}

	private void persistProductImageToAws(Product product, ProductImageSize size) {
		Assert.notNull(product.getImageEncryptedKey());
		Assert.isTrue(!product.getImageEncryptedKey().equals(""),
				"Product " + product.getId() + " image encrypted key cannot be empty.");
		try {
			loadImageFromUrlToAws("http://images1.centprod.com/" + ean13 + "/" + product.getImageEncryptedKey()
					+ "-cover-" + size.name().toLowerCase() + ".jpg", product, size);

		} catch (ImageNotFoundException imageNotFoundEx) {
			if (product.getIsbn() != null || !product.getIsbn().trim().equals("")) {
				String awsSize = null;
				if (size.equals(ProductImageSize.THUMB))
					awsSize = "TZZZZZZZ";
				else if (size.equals(ProductImageSize.LARGE))
					awsSize = "SCLZZZZZZZ";

				if (awsSize != null)
					loadImageFromUrlToAws(
							"http://images.amazon.com/images/P/" + product.getIsbn() + ".01." + awsSize + ".jpg",
							product, size);

			}
		}
	}

	private void loadImageFromUrlToAws(String url, Product product, ProductImageSize size) {
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URI(url).toURL().openConnection();
			urlConnection.connect();

			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200 || responseCode == 304) {
				InputStream inputStream = null;
				long contentLength = urlConnection.getContentLengthLong();
				if (contentLength == -1) {
					byte[] content = IOUtils.toByteArray(urlConnection.getInputStream());
					inputStream = new ByteArrayInputStream(content);
					contentLength = content.length;
				} else {
					inputStream = urlConnection.getInputStream();
				}

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(contentLength);

				amazonS3.putObject(bucketName, calculateImageKey(product, size), inputStream, metadata);

				ProductImage productImage = productImageRepository.findByProductAndSize(product, size);

				if (productImage == null)
					productImage = new ProductImage(0l, product, size, contentLength);
				else
					productImage.setContentLength(contentLength);

				productImageRepository.save(productImage);
			} else {
				throw new ImageNotFoundException();
			}
		} catch (AmazonClientException | IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}
}
