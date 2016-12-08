package com.uf.stock.restful.bean;

public class LowPriceBuyStrategyResponseData {
  private String stockName;
  private String stockSymbol;
  private float alarmBuyPrice=0.0f;
  private float alarmSellPrice=0.0f;
  private float downPercent=0.0f;
  public String getStockName() {
    return stockName;
  }
  public void setStockName(String stockName) {
    this.stockName = stockName;
  }
  public float getAlarmBuyPrice() {
    return alarmBuyPrice;
  }
  public void setAlarmBuyPrice(float alarmBuyPrice) {
    this.alarmBuyPrice = alarmBuyPrice;
  }
  public float getAlarmSellPrice() {
    return alarmSellPrice;
  }
  public void setAlarmSellPrice(float alarmSellPrice) {
    this.alarmSellPrice = alarmSellPrice;
  }
  public float getDownPercent() {
    return downPercent;
  }
  public void setDownPercent(float downPercent) {
    this.downPercent = downPercent;
  }
  public String getStockSymbol() {
    return stockSymbol;
  }
  public void setStockSymbol(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }
 
}
