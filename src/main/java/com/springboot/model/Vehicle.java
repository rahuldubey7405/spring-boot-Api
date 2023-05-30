package com.springboot.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle")
@SQLDelete(sql = "UPDATE vehicle SET vehicleStatus = true WHERE id=?")
@Where(clause = "vehicleStatus = 0")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "type")
	private String Type;
	@Column(name = "brand")
	private String brand;
	@Column(name = "model")
	private String model;
	@Column(name = "registrationstate")
	private String registrationState;
	@Column(name = "registrationnumber")
	private String registrationNumber;
	@Column(name = "enginecc")
	private int engineCC;

	private boolean vehicleStatus = Boolean.FALSE; // False = not deleted , true = deleted

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

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRegistrationState() {
		return registrationState;
	}

	public void setRegistrationState(String registrationState) {
		this.registrationState = registrationState;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public int getEngineCC() {
		return engineCC;
	}

	public void setEngineCC(int engineCC) {
		this.engineCC = engineCC;
	}

	public boolean isVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(boolean vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

}
