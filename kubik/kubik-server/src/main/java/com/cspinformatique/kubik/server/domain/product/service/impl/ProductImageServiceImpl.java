package com.cspinformatique.kubik.server.domain.product.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

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
import com.amazonaws.services.s3.model.S3Object;
import com.cspinformatique.kubik.server.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.server.domain.product.exception.ImageTooSmallException;
import com.cspinformatique.kubik.server.domain.product.repository.ProductImageRepository;
import com.cspinformatique.kubik.server.domain.product.service.ProductImageService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.product.ProductImage;
import com.cspinformatique.kubik.server.model.product.ProductImageSize;
import com.mortennobel.imagescaling.AdvancedResizeOp.UnsharpenMask;
import com.mortennobel.imagescaling.ResampleOp;

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
	public void deleteByProduct(Product product) {
		productImageRepository.deleteByProduct(product);
	}

	@Override
	public ProductImage findByProductAndSize(Product product, ProductImageSize size) {
		return productImageRepository.findByProductAndSize(product, size);
	}

	@Override
	public void initialLoad() {
		LOGGER.info("Starting product image initial load.");

		Page<Product> productPage = null;
		Pageable pageRequest = new PageRequest(0, 100, Direction.DESC, "id");

		do {
			productPage = productService.findAll(pageRequest);

			productPage.getContent().parallelStream().filter(product -> product.isDilicomReference())
					.forEach(product -> persistProductImagesToAws(product));

			pageRequest = pageRequest.next();
		} while (productPage.hasNext());

		LOGGER.info("Product image initial load completed.");
	}

	@Override
	public InputStream loadInputStream(Product product, ProductImageSize size) {
		try (S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, calculateImageKey(product, size)))) {
			if (object == null) {
				throw new ImageNotFoundException();
			}

			return IOUtils.toBufferedInputStream(object.getObjectContent());
		} catch (AmazonClientException | IOException amazonS3Ex) {
			throw new ImageNotFoundException();
		}
	}

	@Override
	public void persistProductImagesToAws(Product product) {
		Assert.notNull(product.getImageEncryptedKey());
		Assert.isTrue(!product.getImageEncryptedKey().equals(""),
				"Product " + product.getId() + " image encrypted key cannot be empty.");

		try {
			persistAmazonImages(product);
		} catch (ImageNotFoundException | ImageTooSmallException ex) {
			try {
				persistDilicomImages(product);
			} catch (ImageNotFoundException | ImageTooSmallException ex2) {
				LOGGER.warn("Proper image could not be found for product " + product.getId());
			}
		}
	}

	@Override
	public void persistAmazonImages(Product product) {
		persistAmazonImage(product);
	}

	private void persistAmazonImage(Product product) {
		if (product.getIsbn() != null && !product.getIsbn().trim().equals("")) {
			try {
				persistImageFromUrlToAws("http://images.amazon.com/images/P/" + product.getIsbn() + ".01.SCRM.jpg",
						product);
			} catch (ImageTooSmallException imageTooSmallEx) {
				persistImageFromUrlToAws("http://images.amazon.com/images/P/" + product.getIsbn() + ".01.LZZ.jpg",
						product);
			}
		}
	}

	@Override
	public void persistDilicomImages(Product product) {
		persistImageFromUrlToAws(
				"http://images1.centprod.com/" + ean13 + "/" + product.getImageEncryptedKey() + "-cover-full.jpg",
				product);
	}

	@Override
	public void persistImageFromUrlToAws(String url, Product product) {
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URI(url).toURL().openConnection();
			urlConnection.connect();

			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200 || responseCode == 304) {
				uploadImageToAws(IOUtils.toByteArray(urlConnection.getInputStream()), product);
			} else {
				throw new ImageNotFoundException();
			}
		} catch (AmazonClientException | IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void resizeAndPersitsImage(BufferedImage originalImage, Product product, ProductImageSize size) {
		try (ByteArrayOutputStream outputstream = new ByteArrayOutputStream()) {
			ImageIO.write(resizeImageWithHint(originalImage,
					originalImage.getWidth() > size.getWidth() ? size.getWidth() : originalImage.getWidth(),
					originalImage.getWidth() > size.getWidth()
							? size.getWidth() * originalImage.getHeight() / originalImage.getWidth()
							: originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB), "jpg", outputstream);

			byte[] imageBytes = outputstream.toByteArray();
			uploadImageToAws(new ByteArrayInputStream(imageBytes), outputstream.size(), product, size);
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height, int type) {
		ResampleOp resampleOp = new ResampleOp(width, height);
		resampleOp.setUnsharpenMask(UnsharpenMask.Normal);

		return resampleOp.filter(originalImage, null);
	}

	@Override
	public void uploadImageToAws(byte[] imageBytes, Product product) {
		try {
			InputStream inputStream = new ByteArrayInputStream(imageBytes);

			BufferedImage originalImage = ImageIO.read(inputStream);

			if (originalImage.getWidth() == 1) {
				throw new ImageTooSmallException();
			}

			// Persits the inputstream as Full format.
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				ImageIO.write(originalImage, "jpg", outputStream);

				uploadImageToAws(new ByteArrayInputStream(imageBytes), imageBytes.length, product,
						ProductImageSize.FULL);
			}

			resizeAndPersitsImage(originalImage, product, ProductImageSize.THUMB);
			resizeAndPersitsImage(originalImage, product, ProductImageSize.MEDIUM);
			resizeAndPersitsImage(originalImage, product, ProductImageSize.LARGE);

			LOGGER.info("Persisted images for product " + product.getId() + ".");
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private void uploadImageToAws(InputStream inputStream, long contentLength, Product product, ProductImageSize size) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentLength);

		amazonS3.putObject(bucketName, calculateImageKey(product, size), inputStream, metadata);

		ProductImage productImage = productImageRepository.findByProductAndSize(product, size);

		if (productImage == null)
			productImage = new ProductImage(0l, product, size, contentLength);
		else
			productImage.setContentLength(contentLength);

		productImageRepository.save(productImage);
	}
}
