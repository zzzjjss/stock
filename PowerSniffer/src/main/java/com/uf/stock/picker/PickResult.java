package com.uf.stock.picker;

import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;

public class PickResult {
  private List<StockTradeInfo> allTradeInfos;
  private int pickIndex;
  public List<StockTradeInfo> getAllTradeInfos() {
    return allTradeInfos;
  }
  public void setAllTradeInfos(List<StockTradeInfo> allTradeInfos) {
    this.allTradeInfos = allTradeInfos;
  }
  public int getPickIndex() {
    return pickIndex;
  }
  public void setPickIndex(int pickIndex) {
    this.pickIndex = pickIndex;
  }
  
}
