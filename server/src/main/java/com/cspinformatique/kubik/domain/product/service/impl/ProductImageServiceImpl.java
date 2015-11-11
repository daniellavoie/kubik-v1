package com.cspinformatique.kubik.domain.product.service.impl;

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
import com.cspinformatique.kubik.domain.product.controller.PersistProductImageActor;
import com.cspinformatique.kubik.domain.product.message.PersistProductImagesMessage;
import com.cspinformatique.kubik.domain.product.repository.ProductImageRepository;
import com.cspinformatique.kubik.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.product.ProductImage;
import com.cspinformatique.kubik.model.product.ProductImageSize;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Service
public class ProductImageServiceImpl implements ProductImageService {
	private static Logger LOGGER = LoggerFactory.getLogger(ProductImageServiceImpl.class);

	@Resource
	private ActorSystem actorSystem;

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

	private ActorRef persistImagesActor;

	private String calculateImageKey(Product product, ProductImageSize size) {
		return product.getId() + "-" + size.name() + ".jpg";
	}

	@Override
	public ProductImage findByProductAndSize(Product product, ProductImageSize size) {
		return productImageRepository.findByProductAndSize(product, size);
	}

	@PostConstruct
	public void init() {
		persistImagesActor = actorSystem.actorOf(Props.create(PersistProductImageActor.class, this));
		
		if(!amazonS3.listBuckets().stream().filter(bucket -> bucket.getName().equals(bucketName)).findAny().isPresent()){
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

			productPage.getContent().stream().filter(product -> product.isDilicomReference())
					.forEach(product -> persistImagesActor.tell(new PersistProductImagesMessage(product.getId()),
							ActorRef.noSender()));

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

		Arrays.asList(ProductImageSize.values()).forEach(size -> persistProductImageToAws(product, size));

		LOGGER.info("Uploaded product images " + product.getId() + " to AWS S3.");
	}

	private void persistProductImageToAws(Product product, ProductImageSize size) {
		Assert.notNull(product.getImageEncryptedKey());
		Assert.isTrue(!product.getImageEncryptedKey().equals(""),
				"Product " + product.getId() + " image encrypted key cannot be empty.");

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URI("http://images1.centprod.com/" + ean13 + "/"
					+ product.getImageEncryptedKey() + "-cover-" + size.name().toLowerCase() + ".jpg").toURL()
							.openConnection();
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

				productImageRepository.save(new ProductImage(0l, product, size, contentLength));
			}
		} catch (AmazonClientException | IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}
}
