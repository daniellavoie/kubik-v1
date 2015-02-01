package com.cspinformatique.kubik.proxy.exception;

public class PrinterNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3275830904388441989L;
	
	private String printerName;

	public PrinterNotFoundException(String printerName) {
		this.printerName = printerName;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
}
