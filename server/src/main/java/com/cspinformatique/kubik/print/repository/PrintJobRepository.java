package com.cspinformatique.kubik.print.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.print.model.PrintJob;

public interface PrintJobRepository extends JpaRepository<PrintJob, Integer> {

}
