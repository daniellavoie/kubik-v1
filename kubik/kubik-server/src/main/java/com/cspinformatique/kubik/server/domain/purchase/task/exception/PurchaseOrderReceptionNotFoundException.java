package com.cspinformatique.kubik.server.domain.purchase.task.exception;

public class PurchaseOrderReceptionNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 6488368406676092837L;
	
	public PurchaseOrderReceptionNotFoundException(long purchaseOrderId) {
		super("There is no reception linked with purchase order " + purchaseOrderId);
	}
}
