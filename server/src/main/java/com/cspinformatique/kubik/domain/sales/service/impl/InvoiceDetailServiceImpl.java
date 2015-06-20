package com.cspinformatique.kubik.domain.sales.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.domain.sales.repository.InvoiceDetailRepository;
import com.cspinformatique.kubik.domain.sales.service.InvoiceDetailService;
import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.sales.InvoiceDetail;
import com.cspinformatique.kubik.model.sales.InvoiceStatus;

@Service
public class InvoiceDetailServiceImpl implements InvoiceDetailService {
	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;

	@Override
	public List<InvoiceDetail> findByProduct(Product product){
		return this.invoiceDetailRepository.findByProduct(product);
	}
	
	@Override
	public Page<InvoiceDetail> findByProductAndInvoiceStatus(Product product,
			InvoiceStatus status, Pageable pageable) {
		return this.invoiceDetailRepository.findByProductAndInvoiceStatus(
				product, status, pageable);
	}
	
	@Override
	public InvoiceDetail save(InvoiceDetail invoiceDetail){
		return this.invoiceDetailRepository.save(invoiceDetail);
	}

}
