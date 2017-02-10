package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.k_analysis.StockStage;
import com.uf.stock.k_analysis.StockStageAnalysis;

public class KLineFilter implements StockFilter{
private int periodDays;
  public KLineFilter(int periodDays){
    this.periodDays=periodDays;
  }
  @Override
  public  FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    StockStage stage=StockStageAnalysis.analyseStockStageByKLine(tradeInfo.getStock().getCode(), tradeInfo.getTradeDate(), periodDays);
    if (stage!=null&&stage.getUpPower()>stage.getDownPower()) {
      result.setIsPass(true);
    }else{
      result.setIsPass(false);
    }
    if(stage!=null){
      result.setFilterValue(stage.getUpPower()-stage.getDownPower());
    }
    return result;
  }

}
