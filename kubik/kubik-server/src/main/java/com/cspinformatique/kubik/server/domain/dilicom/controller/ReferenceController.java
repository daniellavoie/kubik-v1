package com.cspinformatique.kubik.server.domain.dilicom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.dilicom.model.Reference;
import com.cspinformatique.kubik.server.domain.dilicom.service.ReferenceService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.model.product.Product;

@Controller
@RequestMapping("/reference")
@ConditionalOnProperty(name = "kubik.dilicom.enabled")
public class ReferenceController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ReferenceService referenceService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/product", params = { "ean13", "supplierEan13" })
	public @ResponseBody Product buildProductFromReference(
			@RequestParam String ean13, String supplierEan13) {
		return this.productService
				.buildProductFromReference(this.referenceService
						.findByEan13AndSupplierEan13(ean13, supplierEan13));
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{referenceId}", params = "createProduct")
	public @ResponseBody Product createProduct(@PathVariable String referenceId) {
		return this.referenceService
				.createProductFromReference(this.referenceService
						.findOne(referenceId));
	}

	@RequestMapping(params = "card", produces = MediaType.TEXT_HTML_VALUE)
	public String getReferenceCardPage() {
		return "dilicom/reference-card :: reference-card";
	}

	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String getReferencesPage() {
		return "dilicom/references-page";
	}

	@RequestMapping(method = RequestMethod.GET, params = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<Reference> search(@RequestParam String query,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "50") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "extendedLabel") String sortBy) {
		return this.referenceService.search(query, new PageRequest(page,
				resultPerPage, direction != null ? direction : Direction.ASC,
				sortBy));
	}
}
