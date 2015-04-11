package com.cspinformatique.kubik.domain.dilicom.service.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.cspinformatique.kubik.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.domain.dilicom.repository.es.ReferenceRepository;
import com.cspinformatique.kubik.domain.dilicom.service.ImageService;
import com.cspinformatique.kubik.domain.dilicom.service.ReferenceService;
import com.cspinformatique.kubik.domain.product.service.ProductService;
import com.cspinformatique.kubik.model.product.Product;

@Service
public class ReferenceServiceImpl implements ReferenceService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReferenceServiceImpl.class);

	@Autowired
	private ImageService imageService;

	@Autowired
	private ReferenceRepository referenceRepository;
	
	@Autowired
	private ProductService productService;

	public Reference buildReferenceFromProduct(Product product) {
		Reference oldReference = this.findByEan13AndSupplierEan13(
				product.getEan13(), product.getSupplier().getEan13());

		if (oldReference == null) {
			oldReference = new Reference();
		}

		return new Reference(oldReference.getId(), product.getEan13(), product
				.getSupplier().getEan13(),
				oldReference.getPriceApplicationOrAvailabilityDate(),
				oldReference.getAvailability(), oldReference.getPriceType(),
				product.getPriceTaxIn(), product.getSchoolbook(),
				product.getTvaRate1(), product.getPriceTaxOut1(),
				product.getTvaRate2(), product.getPriceTaxOut2(),
				product.getTvaRate3(), product.getPriceTaxOut3(),
				product.getReturnType() != null ? product.getReturnType()
						.getCode() : null, product.getAvailableForOrder(),
				product.getDatePublished(),
				product.getProductType() != null ? product.getProductType()
						.getCode() : null, product.getPublishEndDate(),
				product.getStandardLabel(), product.getCashRegisterLabel(),
				product.getThickness(), product.getWidth(),
				product.getHeight(), product.getWeight(),
				product.getExtendedLabel(), product.getPublisher(),
				product.getCollection(), product.getAuthor(),
				product.getPublisherPresentation(), product.getIsbn(),
				product.getSupplierReference(),
				product.getCollectionReference(), product.getTheme(),
				product.getPublisherIsnb(), product.getReplacesAReference(),
				product.getReplacedByAReference(), product.getReplacesEan13(),
				product.getReplacedByEan13(), product.getOrderableByUnit(),
				product.getBarcodeType() != null ? product.getBarcodeType()
						.getCode() : null, product.getMainReference(),
				product.getSecondaryReference(), product.getReferencesCount(),
				true, product.getImageEncryptedKey(), new Date(), new Date());
	}

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
	public Iterable<Reference> cleanDoubles(Iterable<Reference> references) {
		Map<String, Reference> referencesMap = new HashMap<String, Reference>();

		for (Reference reference : references) {
			String key = reference.getEan13() + "-"
					+ reference.getSupplierEan13();
			if (!referencesMap.containsKey(key)) {
				referencesMap.put(key, reference);
			} else {
				LOGGER.warn("Cleaning reference double for ean13 "
						+ reference.getEan13() + " from supplier "
						+ reference.getSupplierEan13() + ". Reference id "
						+ reference.getId() + ".");

				this.delete(reference.getId());

			}
		}

		return new ArrayList<Reference>(referencesMap.values());
	}
	
	@Override
	public Product createProductFromReference(Reference reference){
		Product product = this.productService.save(this.productService.buildProductFromReference(reference)); 
		
		reference.setImportedInKubik(true);
		
		this.save(reference);
		
		return product;
		
	}

	@Override
	public void delete(String id) {
		this.referenceRepository.delete(id);
	}

	@Override
	public void delete(String ean13, String supplierEan13) {
		this.referenceRepository.delete(this.findByEan13AndSupplierEan13(ean13,
				supplierEan13));
	}

	@Override
	public Reference find(String ean13, String supplierEan13,
			boolean importedInKubik) {
		return this.referenceRepository
				.findByEan13AndSupplierEan13AndImportedInKubik(ean13,
						supplierEan13, importedInKubik);
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
		Reference reference = null;
		List<Reference> references = this.referenceRepository
				.findByEan13AndSupplierEan13(ean13, supplierEan13);

		if (references.size() > 0) {
			reference = references.get(0);
		}

		boolean firstResult = true;
		for (Reference referenceToClean : references) {
			if (!firstResult) {
				LOGGER.warn("Deleting double reference " + reference.getEan13()
						+ " from supplier " + reference.getSupplierEan13()
						+ " created at " + reference.getCreationDate() + ".");

				this.referenceRepository.delete(referenceToClean);
			}

			firstResult = false;
		}

		if (reference == null) {
			return null;
		}

		return reference;
	}

	@Override
	public Page<Reference> findByImportedInKubik(boolean importedInubik,
			Pageable pageable) {
		return this.calculateImageUrl(this.referenceRepository
				.findByImportedInKubik(importedInubik, pageable));
	}
	
	@Override
	public Reference findOne(String id){
		return this.referenceRepository.findOne(id);
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
				Reference oldReference = this.findByEan13AndSupplierEan13(
						reference.getEan13(), reference.getSupplierEan13());

				if (oldReference != null) {
					reference.setId(oldReference.getId());
				}

				referenceMap.put(
						reference.getEan13() + "-"
								+ reference.getSupplierEan13(), reference);
			} catch (RuntimeException ex) {
				LOGGER.error(
						"Error while processing reference "
								+ reference.getEan13() + " from supplier "
								+ reference.getSupplierEan13(), ex);
			}
		}
		
		Iterable<Reference> result = this.calculateImageUrl(this.referenceRepository
				.save(referenceMap.values()));

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(referenceMap.size()  + " references saved.");
		}
		
		return result;
	}

	@Override
	public Page<Reference> search(String query, Pageable pageable) {
		Page<Reference> page = this.referenceRepository.search(query, pageable);

		return this.calculateImageUrl(page);
	}
}
