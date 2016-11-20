package com.cspinformatique.kubik.server.domain.sales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.sales.InvoiceConfirmation;
import com.cspinformatique.kubik.server.model.sales.InvoiceConfirmation.Status;

public interface InvoiceConfirmationRepository extends JpaRepository<InvoiceConfirmation, Long> {
	List<InvoiceConfirmation> findByStatus(Status status);
}
