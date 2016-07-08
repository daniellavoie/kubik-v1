package com.cspinformatique.kubik.server.domain.reporting.controller;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cspinformatique.kubik.server.domain.reporting.service.ProductSaleService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceService;
import com.cspinformatique.kubik.server.domain.sales.service.InvoiceStatusService;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.model.sales.InvoiceStatus.Types;

@RestController
@RequestMapping("/product-sale")
public class ProductSaleController {
	@Resource
	private ProductSaleService productSaleService;

	@Resource
	private InvoiceService invoiceService;

	@Resource
	private InvoiceStatusService invoiceStatusService;

	@RequestMapping
	public void reloadSalesStats() {
		for (Invoice invoice : invoiceService.findByStatus(invoiceStatusService.findByType(Types.PAID.name()),
				new PageRequest(0, Integer.MAX_VALUE)))
			productSaleService.update(invoice);
	}
}
