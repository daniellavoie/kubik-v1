package com.cspinformatique.kubik.kos.domain.kubik.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.kos.model.KubikNotification.Status;

public interface KubikNotificationRepository extends JpaRepository<KubikNotification, Long> {
	List<KubikNotification> findByStatus(Status status, Sort sort);
}
