package com.cspinformatique.kubik.common.error.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.common.error.model.ErrorTrace;
import com.cspinformatique.kubik.common.error.repository.ErrorTraceRepository;
import com.cspinformatique.kubik.common.error.service.ErrorTraceService;

@Service
public class ErrorTraceServiceImpl implements ErrorTraceService {
	@Resource private ErrorTraceRepository errorTraceRepository;
	
	@Override
	public ErrorTrace save(ErrorTrace errorTrace) {
		return errorTraceRepository.save(errorTrace);
	}
}
