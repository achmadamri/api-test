package com.api.mt.auth.model.auth;

import org.springframework.http.HttpHeaders;

import com.api.mt.auth.model.ResponseModel;

public class TokenSyncResponseModel extends ResponseModel {
	
	public TokenSyncResponseModel(HttpHeaders headers) {
		super(headers);
	}
	
}
