package com.cspinformatique.kubik.print.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cspinformatique.kubik.print.model.PrintJob;
import com.cspinformatique.kubik.print.repository.PrintJobRepository;
import com.cspinformatique.kubik.print.service.PrintService;
import com.cspinformatique.kubik.proxy.model.Proxy;
import com.cspinformatique.kubik.proxy.service.ProxyService;

@Service
public class PrintServiceImpl implements PrintService{
	@Autowired private PrintJobRepository printJobRepository;
	
	@Autowired private ProxyService proxyService;
	
	@Autowired private RestTemplate restTemplate;
	
	@Override
	public void print(byte[] content){
		Proxy proxy = this.proxyService.find();		
		
		PrintJob printJob = this.printJobRepository.save(new PrintJob(null, new Date(), null, null, proxy.getHostname()));
			
		printJob.setStatus(this.restTemplate.postForEntity("http://" + proxy.getHostname() + ":" + proxy.getPort() + "/print", content, Void.class).getStatusCode());
		
		this.printJobRepository.save(printJob);
	}
}
