package com.cspinformatique.kubik.domain.sales.repository;

import org.springframework.data.repository.CrudRepository;

import com.cspinformatique.kubik.model.sales.InvoiceStatus;

public interface InvoiceStatusRepository extends
		CrudRepository<InvoiceStatus, String> {

}
