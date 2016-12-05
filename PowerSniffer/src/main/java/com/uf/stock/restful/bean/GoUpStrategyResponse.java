package com.uf.stock.restful.bean;

import java.util.List;

public class GoUpStrategyResponse extends RestfulResponse {
  private List<GoUpStrategyResponseData> data;
  public List<GoUpStrategyResponseData> getData() {
    return data;
  }
  public void setData(List<GoUpStrategyResponseData> data) {
    this.data = data;
  }
  
  
}
