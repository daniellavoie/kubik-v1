package com.cspinformatique.kubik.common.error.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.common.error.model.ErrorTrace;

public interface ErrorTraceRepository extends JpaRepository<ErrorTrace, Long> {

}
