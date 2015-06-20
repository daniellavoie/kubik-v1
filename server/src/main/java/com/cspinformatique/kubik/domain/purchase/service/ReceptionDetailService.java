package com.cspinformatique.kubik.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.product.Product;
import com.cspinformatique.kubik.model.purchase.ReceptionDetail;
import com.cspinformatique.kubik.model.purchase.Reception.Status;

public interface ReceptionDetailService {
	List<ReceptionDetail> findByProduct(Product product);
	
	Page<ReceptionDetail> findByProductAndReceptionStatus(Product product, Status status, Pageable pageable);
	
	ReceptionDetail save(ReceptionDetail receptionDetail);
}
