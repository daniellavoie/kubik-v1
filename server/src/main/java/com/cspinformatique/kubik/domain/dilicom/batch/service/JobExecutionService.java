package com.cspinformatique.kubik.domain.dilicom.batch.service;

import org.springframework.batch.core.JobParameters;

public interface JobExecutionService {
	public void executeJob(String jobName, JobParameters jobParameters);
}
