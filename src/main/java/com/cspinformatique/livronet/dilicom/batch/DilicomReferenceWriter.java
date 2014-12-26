package com.cspinformatique.livronet.dilicom.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.cspinformatique.livronet.dilicom.model.Reference;
import com.cspinformatique.livronet.dilicom.reference.service.ReferenceService;

public class DilicomReferenceWriter implements ItemWriter<Reference> {
	private ReferenceService referenceService;
	
	public DilicomReferenceWriter(ReferenceService referenceService){
		this.referenceService = referenceService;
	}

	@Override
	public void write(List<? extends Reference> items) throws Exception {
		this.referenceService.save(items);
	}
}
