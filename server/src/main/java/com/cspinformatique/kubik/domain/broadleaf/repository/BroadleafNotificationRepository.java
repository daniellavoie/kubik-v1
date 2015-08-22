package com.cspinformatique.kubik.domain.broadleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification;
import com.cspinformatique.kubik.model.broadleaf.BroadleafNotification.Status;

public interface BroadleafNotificationRepository extends
		JpaRepository<BroadleafNotification, Integer> {
	List<BroadleafNotification> findByStatus(Status status);
}
