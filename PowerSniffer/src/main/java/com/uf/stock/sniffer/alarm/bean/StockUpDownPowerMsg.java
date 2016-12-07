package com.uf.stock.sniffer.alarm.bean;

public class StockUpDownPowerMsg {
  private String stockName;
  private String stockSymbol;
  private Float power;
  private Boolean isUpPower;
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
  public Float getPower() {
    return power;
  }
  public void setPower(Float power) {
    this.power = power;
  }
  public Boolean getIsUpPower() {
    return isUpPower;
  }
  public void setIsUpPower(Boolean isUpPower) {
    this.isUpPower = isUpPower;
  }
  
}
