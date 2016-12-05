package com.uf.stock.data.bean;

public class AlarmStock {
  private Integer id;
  private Integer stockCode;
  private String stockName;
  private Float alarmBuyPrice;
  private Float alarmSellPrice;
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getStockCode() {
    return stockCode;
  }

  public void setStockCode(Integer stockCode) {
    this.stockCode = stockCode;
  }

  public String getStockName() {
    return stockName;
  }

  public void setStockName(String stockName) {
    this.stockName = stockName;
  }



  public Float getAlarmBuyPrice() {
    return alarmBuyPrice;
  }

  public void setAlarmBuyPrice(Float alarmBuyPrice) {
    this.alarmBuyPrice = alarmBuyPrice;
  }

  public Float getAlarmSellPrice() {
    return alarmSellPrice;
  }

  public void setAlarmSellPrice(Float alarmSellPrice) {
    this.alarmSellPrice = alarmSellPrice;
  }


}
