package com.cspinformatique.kubik.server.domain.sales.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.kos.model.KubikNotification;
import com.cspinformatique.kubik.server.domain.sales.repository.CustomerOrderRepository;
import com.cspinformatique.kubik.server.domain.sales.service.CustomerOrderService;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
	@Resource
	private CustomerOrderRepository customerOrderRepository;

	@Override
	public void processNotification(KubikNotification kubikNotification) {

	}
}
