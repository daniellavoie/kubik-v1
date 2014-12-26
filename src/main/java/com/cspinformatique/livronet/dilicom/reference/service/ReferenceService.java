package com.cspinformatique.livronet.dilicom.reference.service;

import java.util.List;

import com.cspinformatique.livronet.dilicom.model.Reference;

public interface ReferenceService {
	public Reference save(Reference reference);
	
	public Iterable<? extends Reference> save(List<? extends Reference> references);
}
