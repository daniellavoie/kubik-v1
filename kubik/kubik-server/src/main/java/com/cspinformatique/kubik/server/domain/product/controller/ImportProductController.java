package com.cspinformatique.kubik.server.domain.product.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cspinformatique.kubik.server.domain.product.service.ImportProductService;

@RestController
@RequestMapping("/import-product")
public class ImportProductController {
	private ImportProductService importProductService;

	@Autowired
	public ImportProductController(ImportProductService importProductService) {
		this.importProductService = importProductService;
	}

	@RequestMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void importProductsFromClasspath() throws IOException {
		importProductService.importProducts(new ClassPathResource("import-products.csv").getInputStream());
	}
}
