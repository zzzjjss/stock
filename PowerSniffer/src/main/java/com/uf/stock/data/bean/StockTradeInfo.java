package com.uf.stock.data.bean;

import java.util.Date;

import com.uf.stock.k_analysis.MACD_Analysis.MACDResult;

public class StockTradeInfo implements Comparable<StockTradeInfo>{
  private Long id;
  private Date tradeDate;
  private Float openPrice;
  private Float highestPrice;
  private Float closePrice;
  private Float lowestPrice;
  private Long tradeAmount;
  private Long tradeMoney;
  private Float turnoverRate;
  private Float upDownPrice;
  private Float upDownRate;
  private String stockSymbol;
  private StockInfo stock;
  private Float shortEMA;
  private Float longEMA;
  private MACDResult macdResult;

  

  public MACDResult getMacdResult() {
    return macdResult;
  }

  public void setMacdResult(MACDResult macdResult) {
    this.macdResult = macdResult;
  }

  public Float getShortEMA() {
    return shortEMA;
  }

  public void setShortEMA(Float shortEMA) {
    this.shortEMA = shortEMA;
  }

  public Float getLongEMA() {
    return longEMA;
  }

  public void setLongEMA(Float longEMA) {
    this.longEMA = longEMA;
  }

  public Float getUpDownPrice() {
    return upDownPrice;
  }

  public void setUpDownPrice(Float upDownPrice) {
    this.upDownPrice = upDownPrice;
  }

  public Float getUpDownRate() {
    return upDownRate;
  }

  public void setUpDownRate(Float upDownRate) {
    this.upDownRate = upDownRate;
  }

  public Float getTurnoverRate() {
    return turnoverRate;
  }

  public void setTurnoverRate(Float turnoverRate) {
    this.turnoverRate = turnoverRate;
  }


  public String getStockSymbol() {
    return stockSymbol;
  }

  public void setStockSymbol(String stockSymbol) {
    this.stockSymbol = stockSymbol;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Date tradeDate) {
    this.tradeDate = tradeDate;
  }

  public Float getOpenPrice() {
    return openPrice;
  }

  public void setOpenPrice(Float openPrice) {
    this.openPrice = openPrice;
  }

  public Float getHighestPrice() {
    return highestPrice;
  }

  public void setHighestPrice(Float highestPrice) {
    this.highestPrice = highestPrice;
  }

  public Float getClosePrice() {
    return closePrice;
  }

  public void setClosePrice(Float closePrice) {
    this.closePrice = closePrice;
  }

  public Float getLowestPrice() {
    return lowestPrice;
  }

  public void setLowestPrice(Float lowestPrice) {
    this.lowestPrice = lowestPrice;
  }

  public Long getTradeAmount() {
    return tradeAmount;
  }

  public void setTradeAmount(Long tradeAmount) {
    this.tradeAmount = tradeAmount;
  }

  public Long getTradeMoney() {
    return tradeMoney;
  }

  public void setTradeMoney(Long tradeMoney) {
    this.tradeMoney = tradeMoney;
  }

  public StockInfo getStock() {
    return stock;
  }

  public void setStock(StockInfo stock) {
    this.stock = stock;
  }

  @Override
  public int compareTo(StockTradeInfo o) {
    if(this.tradeDate!=null&&o.getTradeDate()!=null){
      return (int)(this.tradeDate.getTime()-o.getTradeDate().getTime()); 
    }
    return 0;
  }

}
