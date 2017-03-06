package com.cspinformatique.kubik.server.domain.dilicom.repository.es.impl;

import java.util.Arrays;

import org.elasticsearch.client.Client;
import org.junit.Assert;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReferenceRepositoryImplIT {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceRepositoryImplIT.class);

	private ReferenceRepositoryImpl referenceRepository;

//	@Before
	public void before() {
		Client client = Mockito.mock(Client.class);
		
		referenceRepository = new ReferenceRepositoryImpl(client, new ObjectMapper());
	}

//	@Test
	public void testReferenceRepository() {
		Reference kubikReference = new Reference();

		kubikReference.setEan13("111");
		kubikReference.setExtendedLabel("Kubik product");
		kubikReference.setImportedInKubik(true);
		kubikReference.setSupplierEan13("444");

		Reference dilicomReference = new Reference();

		dilicomReference.setEan13("222");
		dilicomReference.setExtendedLabel("Dilicom product");

		referenceRepository.save(Arrays.asList(new Reference[] { kubikReference, dilicomReference }));

		Page<Reference> searchResult = referenceRepository.search("product", new PageRequest(0, 10));

		LOGGER.info(searchResult.getContent().size() + " results found.");

		Assert.assertTrue("2 search result expected", searchResult.getTotalElements() == 2);
		Assert.assertTrue(referenceRepository.findByEan13("111").size() == 1);
		Assert.assertTrue(referenceRepository.search("Kubik", new PageRequest(0, 10)).getTotalElements() == 1);
		Assert.assertTrue(
				referenceRepository.findByImportedInKubik(true, new PageRequest(0, 10)).getTotalElements() == 1);
		Assert.assertTrue(referenceRepository.findByEan13AndImportedInKubik("111", true).size() == 1);
		Assert.assertTrue(referenceRepository.findByEan13AndSupplierEan13("111", "444").size() == 1);
		Assert.assertTrue(
				referenceRepository.findByEan13AndSupplierEan13AndImportedInKubik("111", "444", true).isPresent());

		referenceRepository.delete(referenceRepository.findByEan13("222").get(0).getId());

		Assert.assertTrue(referenceRepository.search("product", new PageRequest(0, 10)).getTotalElements() == 1);
		Assert.assertNotNull(referenceRepository
				.findOne((referenceRepository.search("product", new PageRequest(0, 10)).getContent().get(0).getId())));
	}
}
