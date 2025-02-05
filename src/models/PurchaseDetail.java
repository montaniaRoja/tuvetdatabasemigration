package models;

public class PurchaseDetail {
	
	public int id;
	public int purchase_id;
	public int product_id;
	public int quantity;
	public int bonification;
	public int total;
	public Double unit_cost;
	public Double tax_amount;
	
	
	public PurchaseDetail() {
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getPurchase_id() {
		return purchase_id;
	}


	public void setPurchase_id(int purchase_id) {
		this.purchase_id = purchase_id;
	}


	public int getProduct_id() {
		return product_id;
	}


	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public int getBonification() {
		return bonification;
	}


	public void setBonification(int bonification) {
		this.bonification = bonification;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public Double getUnit_cost() {
		return unit_cost;
	}


	public void setUnit_cost(Double unit_cost) {
		this.unit_cost = unit_cost;
	}


	public Double getTax_amount() {
		return tax_amount;
	}


	public void setTax_amount(Double tax_amount) {
		this.tax_amount = tax_amount;
	}
	
	

}
