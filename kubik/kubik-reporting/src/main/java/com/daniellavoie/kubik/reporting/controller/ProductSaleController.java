package com.daniellavoie.kubik.reporting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus.Types;
import com.daniellavoie.kubik.reporting.model.ProductSale;
import com.daniellavoie.kubik.reporting.service.InvoiceService;
import com.daniellavoie.kubik.reporting.service.ProductSaleService;

@RestController
@RequestMapping("/product-sale")
public class ProductSaleController {
	private ProductSaleService productSaleService;
	private InvoiceService invoiceService;

	public ProductSaleController(ProductSaleService productSaleService, InvoiceService invoiceService) {
		this.productSaleService = productSaleService;
		this.invoiceService = invoiceService;
	}

	@RequestMapping
	@GetMapping(params = "reload")
	public void reloadSalesStats() {
		for (Invoice invoice : invoiceService.findByStatus(Types.PAID.name()))
			productSaleService.update(invoice);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void update(@RequestBody Invoice invoice) {
		productSaleService.update(invoice);
	}

	@GetMapping
	public List<ProductSale> findAll() {
		return productSaleService.findAll();
	}
}
