package com.cspinformatique.kubik.common.rest;

public enum RestHeaders {
	REASON("reason"), ERROR_CODE("error-code");

	private String headerName;

	RestHeaders(String headerName) {
		this.headerName = headerName;
	}

	public String getHeaderName() {
		return headerName;
	}

	public String toString() {
		return getHeaderName();
	}
}
