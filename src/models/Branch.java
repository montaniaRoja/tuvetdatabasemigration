package models;

import java.sql.Date;

public class Branch {
	public int id;
	public String name;
	public String address;
	public String phone;
	public int created_by;
	public Date created_at;
	
	public Branch() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public void setCreated_at(Date date) {
		// TODO Auto-generated method stub
		
	}

	
		
	
}