package com.cspinformatique.kubik.domain.accounting.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class EntriesExport {
	private Date startDate;
	private Date endDate;
	private List<Entry> entries;
	private String label;
	private String separator;
	private DecimalFormat decimalFormat;
	
	public EntriesExport() {
		 
	}

	public EntriesExport(Date startDate, Date endDate, List<Entry> entries, String label, String separator, DecimalFormat decimalFormat) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.entries = entries;
		this.label = label;
		this.separator = separator;
		this.decimalFormat = decimalFormat;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}
}