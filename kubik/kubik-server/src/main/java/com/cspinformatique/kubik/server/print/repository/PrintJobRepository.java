package com.cspinformatique.kubik.server.print.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.print.ReceiptPrintJob;

public interface PrintJobRepository extends JpaRepository<ReceiptPrintJob, Integer> {

}