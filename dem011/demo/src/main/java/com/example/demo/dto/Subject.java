package com.example.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Subject {
	@NotNull
	@NotEmpty(message = "StudyId is required.")
	private String studyId;
	@NotEmpty(message = "SiteId is required.")
	private String siteId;
	@NotEmpty(message = "SiteNumber is required.")
	private String siteNumber;
	@NotEmpty(message = "SubjectId is required.")
	private String subjectId;
	@NotEmpty(message = "screeningNumber is required.")
	private String screeningNumber;
	@NotEmpty(message = "randamizationNumber is required.")
	private String randamizationNumber;
	@NotEmpty(message = "SiteCountry is required.")
	@Size(min = 4, message = "The length of siteCountry must be greater than 4 characters.")
	private String siteCountry;
	@NotEmpty(message = "StudyId is required.")
	private String gender;
	@NotEmpty(message = "StudyId is required.")
	private String currentStatus;
	
	public Subject(String studyId, String siteId, String siteNumber, String subjectId, String screeningNumber,
			String randamizationNumber, String siteCountry, String gender, String currentStatus) {
		super();
		this.studyId = studyId;
		this.siteId = siteId;
		this.siteNumber = siteNumber;
		this.subjectId = subjectId;
		this.screeningNumber = screeningNumber;
		this.randamizationNumber = randamizationNumber;
		this.siteCountry = siteCountry;
		this.gender = gender;
		this.currentStatus = currentStatus;
	}
	public String getStudyId() {
		return studyId;
	}
	public void setStudyId(String studyId) {
		this.studyId = studyId;
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
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getScreeningNumber() {
		return screeningNumber;
	}
	public void setScreeningNumber(String screeningNumber) {
		this.screeningNumber = screeningNumber;
	}
	public String getRandamizationNumber() {
		return randamizationNumber;
	}
	public void setRandamizationNumber(String randamizationNumber) {
		this.randamizationNumber = randamizationNumber;
	}
	public String getSiteCountry() {
		return siteCountry;
	}
	public void setSiteCountry(String siteCountry) {
		this.siteCountry = siteCountry;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	
}
