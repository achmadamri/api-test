package com.api.jasa.auth.model.test;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.api.jasa.auth.model.RequestModel;

public class TestGenerateRequestModel extends RequestModel {
	@NotEmpty(message = "Data may not be empty")
	@Size(min = 1, max = 1000, message = "Data must be between 1 and 1000 characters long")
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
