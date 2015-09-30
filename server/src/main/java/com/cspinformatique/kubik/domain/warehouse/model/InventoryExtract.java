package com.cspinformatique.kubik.domain.warehouse.model;

import java.util.Date;
import java.util.List;

public class InventoryExtract {
	private Date date;
	private List<InventoryExtractLine> inventoryExtractLines;
	
	public InventoryExtract(){
		
	}

	public InventoryExtract(Date date, List<InventoryExtractLine> inventoryExtractLines) {
		this.date = date;
		this.inventoryExtractLines = inventoryExtractLines;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<InventoryExtractLine> getInventoryExtractLines() {
		return inventoryExtractLines;
	}

	public void setInventoryExtractLines(List<InventoryExtractLine> inventoryExtractLines) {
		this.inventoryExtractLines = inventoryExtractLines;
	}
}
