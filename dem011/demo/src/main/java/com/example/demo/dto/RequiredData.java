package com.example.demo.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RequiredData {
	
	@Valid
	@NotNull
	private SubjectDetails subjectDetails;

	public SubjectDetails getSubjectDetails() {
		return subjectDetails;
	}

	public void setSubjectDetails(SubjectDetails subjectDetails) {
		this.subjectDetails = subjectDetails;
	}

}
