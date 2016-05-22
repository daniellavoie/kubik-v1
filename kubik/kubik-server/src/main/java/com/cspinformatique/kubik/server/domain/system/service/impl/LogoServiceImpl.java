package com.cspinformatique.kubik.server.domain.system.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.cspinformatique.kubik.server.domain.product.exception.ImageNotFoundException;
import com.cspinformatique.kubik.server.domain.system.service.LogoService;

@Service
public class LogoServiceImpl implements LogoService {

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	private AmazonS3 amazonS3;

	@Autowired
	public LogoServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Override
	public InputStream findLogo() {
		try {
			S3Object object = amazonS3.getObject(new GetObjectRequest(bucketName, "logo"));

			if (object == null) {
				throw new ImageNotFoundException();
			}

			return object.getObjectContent();
		} catch (AmazonS3Exception amazonS3Ex) {
			throw new ImageNotFoundException();
		}
	}

	@Override
	public void saveLogo(byte[] logoBytes) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(logoBytes.length);

		amazonS3.putObject(bucketName, "logo", new ByteArrayInputStream(logoBytes), metadata);
	}

}
