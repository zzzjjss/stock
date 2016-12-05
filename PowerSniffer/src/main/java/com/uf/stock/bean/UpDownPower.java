package com.uf.stock.bean;

import com.uf.stock.data.bean.StockTradeInfo;



public class UpDownPower implements Comparable<UpDownPower> {
  private Float powerValue;
  private String stockName;
  private Float stockPeRatio;
  private StockTradeInfo tradeInfo;
  
  
  public StockTradeInfo getTradeInfo() {
    return tradeInfo;
  }

  public void setTradeInfo(StockTradeInfo tradeInfo) {
    this.tradeInfo = tradeInfo;
  }

  public Float getPowerValue() {
    return powerValue;
  }

  public void setPowerValue(Float powerValue) {
    this.powerValue = powerValue;
  }


 

  public String getStockName() {
    return stockName;
  }

  public void setStockName(String stockName) {
    this.stockName = stockName;
  }

  @Override
  public int compareTo(UpDownPower o) {
    return o.getPowerValue().compareTo(powerValue);
  }

  @Override
  public String toString() {
    return stockName+ ":" + powerValue.toString()+":  "+tradeInfo.getUpDownRate()+"%";
  }

  public Float getStockPeRatio() {
    return stockPeRatio;
  }

  public void setStockPeRatio(Float stockPeRatio) {
    this.stockPeRatio = stockPeRatio;
  }
  
}
