package com.cspinformatique.kubik.domain.purchase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.domain.purchase.model.Reception.Status;
import com.cspinformatique.kubik.domain.purchase.model.ReceptionDetail;
import com.cspinformatique.kubik.model.product.Product;

public interface ReceptionDetailService {
	Page<ReceptionDetail> findByProductAndReceptionStatus(Product product, Status status, Pageable pageable);
	
	public ReceptionDetail save(ReceptionDetail receptionDetail);
}
