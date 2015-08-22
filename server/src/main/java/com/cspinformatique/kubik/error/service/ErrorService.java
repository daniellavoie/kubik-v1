package com.cspinformatique.kubik.error.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.error.model.Error;

public interface ErrorService {
	void delete(long id);

	Page<Error> findAll(Pageable pageable);

	Error findOne(long id);

	Error save(Error error);
}
