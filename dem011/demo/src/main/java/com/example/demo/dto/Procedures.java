package com.example.demo.dto;

public class Procedures {
	private String procedureId;
	private String patientId;
	private String procedureDefId;
	private String name;
	private String siteId;
	private String siteNumber;
	private String procedureDate;
	private boolean nonPayable;
	private String visitId;
	private String visitName;
	private String visitDate;
	private String sourceReference;
	private String sourceTable;
	private String sourceSystem;
	private String monitoringDate;
	
	
	public Procedures(String procedureId, String patientId, String procedureDefId, String name, String siteId,
			String siteNumber, String procedureDate, boolean nonPayable, String visitId, String visitName,
			String visitDate, String sourceReference, String sourceTable, String sourceSystem, String monitoringDate) {
		super();
		this.procedureId = procedureId;
		this.patientId = patientId;
		this.procedureDefId = procedureDefId;
		this.name = name;
		this.siteId = siteId;
		this.siteNumber = siteNumber;
		this.procedureDate = procedureDate;
		this.nonPayable = nonPayable;
		this.visitId = visitId;
		this.visitName = visitName;
		this.visitDate = visitDate;
		this.sourceReference = sourceReference;
		this.sourceTable = sourceTable;
		this.sourceSystem = sourceSystem;
		this.monitoringDate = monitoringDate;
	}
	public String getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getProcedureDefId() {
		return procedureDefId;
	}
	public void setProcedureDefId(String procedureDefId) {
		this.procedureDefId = procedureDefId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}
	public String getProcedureDate() {
		return procedureDate;
	}
	public void setProcedureDate(String procedureDate) {
		this.procedureDate = procedureDate;
	}
	public boolean isNonPayable() {
		return nonPayable;
	}
	public void setNonPayable(boolean nonPayable) {
		this.nonPayable = nonPayable;
	}
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}
	public String getVisitName() {
		return visitName;
	}
	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getSourceReference() {
		return sourceReference;
	}
	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
	}
	public String getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getMonitoringDate() {
		return monitoringDate;
	}
	public void setMonitoringDate(String monitoringDate) {
		this.monitoringDate = monitoringDate;
	}
	
	

}
