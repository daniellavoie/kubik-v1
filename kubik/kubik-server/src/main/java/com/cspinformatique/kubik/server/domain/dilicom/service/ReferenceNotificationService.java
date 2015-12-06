package com.cspinformatique.kubik.server.domain.dilicom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.domain.dilicom.model.ReferenceNotification;
import com.cspinformatique.kubik.server.domain.dilicom.model.ReferenceNotification.Status;
import com.cspinformatique.kubik.server.model.product.Product;

public interface ReferenceNotificationService {
	
	Long countByStatus(Status status);
	
	Page<ReferenceNotification> findByStatus(Status status, Pageable pageable);
	
	ReferenceNotification save(ReferenceNotification referenceNotification);
	
	Iterable<ReferenceNotification> save(Iterable<ReferenceNotification> referenceNotification);
	
	void validate(Integer referenceNotificationId, Product product);
	
}
