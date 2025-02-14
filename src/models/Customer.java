package models;

import java.sql.Date;

public class Customer {
	public int id;
	public String name;
	public int docType;
	public String docNumber;
	public Date birthDate;
	public boolean isCompany;
	public String bussinesType;
	public String address;
	public String phone;
	public String email;
	public boolean hasCredit;
	public int createdBy;
	
	public boolean earnPoints;
	public int creationBranch;
	public Date createdAt;
	
	public Customer() {
		
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

	public int getDocType() {
		return docType;
	}

	public void setDocType(int docType) {
		this.docType = docType;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public boolean isCompany() {
		return isCompany;
	}

	public void setCompany(boolean isCompany) {
		this.isCompany = isCompany;
	}

	public String getBussinesType() {
		return bussinesType;
	}

	public void setBussinesType(String bussinesType) {
		this.bussinesType = bussinesType;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isHasCredit() {
		return hasCredit;
	}

	public void setHasCredit(boolean hasCredit) {
		this.hasCredit = hasCredit;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	

	public boolean isEarnPoints() {
		return earnPoints;
	}

	public void setEarnPoints(boolean earnPoints) {
		this.earnPoints = earnPoints;
	}

	public int getCreationBranch() {
		return creationBranch;
	}

	public void setCreationBranch(int creationBranch) {
		this.creationBranch = creationBranch;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
	

}
