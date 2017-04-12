package com.uf.stock.restful.bean;

public class PriceSpeedAnalysisResultData {
private String stockName;
private String stockSymbol;
private float downRateToLowest;
private float sidewayIndex;
private float slowUpFastDownIndex;
private float macd;
private float tKLine;
private String volumePriceUp;


public String getVolumePriceUp() {
  return volumePriceUp;
}
public void setVolumePriceUp(String volumePriceUp) {
  this.volumePriceUp = volumePriceUp;
}
public float gettKLine() {
  return tKLine;
}
public void settKLine(float tKLine) {
  this.tKLine = tKLine;
}
public float getMacd() {
  return macd;
}
public void setMacd(float macd) {
  this.macd = macd;
}
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
public float getDownRateToLowest() {
	return downRateToLowest;
}
public void setDownRateToLowest(float downRateToLowest) {
	this.downRateToLowest = downRateToLowest;
}
public float getSidewayIndex() {
	return sidewayIndex;
}
public void setSidewayIndex(float sidewayIndex) {
	this.sidewayIndex = sidewayIndex;
}
public float getSlowUpFastDownIndex() {
	return slowUpFastDownIndex;
}
public void setSlowUpFastDownIndex(float slowUpFastDownIndex) {
	this.slowUpFastDownIndex = slowUpFastDownIndex;
}

}
