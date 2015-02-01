package com.cspinformatique.kubik.sales.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cspinformatique.kubik.sales.model.DailyReport;

public interface DailyReportService {
	Page<DailyReport> findAll(Pageable pageable);
	
	DailyReport findOne(int id);
	
	void generateDailyReport(Date date);
	
	void initializeDailyReports();
}
