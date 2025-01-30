package models;

import java.sql.Date;

public class Product {
	public int id;
	public String barcode;
	public String name;
	public int brand_id;
	public int category_id;
	public int min_purchase;
	public int min_dist;
	public int tax_id;
	public String scales;
	public float avg_cost;
	public float markup;
	public float customer_price;
	public float discount_percent;
	public float unit_discount;
	public int created_by;
	public boolean is_inventory;
	public float last_cost;
	public boolean is_active;
	public boolean is_chronical;
	public int min_sale;
	public boolean from_store;
	public int fraction_unit;
	public float concentration;
	public String food_type;
	public String measure_content;
	public float supplier_discount;
	public float competitor_price;
	public Date created_at;
	public int inventory_sum;
	
	

	public Product() {
		
	}
	
	public int getInventory_sum() {
		return inventory_sum;
	}

	public void setInventory_sum(int inventory_sum) {
		this.inventory_sum = inventory_sum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getMin_purchase() {
		return min_purchase;
	}

	public void setMin_purchase(int min_purchase) {
		this.min_purchase = min_purchase;
	}

	public int getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(int min_dist) {
		this.min_dist = min_dist;
	}

	public int getTax_id() {
		return tax_id;
	}

	public void setTax_id(int tax_id) {
		this.tax_id = tax_id;
	}

	public String getScales() {
		return scales;
	}

	public void setScales(String scales) {
		this.scales = scales;
	}

	public float getAvg_cost() {
		return avg_cost;
	}

	public void setAvg_cost(float avg_cost) {
		this.avg_cost = avg_cost;
	}

	public float getMarkup() {
		return markup;
	}

	public void setMarkup(float markup) {
		this.markup = markup;
	}

	public float getCustomer_price() {
		return customer_price;
	}

	public void setCustomer_price(float customer_price) {
		this.customer_price = customer_price;
	}

	public float getDiscount_percent() {
		return discount_percent;
	}

	public void setDiscount_percent(float discount_percent) {
		this.discount_percent = discount_percent;
	}

	public float getUnit_discount() {
		return unit_discount;
	}

	public void setUnit_discount(float unit_discount) {
		this.unit_discount = unit_discount;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public boolean isIs_inventory() {
		return is_inventory;
	}

	public void setIs_inventory(boolean is_inventory) {
		this.is_inventory = is_inventory;
	}

	public float getLast_cost() {
		return last_cost;
	}

	public void setLast_cost(float last_cost) {
		this.last_cost = last_cost;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public boolean isIs_chronical() {
		return is_chronical;
	}

	public void setIs_chronical(boolean is_chronical) {
		this.is_chronical = is_chronical;
	}

	public int getMin_sale() {
		return min_sale;
	}

	public void setMin_sale(int min_sale) {
		this.min_sale = min_sale;
	}

	public boolean isFrom_store() {
		return from_store;
	}

	public void setFrom_store(boolean from_store) {
		this.from_store = from_store;
	}

	public int getFraction_unit() {
		return fraction_unit;
	}

	public void setFraction_unit(int fraction_unit) {
		this.fraction_unit = fraction_unit;
	}

	public float getConcentration() {
		return concentration;
	}

	public void setConcentration(float concentration) {
		this.concentration = concentration;
	}

	public String getFood_type() {
		return food_type;
	}

	public void setFood_type(String food_type) {
		this.food_type = food_type;
	}

	public String getMeasure_content() {
		return measure_content;
	}

	public void setMeasure_content(String measure_content) {
		this.measure_content = measure_content;
	}

	public float getSupplier_discount() {
		return supplier_discount;
	}

	public void setSupplier_discount(float supplier_discount) {
		this.supplier_discount = supplier_discount;
	}

	public float getCompetitor_price() {
		return competitor_price;
	}

	public void setCompetitor_price(float competitor_price) {
		this.competitor_price = competitor_price;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	
	
	
	

}