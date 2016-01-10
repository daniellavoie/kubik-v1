package com.cspinformatique.kubik.kos.domain.kubik.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.cspinformatique.kubik.kos.model.kubik.KubikNotification;
import com.cspinformatique.kubik.kos.model.kubik.KubikNotification.Action;
import com.cspinformatique.kubik.kos.model.kubik.KubikNotification.Status;
import com.cspinformatique.kubik.kos.model.kubik.KubikNotification.Type;

public interface KubikNotificationService {
	KubikNotification createNewNotification(long kubikId, Type type, Action action);
	
	Page<KubikNotification> findAll(Pageable pageable);
	
	List<KubikNotification> findByStatus(Status status, Sort sort);
	
	KubikNotification findOne(long id);
	
	void initialLoad();
	
	void process(KubikNotification kubikNotification);

	KubikNotification save(KubikNotification kubikNotification);
}
