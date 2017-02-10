package com.uf.stock.data.bean;

public class ConfigInfo {
	private boolean isUseProxy=false;
	private String proxyAddress;
	private int proxyPort;
	private String proxyUserName;
	private String proxyPassword;
	
	public String getProxyUserName() {
    return proxyUserName;
  }
  public void setProxyUserName(String proxyUserName) {
    this.proxyUserName = proxyUserName;
  }
  public String getProxyPassword() {
    return proxyPassword;
  }
  public void setProxyPassword(String proxyPassword) {
    this.proxyPassword = proxyPassword;
  }
  public boolean isUseProxy() {
		return isUseProxy;
	}
	public void setIsUseProxy(boolean isUseProxy) {
		this.isUseProxy = isUseProxy;
	}
	public String getProxyAddress() {
		return proxyAddress;
	}
	public void setProxyAddress(String proxyAddress) {
		this.proxyAddress = proxyAddress;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
}
