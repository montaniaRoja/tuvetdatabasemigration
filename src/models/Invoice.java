package models;

import java.sql.Date;
import java.sql.Time;

public class Invoice {
	
	public int id;
	public String invoiceNumber;
	public int branchId;
	public int customerId;
	public int createdBy;
	public int processedBy;
	public int anulledBy;
	public double subTotal;
	public double taxedAmount;
	public double taxAmount;
	public double untaxedAmount;
	public double total;
	public double discountAmount;
	public double saleCost;
	public double cashPaid;
	public double cardPaid;
	public double creditPaid;
	public double pointsPaid;
	public String authorizationNumber;
	public boolean alreadyPaid;
	public boolean isAnulled;
	public Date anullationDate;
	public String paymentCondition;
	public Time startTime; 
	public Time endTime;
	public String orderNumber;
	public Date createdAt;
	public Date updatedAt;
	
	public Invoice() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(int processedBy) {
		this.processedBy = processedBy;
	}

	public int getAnulledBy() {
		return anulledBy;
	}

	public void setAnulledBy(int anulledBy) {
		this.anulledBy = anulledBy;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getTaxedAmount() {
		return taxedAmount;
	}

	public void setTaxedAmount(double taxedAmount) {
		this.taxedAmount = taxedAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getUntaxedAmount() {
		return untaxedAmount;
	}

	public void setUntaxedAmount(double untaxedAmount) {
		this.untaxedAmount = untaxedAmount;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getSaleCost() {
		return saleCost;
	}

	public void setSaleCost(double saleCost) {
		this.saleCost = saleCost;
	}

	public double getCashPaid() {
		return cashPaid;
	}

	public void setCashPaid(double cashPaid) {
		this.cashPaid = cashPaid;
	}

	public double getCardPaid() {
		return cardPaid;
	}

	public void setCardPaid(double cardPaid) {
		this.cardPaid = cardPaid;
	}

	public double getCreditPaid() {
		return creditPaid;
	}

	public void setCreditPaid(double creditPaid) {
		this.creditPaid = creditPaid;
	}

	public double getPointsPaid() {
		return pointsPaid;
	}

	public void setPointsPaid(double pointsPaid) {
		this.pointsPaid = pointsPaid;
	}

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public boolean isAlreadyPaid() {
		return alreadyPaid;
	}

	public void setAlreadyPaid(boolean alreadyPaid) {
		this.alreadyPaid = alreadyPaid;
	}

	public boolean isAnulled() {
		return isAnulled;
	}

	public void setAnulled(boolean isAnulled) {
		this.isAnulled = isAnulled;
	}

	public Date getAnullationDate() {
		return anullationDate;
	}

	public void setAnullationDate(Date anullationDate) {
		this.anullationDate = anullationDate;
	}

	public String getPaymentCondition() {
		return paymentCondition;
	}

	public void setPaymentCondition(String paymentCondition) {
		this.paymentCondition = paymentCondition;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	
	
	
	
	

}
