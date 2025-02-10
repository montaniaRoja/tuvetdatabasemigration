package models;

import java.sql.Date;

public class Transfer {
	
	public int id;
	public int from_id;
	public int to_id;
	public int created_by;
	public int authorized_by;
	public int received_by;
	public String status;
	public Date created_at;
	public Date updated_at;
	
	public Transfer() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFrom_id() {
		return from_id;
	}

	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}

	public int getTo_id() {
		return to_id;
	}

	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getAuthorized_by() {
		return authorized_by;
	}

	public void setAuthorized_by(int authorized_by) {
		this.authorized_by = authorized_by;
	}

	public int getReceived_by() {
		return received_by;
	}

	public void setReceived_by(int received_by) {
		this.received_by = received_by;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	

}
