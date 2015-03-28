package com.cspinformatique.kubik.domain.purchase.task.exception;

public class InvalidReceptionSupplierException extends RuntimeException {
	private static final long serialVersionUID = 6648037881392058871L;
	
	public InvalidReceptionSupplierException(int receptionSupplierId, int shippingSupplierId) {
		super("Supplier " + receptionSupplierId + " does not match supplier " + shippingSupplierId + " from shipping notification.");
	}
}
