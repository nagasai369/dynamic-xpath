package com.example.demo.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SubjectDetails {
	
	@Valid
	@NotNull
	private List<AllDetails> allOf;

	public List<AllDetails> getAllOf() {
		return allOf;
	}

	public void setAllOf(List<AllDetails> allOf) {
		this.allOf = allOf;
	}
	
	
	
}
