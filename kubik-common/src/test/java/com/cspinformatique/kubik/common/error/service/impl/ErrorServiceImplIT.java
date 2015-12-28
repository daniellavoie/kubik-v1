package com.cspinformatique.kubik.common.error.service.impl;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cspinformatique.kubik.common.error.ErrorTestApplication;
import com.cspinformatique.kubik.common.error.model.ErrorTrace;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ErrorTestApplication.class)
public class ErrorServiceImplIT {
	@Resource
	private ErrorTraceServiceImpl errorTraceServiceImpl;

	@Test
	public void testSave() {
		Assert.assertTrue(errorTraceServiceImpl.save(new ErrorTrace()).getId() != 0l);
	}
}
