package com.cspinformatique.kubik.server.domain.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.kubik.server.domain.sales.service.DailyReportService;
import com.cspinformatique.kubik.server.model.sales.DailyReport;

@Controller
@RequestMapping("/dailyReport")
public class DailyReportController {
	@Autowired
	private DailyReportService dailyReportService;

	@RequestMapping(value = "/init")
	public void init() throws Exception {
		this.dailyReportService.initializeDailyReports();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void recalculateDailyReport(@PathVariable int id) {
		this.dailyReportService.generateDailyReport(this.dailyReportService
				.findOne(id).getDate());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getDailyReportDetailsPage() {
		return "sales/daily-report/daily-report-details";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getDailyReportPage() {
		return "sales/daily-report/daily-report-page";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<DailyReport> find(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer resultPerPage,
			@RequestParam(required = false) Direction direction,
			@RequestParam(defaultValue = "date") String sortBy) {
		return this.dailyReportService.findAll(new PageRequest(page,
				resultPerPage, direction != null ? direction : Direction.DESC,
				sortBy));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DailyReport findOne(@PathVariable int id) {
		return this.dailyReportService.findOne(id);
	}
}
