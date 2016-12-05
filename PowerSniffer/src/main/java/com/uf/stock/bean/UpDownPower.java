package com.uf.stock.bean;

import com.uf.stock.data.bean.StockTradeInfo;



public class UpDownPower implements Comparable<UpDownPower> {
  private Float updownPowerValue;
  private boolean isUpPower;
  private String stockName;
  private Float stockPeRatio;
  private StockTradeInfo tradeInfo;
  
  
  public StockTradeInfo getTradeInfo() {
    return tradeInfo;
  }

  public void setTradeInfo(StockTradeInfo tradeInfo) {
    this.tradeInfo = tradeInfo;
  }

public Float getUpdownPowerValue() {
	return updownPowerValue;
}

public void setUpdownPowerValue(Float updownPowerValue) {
	this.updownPowerValue = updownPowerValue;
}

public boolean isUpPower() {
	return isUpPower;
}

public void setUpPower(boolean isUpPower) {
	this.isUpPower = isUpPower;
}

public String getStockName() {
    return stockName;
  }

  public void setStockName(String stockName) {
    this.stockName = stockName;
  }

  @Override
  public int compareTo(UpDownPower o) {
    return o.getUpdownPowerValue().compareTo(updownPowerValue);
  }

  @Override
  public String toString() {
		  return stockName+ ":"+updownPowerValue.toString()+" :  "+tradeInfo.getUpDownRate()+"%";
  }

  public Float getStockPeRatio() {
    return stockPeRatio;
  }

  public void setStockPeRatio(Float stockPeRatio) {
    this.stockPeRatio = stockPeRatio;
  }
  
}
