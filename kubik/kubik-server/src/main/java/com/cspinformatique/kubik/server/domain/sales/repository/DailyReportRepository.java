package com.cspinformatique.kubik.server.domain.sales.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cspinformatique.kubik.server.model.sales.DailyReport;

public interface DailyReportRepository extends
		JpaRepository<DailyReport, Integer> {
	DailyReport findByDate(Date date);
}
