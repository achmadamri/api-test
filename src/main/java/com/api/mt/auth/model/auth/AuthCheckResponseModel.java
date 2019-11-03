package com.api.mt.auth.model.auth;

import org.springframework.http.HttpHeaders;

import com.api.mt.auth.model.ResponseModel;

import io.jsonwebtoken.Claims;

public class AuthCheckResponseModel extends ResponseModel {
	
	public AuthCheckResponseModel(HttpHeaders headers) {
		super(headers);
	}
	
	private Claims claims;

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}
}
