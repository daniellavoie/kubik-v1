package com.cspinformatique.kubik.sales.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.sales.model.DailyReport;

public interface DailyReportRepository extends
		JpaRepository<DailyReport, Integer> {
	DailyReport findByDate(Date date);
}
