package com.cspinformatique.kubik.server.domain.product.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.product.service.CategoryService;
import com.cspinformatique.kubik.server.domain.product.service.ImportProductService;
import com.cspinformatique.kubik.server.domain.product.service.ProductService;
import com.cspinformatique.kubik.server.domain.product.service.SupplierService;
import com.cspinformatique.kubik.server.model.product.Product;

@Service
public class ImportProductServiceImpl implements ImportProductService {
	private CategoryService categoryService;
	private ProductService productService;
	private SupplierService supplierService;

	@Autowired
	public ImportProductServiceImpl(CategoryService categoryService, ProductService productService,
			SupplierService supplierService) {
		this.categoryService = categoryService;
		this.productService = productService;
		this.supplierService = supplierService;
	}

	@Override
	public void importProducts(InputStream inputStream) {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
			for (String line : buffer.lines().collect(Collectors.toList())) {
				String[] columns = line.split(";");

				Product product = new Product();
				product.setName(columns[0]);
				product.setCategory(categoryService.findOne(Integer.valueOf(columns[1])));
				product.setTvaRate1(Double.valueOf(columns[2]));
				product.setPriceTaxIn(Double.valueOf(columns[3]));
				product.setEan13(columns[4]);
				product.setSupplier(supplierService.findOne(Integer.valueOf(columns[5])));

				productService.save(product);
			}
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}
	}
}
