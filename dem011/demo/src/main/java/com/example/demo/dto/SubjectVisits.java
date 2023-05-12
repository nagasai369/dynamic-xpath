package com.example.demo.dto;

public class SubjectVisits {
	
	private String visitId;
	private String patientId;
	private String visitDefId;
	private String name;
	private boolean visitCompleted;
	private String visitEvent;
	private String visitDate;
	private String visitScheduleStartDate;
	private String visitScheduleEndDate;
	private String siteId;
	private String siteNumber;
	private String patientStatus;
	private String screenFailedReason;
	private String sourceReference;
	private String sourceSystem;
	private String monitoringDate;
	private boolean nonPayable;
	
	public SubjectVisits(String visitId, String patientId, String visitDefId, String name, boolean visitCompleted,
			String visitEvent, String visitDate, String visitScheduleStartDate, String visitScheduleEndDate,
			String siteId, String siteNumber, String patientStatus, String screenFailedReason, String sourceReference,
			String sourceSystem, String monitoringDate, boolean nonPayable) {
		super();
		this.visitId = visitId;
		this.patientId = patientId;
		this.visitDefId = visitDefId;
		this.name = name;
		this.visitCompleted = visitCompleted;
		this.visitEvent = visitEvent;
		this.visitDate = visitDate;
		this.visitScheduleStartDate = visitScheduleStartDate;
		this.visitScheduleEndDate = visitScheduleEndDate;
		this.siteId = siteId;
		this.siteNumber = siteNumber;
		this.patientStatus = patientStatus;
		this.screenFailedReason = screenFailedReason;
		this.sourceReference = sourceReference;
		this.sourceSystem = sourceSystem;
		this.monitoringDate = monitoringDate;
		this.nonPayable = nonPayable;
	}
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getVisitDefId() {
		return visitDefId;
	}
	public void setVisitDefId(String visitDefId) {
		this.visitDefId = visitDefId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isVisitCompleted() {
		return visitCompleted;
	}
	public void setVisitCompleted(boolean visitCompleted) {
		this.visitCompleted = visitCompleted;
	}
	public String getVisitEvent() {
		return visitEvent;
	}
	public void setVisitEvent(String visitEvent) {
		this.visitEvent = visitEvent;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getVisitScheduleStartDate() {
		return visitScheduleStartDate;
	}
	public void setVisitScheduleStartDate(String visitScheduleStartDate) {
		this.visitScheduleStartDate = visitScheduleStartDate;
	}
	public String getVisitScheduleEndDate() {
		return visitScheduleEndDate;
	}
	public void setVisitScheduleEndDate(String visitScheduleEndDate) {
		this.visitScheduleEndDate = visitScheduleEndDate;
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
	public String getPatientStatus() {
		return patientStatus;
	}
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}
	public String getScreenFailedReason() {
		return screenFailedReason;
	}
	public void setScreenFailedReason(String screenFailedReason) {
		this.screenFailedReason = screenFailedReason;
	}
	public String getSourceReference() {
		return sourceReference;
	}
	public void setSourceReference(String sourceReference) {
		this.sourceReference = sourceReference;
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
	public boolean isNonPayable() {
		return nonPayable;
	}
	public void setNonPayable(boolean nonPayable) {
		this.nonPayable = nonPayable;
	}
	
	

}
