package com.cspinformatique.kubik.server.domain.sales.service.impl;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.sales.service.ProductSaleService;
import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.cspinformatique.kubik.server.rest.KubikReportingTemplate;

@Service
public class ProductSaleServiceImpl implements ProductSaleService {
	private KubikReportingTemplate kubikReportingTemplate;

	public ProductSaleServiceImpl(KubikReportingTemplate kubikReportingTemplate) {
		this.kubikReportingTemplate = kubikReportingTemplate;
	}

	@Override
	public void update(Invoice invoice) {
		kubikReportingTemplate.exchange("/product-sale", HttpMethod.POST, invoice, Void.class);
	}

}
