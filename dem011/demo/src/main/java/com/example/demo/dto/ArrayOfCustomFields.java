package com.example.demo.dto;

public class ArrayOfCustomFields {
	
	private String patientId;
	private String name;
	private String value;
	
	public ArrayOfCustomFields(String patientId, String name, String value) {
		super();
		this.patientId = patientId;
		this.name = name;
		this.value = value;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
