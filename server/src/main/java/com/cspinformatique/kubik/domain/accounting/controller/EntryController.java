package com.cspinformatique.kubik.domain.accounting.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.kubik.domain.accounting.model.AccountsExports;
import com.cspinformatique.kubik.domain.accounting.model.EntriesExport;
import com.cspinformatique.kubik.domain.accounting.service.EntryService;

@Controller
@RequestMapping("/accounting/entry")
public class EntryController {
	@Autowired
	private EntryService entryService;

	@RequestMapping()
	public String getEntriesExportPage() {
		return "accounting/export";
	}

	@RequestMapping(value = "/accounts", produces = "text/csv; charset=utf-8")
	public @ResponseBody AccountsExports exportAccountsEntries(@RequestParam String separator) {
		return new AccountsExports(entryService.generateAccounts(), "COMPTES", separator);
	}

	@RequestMapping(value = "/sales", produces = "text/csv; charset=utf-8")
	public @ResponseBody EntriesExport exportSalesEntries(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate, @RequestParam String separator) {
		return new EntriesExport(startDate, endDate, entryService.generateSaleJournalEntries(startDate, endDate),
				"VENTES", separator);
	}

	@RequestMapping(value = "/treasury", produces = "text/csv; charset=utf-8")
	public @ResponseBody EntriesExport exportTreasuryEntries(
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
			@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate, @RequestParam String separator) {
		return new EntriesExport(startDate, endDate, entryService.generateTreasuryJournalEntries(startDate, endDate),
				"TRESORERIE", separator);
	}
}
