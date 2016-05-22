package com.cspinformatique.kubik.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Region;

@Configuration
public class AwsConfig {

	@Value("${aws.access.key}")
	private String awsAccessKey;

	@Value("${aws.access.secret}")
	private String awsAccessSecret;
	
	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	public @Bean AmazonS3 amazonS3() {
		AmazonS3Client amazonS3 = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsAccessSecret));

		amazonS3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.EU_WEST_1));

		if (!amazonS3.listBuckets().stream().filter(bucket -> bucket.getName().equals(bucketName)).findAny()
				.isPresent()) {
			amazonS3.createBucket(bucketName, Region.EU_Ireland);
		}

		return amazonS3;
	}
}
