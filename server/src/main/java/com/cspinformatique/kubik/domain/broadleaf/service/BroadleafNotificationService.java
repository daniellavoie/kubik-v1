package com.cspinformatique.kubik.domain.broadleaf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification.Status;

public interface BroadleafNotificationService {

	Page<BroadleafNotification> findAll(Pageable pageable);
	
	List<BroadleafNotification> findByStatus(Status status);
	
	BroadleafNotification findOne(int id);
	
	void process(BroadleafNotification broadleafNotification);

	BroadleafNotification save(BroadleafNotification broadleafNotification);
}
