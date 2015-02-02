package com.cspinformatique.kubik.sales.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.sales.model.DailyReport;
import com.cspinformatique.kubik.sales.service.DailyReportService;

@Controller
@RequestMapping("/dailyReport")
public class DailyReportController implements InitializingBean {
	@Autowired
	private DailyReportService dailyReportService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.dailyReportService.initializeDailyReports();
	}

	@RequestMapping(value = "/{dailyReportID}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
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
	
	@RequestMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DailyReport findOne(@PathVariable int id){
		return this.dailyReportService.findOne(id);
	}
}
