package models;

import java.sql.Date;

public class Purchase {
	public int id;
	public int supplier_id;
	public String invoice_number;
	public String payment_condition;
	public Double subtotal_amount;
	public Double tax_amount;
	public Double retention_amount;
	public Double total;
	public int branch_id;
	public int created_by;
	public Date created_at;
	
	public Purchase() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getPayment_condition() {
		return payment_condition;
	}

	public void setPayment_condition(String payment_condition) {
		this.payment_condition = payment_condition;
	}

	public double getSubtotal_amount() {
		return subtotal_amount;
	}

	public void setSubtotal_amount(double subtotal_amount) {
		this.subtotal_amount = subtotal_amount;
	}

	public double getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(double tax_amount) {
		this.tax_amount = tax_amount;
	}

	public double getRetention_amount() {
		return retention_amount;
	}

	public void setRetention_amount(double retention_amount) {
		this.retention_amount = retention_amount;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	

}
