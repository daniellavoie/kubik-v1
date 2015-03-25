package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.sales.model.InvoiceStatus;

public interface InvoiceStatusRepository extends
		CrudRepository<InvoiceStatus, String> {

}
