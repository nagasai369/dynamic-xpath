package com.example.demo.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AllDetails {
	@Valid
	@NotNull
	private Subject subject;
	@Valid
	@NotNull
	private KeyMilestones keyMilestones;
	@Valid
	@NotNull
	private SubjectCustomFields subjectCustomFields;
	@Valid
	@NotNull
	private List<SubjectVisits> subjectVisits;
	@Valid
	@NotNull
	private List<Procedures> procedures;
	
	
	public AllDetails(Subject subject, KeyMilestones keyMilestones, SubjectCustomFields subjectCustomFields,
			List<SubjectVisits> subjectVisits, List<Procedures> procedures) {
		super();
		this.subject = subject;
		this.keyMilestones = keyMilestones;
		this.subjectCustomFields = subjectCustomFields;
		this.subjectVisits = subjectVisits;
		this.procedures = procedures;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public KeyMilestones getKeyMilestones() {
		return keyMilestones;
	}
	public void setKeyMilestones(KeyMilestones keyMilestones) {
		this.keyMilestones = keyMilestones;
	}
	public SubjectCustomFields getSubjectCustomFields() {
		return subjectCustomFields;
	}
	public void setSubjectCustomFields(SubjectCustomFields subjectCustomFields) {
		this.subjectCustomFields = subjectCustomFields;
	}
	public List<SubjectVisits> getSubjectVisits() {
		return subjectVisits;
	}
	public void setSubjectVisits(List<SubjectVisits> subjectVisits) {
		this.subjectVisits = subjectVisits;
	}
	public List<Procedures> getProcedures() {
		return procedures;
	}
	public void setProcedures(List<Procedures> procedures) {
		this.procedures = procedures;
	}
	
}
