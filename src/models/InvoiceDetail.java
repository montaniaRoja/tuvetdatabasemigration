package models;

import java.sql.Date;

public class InvoiceDetail {
	
	public int id;
	public int invoiceId;
	public int productId;
	public int branchId;
	public int quantity;
	public double unitCost;
	public double unitPrice;
	public double subtotal;
	public double taxAmount;
	public double discountAmount;
	public double lineTotal;
	public boolean stockUpdated;
	public boolean isAnulled;
	public Date createdAt;
	public Date updatedAt;
	
	public InvoiceDetail() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double untiPrice) {
		this.unitPrice = untiPrice;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public boolean isStockUpdated() {
		return stockUpdated;
	}

	public void setStockUpdated(boolean stockUpdated) {
		this.stockUpdated = stockUpdated;
	}

	public boolean isAnulled() {
		return isAnulled;
	}

	public void setAnulled(boolean isAnulled) {
		this.isAnulled = isAnulled;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}	
	

}
