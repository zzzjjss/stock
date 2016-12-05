package com.uf.stock.restful.bean;

public class RestfulResponse {
  private boolean success;
  private String msg;
  private ResponseError error;
  
  public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public boolean isSuccess() {
    return success;
  }
  public void setSuccess(boolean success) {
    this.success = success;
  }
  public ResponseError getError() {
    return error;
  }
  public void setError(ResponseError error) {
    this.error = error;
  }
}
