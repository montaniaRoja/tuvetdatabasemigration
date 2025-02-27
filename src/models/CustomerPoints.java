package models;

import java.sql.Date;

public class CustomerPoints {
	public int id;
	public int customerId;
	public int invoiceId;
	public int userId;
	public double invoiceAmount;
	public double earnedPoints;
	public double redeemedPoints;
	public Date createdAt;
	
	public CustomerPoints() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public double getEarnedPoints() {
		return earnedPoints;
	}

	public void setEarnedPoints(double earnedPoints) {
		this.earnedPoints = earnedPoints;
	}

	public double getRedeemedPoints() {
		return redeemedPoints;
	}

	public void setRedeemedPoints(double redeemedPoints) {
		this.redeemedPoints = redeemedPoints;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
