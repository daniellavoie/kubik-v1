package com.cspinformatique.kubik.server.domain.dilicom.batch.service.impl;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cspinformatique.kubik.server.domain.dilicom.batch.service.JobExecutionService;

@Service
public class JobExecutionServiceImpl implements JobExecutionService {
	
	@Autowired JobLauncher jobLauncher;
	
	@Autowired Job importDilicomReferencesJob;

	@Override
	public void executeJob(String jobName, JobParameters jobParameters) {
		try {
			this.jobLauncher.run(importDilicomReferencesJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException
				| JobInstanceAlreadyCompleteException | JobParametersInvalidException ex) {
			throw new RuntimeException(ex);
		}
	}
}
