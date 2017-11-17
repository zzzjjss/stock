package com.uf.store.restful.action.manager.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class LoginResponse extends RestfulResponse{
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
