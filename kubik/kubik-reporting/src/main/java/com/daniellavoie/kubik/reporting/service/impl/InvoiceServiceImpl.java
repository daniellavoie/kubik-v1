package com.daniellavoie.kubik.reporting.service.impl;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.model.sales.Invoice;
import com.daniellavoie.kubik.reporting.rest.KubikTemplate;
import com.daniellavoie.kubik.reporting.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	private KubikTemplate kubikTemplate;

	@Override
	public List<Invoice> findByStatus(String status) {
		return kubikTemplate
				.exchange("/invoice?status=" + status, HttpMethod.GET, new ParameterizedTypeReference<List<Invoice>>() {
				}).getBody();
	}

}
