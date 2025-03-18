package models;

import java.sql.Date;

public class Pet {
	public int id;
	public String name;
	public int ownerId;
	public String specie;
	public int breedId;
	public String age;
	public Date birthDate;
	public String gender;
	public String hairType;
	public String color;
	public String size;
	public String particularSigns;
	public String reproductiveStatus;
	public boolean hasChip;
	public String habitat;
	public String diet;
	public boolean isActive;
	public int branchId;
	public int createdBy;
	
	
	public Pet() {
		
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


	public int getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}


	public String getSpecie() {
		return specie;
	}


	public void setSpecie(String specie) {
		this.specie = specie;
	}


	public int getBreedId() {
		return breedId;
	}


	public void setBreedId(int breedId) {
		this.breedId = breedId;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public Date getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getHairType() {
		return hairType;
	}


	public void setHairType(String hairType) {
		this.hairType = hairType;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getParticularSigns() {
		return particularSigns;
	}


	public void setParticularSigns(String particularSigns) {
		this.particularSigns = particularSigns;
	}


	public String getReproductiveStatus() {
		return reproductiveStatus;
	}


	public void setReproductiveStatus(String reproductiveStatus) {
		this.reproductiveStatus = reproductiveStatus;
	}


	public boolean isHasChip() {
		return hasChip;
	}


	public void setHasChip(boolean hasChip) {
		this.hasChip = hasChip;
	}


	public String getHabitat() {
		return habitat;
	}


	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}


	public String getDiet() {
		return diet;
	}


	public void setDiet(String diet) {
		this.diet = diet;
	}


	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
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
	
	

}