package com.cspinformatique.kubik.server.domain.purchase.task.exception;

public class PurchaseOrderNotDilicomException extends RuntimeException {
	private static final long serialVersionUID = 660225066627527180L;
	
	private String purchaseOrderId;
	
	public PurchaseOrderNotDilicomException(String purchaseOrderId){
		super("Purchase order " + purchaseOrderId + " does not have a dilicom Order.");
		
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}
}
