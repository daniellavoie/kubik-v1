package com.cspinformatique.livronet.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.cspinformatique.livronet.dilicom.batch.ReferenceEdiMappingEnum;
import com.cspinformatique.livronet.dilicom.model.DilicomReference;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	public @Bean ItemReader<DilicomReference> read(){
		FlatFileItemReader<DilicomReference> reader = new FlatFileItemReader<DilicomReference>();

		reader.setResource(new ClassPathResource("dilicom-flatfile.edi"));
		reader.setLineMapper(new DefaultLineMapper<DilicomReference>(){{
			setLineTokenizer(new FixedLengthTokenizer(){{
				setNames(getDilicomNames());
				setColumns(getDilicomRanges());
			}});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<DilicomReference>() {{
                setTargetType(DilicomReference.class);
            }});
		}});
		
		return reader;
	}
	
	public static String[] getDilicomNames(){
		List<String> names = new ArrayList<String>();

		for(ReferenceEdiMappingEnum mapping : ReferenceEdiMappingEnum.values()){
			names.add(mapping.getFieldName());
		}
		
		return names.toArray(new String[]{});
	}
	
	public static Range[] getDilicomRanges(){
		List<Range> ranges = new ArrayList<Range>();
		for(ReferenceEdiMappingEnum mapping : ReferenceEdiMappingEnum.values()){
			ranges.add(new Range(mapping.getStartRange(), mapping.getEndRange()));
		}
		
		return ranges.toArray(new Range[]{});
	}
}
