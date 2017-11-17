package com.uf.store.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class UserToken implements AuthenticationToken  {
	private String token;
	public UserToken(String token) {
		this.token=token;
	}
	
	public String getToken() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

}
