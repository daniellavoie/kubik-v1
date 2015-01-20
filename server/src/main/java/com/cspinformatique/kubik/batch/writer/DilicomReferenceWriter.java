package com.cspinformatique.kubik.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.kubik.reference.model.Reference;
import com.cspinformatique.kubik.reference.service.ReferenceService;

@Component
public class DilicomReferenceWriter implements ItemWriter<Reference> {
	@Autowired private ReferenceService referenceService;

	@Override
	public void write(List<? extends Reference> items) throws Exception {
		this.referenceService.save(items);
	}
}