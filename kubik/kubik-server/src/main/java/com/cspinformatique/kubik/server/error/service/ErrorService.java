package com.cspinformatique.kubik.server.error.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.server.error.model.Error;

public interface ErrorService {
	void delete(long id);

	Page<Error> findAll(Pageable pageable);

	Error findOne(long id);

	Error save(Error error);
}
