package com.cspinformatique.kubik.kos.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.cspinformatique.kubik.common.rest.RestException;
import com.cspinformatique.kubik.kos.model.RestErrors;
import com.cspinformatique.kubik.kos.model.product.Product;

public class ProductWithoutWeightException extends RestException {
	private static final long serialVersionUID = -2387569841805334452L;

	private final Product product;

	public ProductWithoutWeightException(final Product product) {
		super("Le poids du produit " + product.getEan13() + " n'est pas définit.", HttpStatus.INTERNAL_SERVER_ERROR,
				RestErrors.PRODUCT_WEIGHT_UNDEFINED.getCode());

		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
}
