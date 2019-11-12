package com.api.mt.auth.model.auth;

import com.api.mt.auth.model.ResponseModel;

import io.jsonwebtoken.Claims;

public class AuthCheckResponseModel extends ResponseModel {
	
	public AuthCheckResponseModel(AuthCheckRequestModel requestModel) {
		super(requestModel);
	}
	
	private Claims claims;

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}
}
