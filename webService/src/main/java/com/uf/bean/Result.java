package com.uf.bean;

public class Result {
public static final String RESULT_OK="ok",RESULT_FAIL="fail";
  String result="";
  String mes="";
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

}
