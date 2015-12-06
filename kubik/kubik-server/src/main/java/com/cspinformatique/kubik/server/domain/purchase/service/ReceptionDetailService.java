package com.cspinformatique.kubik.server.domain.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.model.product.Product;
import com.cspinformatique.kubik.server.model.purchase.ReceptionDetail;
import com.cspinformatique.kubik.server.model.purchase.Reception.Status;

public interface ReceptionDetailService {
	List<ReceptionDetail> findByProduct(Product product);
	
	Page<ReceptionDetail> findByProductAndReceptionStatus(Product product, Status status, Pageable pageable);
	
	ReceptionDetail save(ReceptionDetail receptionDetail);
}
