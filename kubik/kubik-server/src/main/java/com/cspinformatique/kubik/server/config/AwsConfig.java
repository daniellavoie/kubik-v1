package com.cspinformatique.kubik.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
public class AwsConfig {
	@Value("${aws.access.key}")
	private String awsAccessKey;

	@Value("${aws.access.secret}")
	private String awsAccessSecret;

	public @Bean AmazonS3 amazonS3() {
		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsAccessSecret));

		client.setRegion(Region.getRegion(Regions.EU_WEST_1));
		
		return client;
	}
}
