package com.uf.wechat.bean;

public class WechatAppConfig {
	private String appId;
	private String appSecret;
	
	public WechatAppConfig(String appId, String appSecret) {
		super();
		this.appId = appId;
		this.appSecret = appSecret;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
}
