package com.cspinformatique.livronet.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cspinformatique.livronet.dilicom.batch.DilicomReferenceReader;
import com.cspinformatique.livronet.dilicom.model.DilicomReference;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	public @Bean ItemReader<DilicomReference> dilicomReferenceReader(){
		return new DilicomReferenceReader();
	}
}
