package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.k_analysis.KLineAnalysisV2.DayBuySellPower;
import com.uf.stock.k_analysis.StockStageAnalysis;

public class KLineFilterV2 implements StockFilter{
  private int days=1;
public KLineFilterV2(int days){
  this.days=days;
}
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    DayBuySellPower buySellPower=StockStageAnalysis.analyseStockBuySellPower(tradeInfo.getStock().getCode(), tradeInfo.getTradeDate(), days);
    if (buySellPower!=null) {
      result.setFilterValue(buySellPower.getBuyPower()-buySellPower.getSellPower());;
    }
    if (buySellPower!=null&&buySellPower.getBuyPower()>buySellPower.getSellPower()) {
      result.setIsPass(true);
    }else{
      result.setIsPass(false);
    }
    return result;
    
  }

}
