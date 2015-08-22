package com.cspinformatique.kubik.error.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.error.model.Error;

public interface ErrorRepository extends JpaRepository<Error, Long> {

}
