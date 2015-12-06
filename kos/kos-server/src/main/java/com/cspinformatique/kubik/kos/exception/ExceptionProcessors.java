package com.cspinformatique.kubik.kos.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class ExceptionProcessors {
	@Resource
	private List<ExceptionProcessor<?>> exceptionProcessorList;

	private Map<Class<?>, ExceptionProcessor<?>> processorsMap;

	@PostConstruct
	public void init() {
		processorsMap = new HashMap<>();

		exceptionProcessorList.stream()
				.forEach(exceptionProcessor -> processorsMap.put(exceptionProcessor.supports(), exceptionProcessor));
	}

	public ExceptionProcessor<?> getExceptionProcessor(Class<?> clazz) {
		return processorsMap.get(clazz);
	}
}
