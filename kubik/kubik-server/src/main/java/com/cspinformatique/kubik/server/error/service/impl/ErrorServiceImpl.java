package com.cspinformatique.kubik.server.error.service.impl;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.error.model.Error;
import com.cspinformatique.kubik.server.error.repository.ErrorRepository;
import com.cspinformatique.kubik.server.error.service.ErrorService;

@Service
public class ErrorServiceImpl implements ErrorService {
	@Autowired
	private ErrorRepository errorRepository;

	@Override
	public void delete(long id) {
		this.errorRepository.delete(id);
	}

	@Override
	public Page<Error> findAll(Pageable pageable) {
		return this.errorRepository.findAll(pageable);
	}

	@Override
	public Error findOne(long id) {
		return errorRepository.findOne(id);
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public Error save(Error error) {
		return errorRepository.saveAndFlush(error);
	}

}
