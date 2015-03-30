package com.cspinformatique.kubik.domain.sales.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.model.sales.DailyReport;

public interface DailyReportService {
	Page<DailyReport> findAll(Pageable pageable);
	
	DailyReport findOne(int id);
	
	void generateDailyReport(Date date);
	
	void initializeDailyReports();
}
