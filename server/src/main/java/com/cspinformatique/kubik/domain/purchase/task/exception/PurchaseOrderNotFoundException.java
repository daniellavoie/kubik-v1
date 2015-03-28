package com.cspinformatique.kubik.domain.purchase.task.exception;

public class PurchaseOrderNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5254096893360461890L;

	private String purchaseOrderId;
	
	public PurchaseOrderNotFoundException(String purchaseOrderId){
		super("Purchase order " + purchaseOrderId + " could not be found.");
		
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}
}
