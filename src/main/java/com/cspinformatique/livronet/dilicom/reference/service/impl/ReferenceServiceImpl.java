package com.cspinformatique.livronet.dilicom.reference.service.impl;

import java.util.List;

import com.cspinformatique.livronet.dilicom.model.Reference;
import com.cspinformatique.livronet.dilicom.reference.repository.ReferenceRepository;
import com.cspinformatique.livronet.dilicom.reference.service.ReferenceService;

public class ReferenceServiceImpl implements ReferenceService{
	private ReferenceRepository referenceRepository;
	
	@Override
	public Reference save(Reference reference) {
		return this.referenceRepository.save(reference);
	}

	@Override
	public Iterable<? extends Reference> save(List<? extends Reference> references) {
		return this.referenceRepository.save(references);
	}

}
