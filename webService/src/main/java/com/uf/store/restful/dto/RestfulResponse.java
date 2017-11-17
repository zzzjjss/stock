package com.uf.store.restful.dto;

public class RestfulResponse  {
	private ResultCode resultCode;
	private String mes = "";

	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public static enum ResultCode {
		OK, FAIL, NO_LOGIN
	}


}
