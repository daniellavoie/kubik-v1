package com.cspinformatique.kubik.proxy.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cspinformatique.kubik.print.model.ReceiptPrintJob;
import com.cspinformatique.kubik.proxy.service.ServerService;
import com.cspinformatique.kubik.sales.model.Invoice;

@Service
public class ServerServiceImpl implements ServerService {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${kubik.proxy.server.url}")
	private String serverUrl;
	
	@Override
	public void deleteReceiptPrintJob(int id){
		this.restTemplate.delete(this.serverUrl + "/invoice/" + id + "receipt");
	}
	
	@Override
	public Iterable<ReceiptPrintJob> findPendingReceiptPrintJob(){
		return Arrays.asList(this.restTemplate.getForObject(serverUrl + "/print", ReceiptPrintJob[].class));
	}
	
	@Override
	public byte[] loadInvoiceReceiptData(Invoice invoice){
		return this.restTemplate.getForObject(this.serverUrl + "/invoice/" + invoice.getId() + "/receipt", byte[].class);
	}
}
