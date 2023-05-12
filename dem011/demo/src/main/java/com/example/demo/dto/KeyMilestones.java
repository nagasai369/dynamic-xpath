package com.example.demo.dto;

public class KeyMilestones {
	
	private String enrolledDate;
	private String patientInitials;
	private String patientBirth;
	private String subjectScreenFailedDate;
	private String sourceReference;
	private String sourceSystem;
	private String informedConsentDate;
	private String screeningStartDate;
	private String screeningDate;
	private String screenFailedDate;
	private String screenFailedReason;
	private String randomisedDate;
	private String withdrawnDate;
	private String withdrawnReason;
	private String completedDate;
	
	
	public KeyMilestones(String enrolledDate, String patientInitials, String patientBirth,
			String subjectScreenFailedDate, String sourceReference, String sourceSystem, String informedConsentDate,
			String screeningStartDate, String screeningDate, String screenFailedDate, String screenFailedReason,
			String randomisedDate, String withdrawnDate, String withdrawnReason, String completedDate) {
		super();
		this.enrolledDate = enrolledDate;
		this.patientInitials = patientInitials;
		this.patientBirth = patientBirth;
		this.subjectScreenFailedDate = subjectScreenFailedDate;
		this.sourceReference = sourceReference;
		this.sourceSystem = sourceSystem;
		this.informedConsentDate = informedConsentDate;
		this.screeningStartDate = screeningStartDate;
		this.screeningDate = screeningDate;
		this.screenFailedDate = screenFailedDate;
		this.screenFailedReason = screenFailedReason;
		this.randomisedDate = randomisedDate;
		this.withdrawnDate = withdrawnDate;
		this.withdrawnReason = withdrawnReason;
		this.completedDate = completedDate;
	}
	public String getEnrolledDate() {
		return enrolledDate;
	}
	public void setEnrolledDate(String enrolledDate) {
		this.enrolledDate = enrolledDate;
	}
	public String getPatientInitials() {
		return patientInitials;
	}
	public void setPatientInitials(String patientInitials) {
		this.patientInitials = patientInitials;
	}
	public String getPatientBirth() {
		return patientBirth;
	}
	public void setPatientBirth(String patientBirth) {
		this.patientBirth = patientBirth;
	}
	public String getSubjectScreenFailedDate() {
		return subjectScreenFailedDate;
	}
	public void setSubjectScreenFailedDate(String subjectScreenFailedDate) {
		this.subjectScreenFailedDate = subjectScreenFailedDate;
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
	public String getInformedConsentDate() {
		return informedConsentDate;
	}
	public void setInformedConsentDate(String informedConsentDate) {
		this.informedConsentDate = informedConsentDate;
	}
	public String getScreeningStartDate() {
		return screeningStartDate;
	}
	public void setScreeningStartDate(String screeningStartDate) {
		this.screeningStartDate = screeningStartDate;
	}
	public String getScreeningDate() {
		return screeningDate;
	}
	public void setScreeningDate(String screeningDate) {
		this.screeningDate = screeningDate;
	}
	public String getScreenFailedDate() {
		return screenFailedDate;
	}
	public void setScreenFailedDate(String screenFailedDate) {
		this.screenFailedDate = screenFailedDate;
	}
	public String getScreenFailedReason() {
		return screenFailedReason;
	}
	public void setScreenFailedReason(String screenFailedReason) {
		this.screenFailedReason = screenFailedReason;
	}
	public String getRandomisedDate() {
		return randomisedDate;
	}
	public void setRandomisedDate(String randomisedDate) {
		this.randomisedDate = randomisedDate;
	}
	public String getWithdrawnDate() {
		return withdrawnDate;
	}
	public void setWithdrawnDate(String withdrawnDate) {
		this.withdrawnDate = withdrawnDate;
	}
	public String getWithdrawnReason() {
		return withdrawnReason;
	}
	public void setWithdrawnReason(String withdrawnReason) {
		this.withdrawnReason = withdrawnReason;
	}
	public String getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}
	
}
