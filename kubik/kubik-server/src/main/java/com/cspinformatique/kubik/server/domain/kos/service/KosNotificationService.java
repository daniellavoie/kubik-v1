package com.cspinformatique.kubik.server.domain.kos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.cspinformatique.kubik.server.model.kos.KosNotification;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Action;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Status;
import com.cspinformatique.kubik.server.model.kos.KosNotification.Type;

public interface KosNotificationService {

	KosNotification createNewNotification(int entityId, Type type, Action action);
	
	Page<KosNotification> findAll(Pageable pageable);
	
	List<KosNotification> findByStatus(Status status, Sort sort);
	
	KosNotification findOne(int id);
	
	void initialLoad();
	
	void process(KosNotification kosNotification);

	KosNotification save(KosNotification kosNotification);
}
