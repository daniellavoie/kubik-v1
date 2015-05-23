package com.cspinformatique.kubik.model.product;

public class ProductStats {
	private Product product;
	private Double quantityOrdered;
	private Double quantityReceived;
	private Double quantitySold;
	private Double quantityReturned;
	private Double quantityRefunded;

	public ProductStats() {

	}

	public ProductStats(Product product, Double quantityOrdered, Double quantityReceived,
			Double quantitySold, Double quantityReturned,
			Double quantityRefunded) {
		this.product = product;
		this.quantityOrdered = quantityOrdered;
		this.quantityReceived = quantityReceived;
		this.quantitySold = quantitySold;
		this.quantityReturned = quantityReturned;
		this.quantityRefunded = quantityRefunded;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Double quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Double getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Double quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public Double getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(Double quantitySold) {
		this.quantitySold = quantitySold;
	}

	public Double getQuantityReturned() {
		return quantityReturned;
	}

	public void setQuantityReturned(Double quantityReturned) {
		this.quantityReturned = quantityReturned;
	}

	public Double getQuantityRefunded() {
		return quantityRefunded;
	}

	public void setQuantityRefunded(Double quantityRefunded) {
		this.quantityRefunded = quantityRefunded;
	}
}
