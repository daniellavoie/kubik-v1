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

	@Value("${kubik.ean13}")
	private String ean13;

	private String calculateImageKey(String ean13, ProductImageSize size, boolean preview) {
		return ean13 + "-" + size.name() + (preview ? "-PREVIEW" : "") + ".jpg";
	}

	@Override
	public void initialLoad() {
		LOGGER.info("Starting product image initial load.");

		Page<Product> productPage = null;
		Pageable pageRequest = new PageRequest(0, 100, Direction.DESC, "id");

		do {
			productPage = productService.findAll(pageRequest);

			productPage.getContent().parallelStream().filter(product -> product.isDilicomReference())
					.forEach(product -> persistProductImagesToAws(product.getEan13()));

			pageRequest = pageRequest.next();
		} while (productPage.hasNext());

		LOGGER.info("Product image initial load completed.");
	}

	@Override
	public InputStream loadInputStream(String ean13, ProductImageSize size, boolean preview) {
		try (S3Object object = amazonS3
				.getObject(new GetObjectRequest(bucketName, calculateImageKey(ean13, size, preview)))) {
			if (object == null)
				if (preview)
					return loadInputStream(ean13, size, false);
				else
					throw new ImageNotFoundException();

			return IOUtils.toBufferedInputStream(object.getObjectContent());
		} catch (AmazonClientException | IOException amazonS3Ex) {
			throw new ImageNotFoundException();
		}
	}

	@Override
	public void persistProductImagesToAws(String ean13) {
		try {
			persistAmazonImages(ean13);
		} catch (ImageNotFoundException | ImageTooSmallException ex) {
			try {
				persistDilicomImages(ean13);
			} catch (ImageNotFoundException | ImageTooSmallException ex2) {
				LOGGER.warn("Proper image could not be found for ean13 " + ean13);
			}
		}
	}

	@Override
	public void persistAmazonImages(String ean13) {
		persistAmazonImage(ean13);
	}

	private void persistAmazonImage(String ean13) {
		Product product = productService.findByEan13(ean13);

		if (product.getIsbn() != null && !product.getIsbn().trim().equals("")) {
			try {
				persistImageFromUrlToAws("http://images.amazon.com/images/P/" + product.getIsbn() + ".01.SCRM.jpg",
						ean13);
			} catch (ImageTooSmallException imageTooSmallEx) {
				persistImageFromUrlToAws("http://images.amazon.com/images/P/" + product.getIsbn() + ".01.LZZ.jpg",
						ean13);
			}
		}
	}

	@Override
	public void persistDilicomImages(String ean13) {
		Product product = productService.findByEan13(ean13);

		persistImageFromUrlToAws(
				"http://images1.centprod.com/" + ean13 + "/" + product.getImageEncryptedKey() + "-cover-full.jpg",
				ean13);
	}

	@Override
	public void persistImageFromUrlToAws(String url, String ean13) {
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URI(url).toURL().openConnection();
			urlConnection.connect();

			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200 || responseCode == 304) {
				uploadImageToAws(IOUtils.toByteArray(urlConnection.getInputStream()), ean13);
			} else {
				throw new ImageNotFoundException();
			}
		} catch (AmazonClientException | IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void resizeAndPersitsImage(BufferedImage originalImage, String ean13, ProductImageSize size) {
		try (ByteArrayOutputStream outputstream = new ByteArrayOutputStream()) {
			ImageIO.write(resizeImageWithHint(originalImage,
					originalImage.getWidth() > size.getWidth() ? size.getWidth() : originalImage.getWidth(),
					originalImage.getWidth() > size.getWidth()
							? size.getWidth() * originalImage.getHeight() / originalImage.getWidth()
							: originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB), "jpg", outputstream);

			byte[] imageBytes = outputstream.toByteArray();
			uploadImageToAws(new ByteArrayInputStream(imageBytes), outputstream.size(), ean13, size);
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
	public void uploadImageToAws(byte[] imageBytes, String ean13) {
		try {
			InputStream inputStream = new ByteArrayInputStream(imageBytes);

			BufferedImage originalImage = ImageIO.read(inputStream);

			if (originalImage.getWidth() == 1) {
				throw new ImageTooSmallException();
			}

			// Persits the inputstream as Full format.
			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				ImageIO.write(originalImage, "jpg", outputStream);

				uploadImageToAws(new ByteArrayInputStream(imageBytes), imageBytes.length, ean13, ProductImageSize.FULL);
			}

			resizeAndPersitsImage(originalImage, ean13, ProductImageSize.THUMB);
			resizeAndPersitsImage(originalImage, ean13, ProductImageSize.MEDIUM);
			resizeAndPersitsImage(originalImage, ean13, ProductImageSize.LARGE);

			LOGGER.info("Persisted images for ean13 " + ean13 + ".");
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}

	private void uploadImageToAws(InputStream inputStream, long contentLength, String ean13, ProductImageSize size) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentLength);

		amazonS3.putObject(bucketName, calculateImageKey(ean13, size, true), inputStream, metadata);
	}

	private void validateImageFromAws(String ean13, ProductImageSize size) {
		String previewFileName = calculateImageKey(ean13, size, true);
		String destinationFileName = calculateImageKey(ean13, size, false);

		boolean validateImage = false;
		try (S3Object s3Object = amazonS3.getObject(bucketName, previewFileName)) {
			validateImage = true;
		} catch (AmazonClientException | IOException e) {
			// File does not exists.
		}

		if (validateImage) {
			amazonS3.deleteObject(bucketName, destinationFileName);

			amazonS3.copyObject(bucketName, previewFileName, bucketName, destinationFileName);

			amazonS3.deleteObject(bucketName, previewFileName);
		}
	}

	@Override
	public void validateImagesFromAws(String ean13) {
		validateImageFromAws(ean13, ProductImageSize.FULL);
		validateImageFromAws(ean13, ProductImageSize.THUMB);
		validateImageFromAws(ean13, ProductImageSize.MEDIUM);
		validateImageFromAws(ean13, ProductImageSize.LARGE);
	}
}
