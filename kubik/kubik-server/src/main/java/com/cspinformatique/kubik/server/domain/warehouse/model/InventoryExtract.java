package com.cspinformatique.kubik.server.domain.warehouse.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class InventoryExtract {
	private Date date;
	private String separator;
	private DecimalFormat decimalFormat;
	private List<InventoryExtractLine> inventoryExtractLines;
	
	public InventoryExtract(){
		
	}

	public InventoryExtract(Date date, String separator, DecimalFormat decimalFormat,
			List<InventoryExtractLine> inventoryExtractLines) {
		this.date = date;
		this.separator = separator;
		this.decimalFormat = decimalFormat;
		this.inventoryExtractLines = inventoryExtractLines;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public List<InventoryExtractLine> getInventoryExtractLines() {
		return inventoryExtractLines;
	}

	public void setInventoryExtractLines(List<InventoryExtractLine> inventoryExtractLines) {
		this.inventoryExtractLines = inventoryExtractLines;
	}
}
