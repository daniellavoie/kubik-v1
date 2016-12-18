package com.daniellavoie.kubik.product.vehicule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.common.rest.KubikTemplate;
import com.cspinformatique.kubik.server.model.product.Category;
import com.cspinformatique.kubik.server.model.product.Supplier;
import com.daniellavoie.kubik.product.vehicule.model.Product;
import com.daniellavoie.kubik.product.vehicule.service.KubikService;

@Service
public class KubikServiceImpl implements KubikService {
	private final static Logger LOGGER = LoggerFactory.getLogger(KubikServiceImpl.class);

	private KubikTemplate kubikTemplate;

	public KubikServiceImpl(KubikTemplate kubikTemplate) {
		this.kubikTemplate = kubikTemplate;
	}

	@Override
	public int saveProduct(Product product) {
		com.cspinformatique.kubik.server.model.product.Product kubikProduct = new com.cspinformatique.kubik.server.model.product.Product();

		kubikProduct.setId(product.getKubikId());
		kubikProduct.setEan13(product.getEan13());
		kubikProduct.setBrand(product.getBrand());
		kubikProduct.setCategory(new Category());
		kubikProduct.getCategory().setId(product.getKubikCategoryId());
		kubikProduct.setName(product.getName());
		kubikProduct.setPriceTaxIn(product.getSellingPrice());
		kubikProduct.setTvaRate1(20d);
		kubikProduct.setWeight((int) product.getWeight());
		kubikProduct.setSupplier(new Supplier());
		kubikProduct.getSupplier().setId(product.getKubikSupplierId());

		LOGGER.info("Notifying " + product + " to Kubik.");

		return kubikTemplate.exchange("/product", HttpMethod.POST, kubikProduct,
				com.cspinformatique.kubik.server.model.product.Product.class).getId();
	}

}
