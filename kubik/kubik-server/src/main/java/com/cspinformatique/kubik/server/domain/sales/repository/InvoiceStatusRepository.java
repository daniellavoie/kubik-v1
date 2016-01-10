package com.cspinformatique.kubik.server.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.server.model.sales.InvoiceStatus;

public interface InvoiceStatusRepository extends
		CrudRepository<InvoiceStatus, String> {
	InvoiceStatus findByType(String type);
}
