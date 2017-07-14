package com.uf.bean;

public class Result<T> {
	public static final String RESULT_OK = "ok", RESULT_FAIL = "fail", RESULT_NO_LOGIN = "fail";
	String result = "";
	String mes = "";
	T  data;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


}
