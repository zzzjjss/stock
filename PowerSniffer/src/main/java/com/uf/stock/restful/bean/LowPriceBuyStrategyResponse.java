package com.uf.stock.restful.bean;

import java.util.List;

public class LowPriceBuyStrategyResponse extends RestfulResponse{
  private List<LowPriceBuyStrategyResponseData> data;
  public List<LowPriceBuyStrategyResponseData> getData() {
    return data;
  }
  public void setData(List<LowPriceBuyStrategyResponseData> data) {
    this.data = data;
  }
  
}
