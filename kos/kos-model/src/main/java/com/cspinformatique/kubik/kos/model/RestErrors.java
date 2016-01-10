package com.cspinformatique.kubik.kos.model;

public enum RestErrors {
	// Products
	PRODUCT_WEIGHT_UNDEFINED(10000),
	
	// Customer Orders
	TRANSACTION_DOES_NOT_MATCH_ORDER(20000);
	
	private int code;
	
	RestErrors(int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
