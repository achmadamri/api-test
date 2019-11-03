package com.api.mt.auth.model.auth;

import org.springframework.http.HttpHeaders;

import com.api.mt.auth.model.ResponseModel;

import io.jsonwebtoken.Claims;

public class AuthGenerateResponseModel extends ResponseModel {

	public AuthGenerateResponseModel(HttpHeaders headers) {
		super(headers);
	}

	private String token;
	
	private Claims claims;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}
}