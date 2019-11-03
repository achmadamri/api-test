package com.api.mt.auth.model.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.api.mt.auth.model.RequestModel;

public class TokenSyncRequestModel extends RequestModel {
	@NotEmpty(message = "Email may not be empty")
	@Email(message = "Email should be valid")
	@Size(min = 1, max = 255, message = "Email must be between 1 and 255 characters long")
	private String email;
	
	@NotEmpty(message = "Salt may not be empty")
	@Size(min = 1, max = 36, message = "Salt must be between 1 and 36 characters long")
	private String salt;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
