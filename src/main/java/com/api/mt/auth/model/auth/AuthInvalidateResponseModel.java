package com.api.mt.auth.model.auth;

import org.springframework.http.HttpHeaders;

import com.api.mt.auth.model.ResponseModel;

public class AuthInvalidateResponseModel extends ResponseModel {
	
	public AuthInvalidateResponseModel(HttpHeaders headers) {
		super(headers);
	}
}
