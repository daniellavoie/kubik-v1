package com.cspinformatique.kubik.reference.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.product.service.ImageService;
import com.cspinformatique.kubik.reference.model.Reference;
import com.cspinformatique.kubik.reference.repository.ReferenceRepository;
import com.cspinformatique.kubik.reference.service.ReferenceService;

@Service
public class ReferenceServiceImpl implements ReferenceService {
	private static final Logger logger = LoggerFactory
			.getLogger(ReferenceServiceImpl.class);

	@Autowired
	private ImageService imageService;

	@Autowired
	private ReferenceRepository referenceRepository;

	private Iterable<Reference> calculateImageUrl(Iterable<Reference> references) {
		for (Reference reference : references) {
			this.calculateImageUrl(reference);
		}

		return references;
	}

	private Page<Reference> calculateImageUrl(Page<Reference> referencePage) {
		for (Reference reference : referencePage.getContent()) {
			this.calculateImageUrl(reference);
		}

		return referencePage;
	}

	private Reference calculateImageUrl(Reference reference) {
		reference.setImageEncryptedKey(this.imageService.getEncryptedUrl(
				reference.getEan13(), reference.getSupplierEan13()));

		return reference;
	}

	@Override
	public Iterable<Reference> findByEan13(String ean13, Sort sort) {
		return this.calculateImageUrl(this.referenceRepository.findByEan13(
				ean13, sort));
	}

	@Override
	public Iterable<Reference> findByEan13AndImportedInKubik(String ean13,
			boolean importedInKubik, Sort sort) {
		return this.referenceRepository.findByEan13AndImportedInKubik(ean13,
				importedInKubik, sort);
	}

	@Override
	public Reference findByEan13AndSupplierEan13(String ean13,
			String supplierEan13) {
		return this.calculateImageUrl(this.referenceRepository
				.findByEan13AndSupplierEan13(ean13, supplierEan13));
	}

	@Override
	public Page<Reference> findByImportedInKubik(boolean importedInubik,
			Pageable pageable) {
		return this.calculateImageUrl(this.referenceRepository
				.findByImportedInKubik(importedInubik, pageable));
	}

	@Override
	public Reference save(Reference reference) {
		return calculateImageUrl(this.referenceRepository.save(reference));
	}

	@Override
	public Iterable<? extends Reference> save(
			List<? extends Reference> references) {
		Map<String, Reference> referenceMap = new HashMap<String, Reference>();
		for (Reference reference : references) {
			try {
				Reference oldReference = this.referenceRepository
						.findByEan13AndSupplierEan13(reference.getEan13(),
								reference.getSupplierEan13());

				if (oldReference != null) {
					reference.setId(oldReference.getId());
				}

				referenceMap.put(
						reference.getEan13() + "-"
								+ reference.getSupplierEan13(), reference);
			} catch (RuntimeException ex) {
				logger.error(
						"Error while processing reference "
								+ reference.getEan13() + " from supplier "
								+ reference.getSupplierEan13(), ex);
			}
		}

		return this.calculateImageUrl(this.referenceRepository
				.save(referenceMap.values()));
	}

	@Override
	public Page<Reference> search(String query, String[] fields,
			Boolean importedInKubik, Pageable pageable) {
		return this.calculateImageUrl(this.referenceRepository.search(query,
				fields, importedInKubik, pageable));
	}

}
