package com.uf.stock.data.bean;

import java.util.Date;

public class TradeDetail {
private Date tradeTime;
private float price;
private float upPercent;
private float upPrice;
private int tradeAmount;
private int tradeMoney;
private TradeType tradeType;
public Date getTradeTime() {
	return tradeTime;
}
public void setTradeTime(Date tradeTime) {
	this.tradeTime = tradeTime;
}
public float getPrice() {
	return price;
}
public void setPrice(float price) {
	this.price = price;
}
public float getUpPercent() {
	return upPercent;
}
public void setUpPercent(float upPercent) {
	this.upPercent = upPercent;
}
public float getUpPrice() {
	return upPrice;
}
public void setUpPrice(float upPrice) {
	this.upPrice = upPrice;
}
public int getTradeAmount() {
	return tradeAmount;
}
public void setTradeAmount(int tradeAmount) {
	this.tradeAmount = tradeAmount;
}
public int getTradeMoney() {
	return tradeMoney;
}
public void setTradeMoney(int tradeMoney) {
	this.tradeMoney = tradeMoney;
}
public TradeType getTradeType() {
	return tradeType;
}
public void setTradeType(TradeType tradeType) {
	this.tradeType = tradeType;
}

}
