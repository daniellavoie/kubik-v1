package com.cspinformatique.kubik.server.error.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.error.model.Error;

public interface ErrorRepository extends JpaRepository<Error, Long> {

}
