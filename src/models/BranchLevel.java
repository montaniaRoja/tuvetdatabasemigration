package models;


import java.time.LocalDateTime;

public class BranchLevel {
	public int branch_id;
	public int product_id;
	public int reorder_point;
	public int stock_amount;
	public double product_discount;
	public LocalDateTime created_at;
	public LocalDateTime updated_at;
	
	public BranchLevel() {
		
	}

	public int getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getReorder_point() {
		return reorder_point;
	}

	public void setReorder_point(int reorder_point) {
		this.reorder_point = reorder_point;
	}

	public int getStock_amount() {
		return stock_amount;
	}

	public void setStock_amount(int stock_amount) {
		this.stock_amount = stock_amount;
	}

	public double getProduct_discount() {
		return product_discount;
	}

	public void setProduct_discount(double product_discount) {
		this.product_discount = product_discount;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime localDateTime) {
		this.created_at = localDateTime;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	
	
	
	

}
