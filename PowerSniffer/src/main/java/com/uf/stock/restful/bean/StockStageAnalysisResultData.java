package com.uf.stock.restful.bean;

public class StockStageAnalysisResultData {
  private String stockName;
  private String stockSymbol;
  private String stageName;
  private Float upPower=0f;
  private Float turnOverRateUp=0f;
  public String getStockName() {
    return stockName;
  }
  public void setStockName(String stockName) {
    this.stockName = stockName;
  }
  public String getStockSymbol() {
    return stockSymbol;
  }
  public void setStockSymbol(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }
  public String getStageName() {
    return stageName;
  }
  public void setStageName(String stageName) {
    this.stageName = stageName;
  }
  public Float getUpPower() {
    return upPower;
  }
  public void setUpPower(Float upPower) {
    this.upPower = upPower;
  }
  public Float getTurnOverRateUp() {
    return turnOverRateUp;
  }
  public void setTurnOverRateUp(Float turnOverRateUp) {
    this.turnOverRateUp = turnOverRateUp;
  }
  
}
