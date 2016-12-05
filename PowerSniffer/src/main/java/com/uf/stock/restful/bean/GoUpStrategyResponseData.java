package com.uf.stock.restful.bean;

public class GoUpStrategyResponseData {
    private String stockName;
    private float power;
    private float peRatio;
    private float upDownRate;
    private float downPercent;
    private String stockSymbol;
    public String getStockName() {
      return stockName;
    }
    public void setStockName(String stockName) {
      this.stockName = stockName;
    }
    public float getPower() {
      return power;
    }
    public void setPower(float power) {
      this.power = power;
    }
    public float getUpDownRate() {
      return upDownRate;
    }
    public void setUpDownRate(float upDownRate) {
      this.upDownRate = upDownRate;
    }
    public float getDownPercent() {
      return downPercent;
    }
    public void setDownPercent(float downPercent) {
      this.downPercent = downPercent;
    }
    public float getPeRatio() {
      return peRatio;
    }
    public void setPeRatio(float peRatio) {
      this.peRatio = peRatio;
    }
    public String getStockSymbol() {
      return stockSymbol;
    }
    public void setStockSymbol(String stockSymbol) {
      this.stockSymbol = stockSymbol;
    }
    
    
}
