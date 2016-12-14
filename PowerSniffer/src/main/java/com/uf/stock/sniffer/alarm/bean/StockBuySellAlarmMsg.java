package com.uf.stock.sniffer.alarm.bean;

public class StockBuySellAlarmMsg {
  private String stockName;
  private String stockSymbol;
  private AlarmMsgType msgType;
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
  public AlarmMsgType getMsgType() {
    return msgType;
  }
  public void setMsgType(AlarmMsgType msgType) {
    this.msgType = msgType;
  }
  
  
}
