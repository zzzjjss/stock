package com.uf.stock.analysis.filter;

import java.util.Date;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.k_analysis.StockStage;
import com.uf.stock.k_analysis.StockStageAnalysis;

public class KLineFilter implements StockFilter{
private int periodDays;
  public KLineFilter(int periodDays){
    this.periodDays=periodDays;
  }
  @Override
  public Boolean doFilter(StockInfo stock, Date date) {
    StockStage stage=StockStageAnalysis.analyseStockStageByKLine(stock.getCode(), date, periodDays);
    if (stage!=null&&stage.getUpPower()>stage.getDownPower()) {
      return true;
    }
    return false;
  }

}
