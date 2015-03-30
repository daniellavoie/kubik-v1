package com.cspinformatique.kubik.domain.sales.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.model.sales.DailyReport;

public interface DailyReportRepository extends
		JpaRepository<DailyReport, Integer> {
	DailyReport findByDate(Date date);
}
