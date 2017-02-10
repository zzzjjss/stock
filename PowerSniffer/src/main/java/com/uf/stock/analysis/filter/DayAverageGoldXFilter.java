package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class DayAverageGoldXFilter implements StockFilter {
  private int shortTerm, longTerm;
  private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
  public DayAverageGoldXFilter(int shortTerm, int longTerm) {
    this.shortTerm = shortTerm;
    this.longTerm = longTerm;
  }

  public FilterResult  doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    Float avgShort=analyseService.calculateAveragePriceBeforeDate(shortTerm, tradeInfo.getTradeDate(), tradeInfo.getStock().getCode());
    Float avgLong=analyseService.calculateAveragePriceBeforeDate(longTerm, tradeInfo.getTradeDate(), tradeInfo.getStock().getCode());
    result.setFilterValue(avgShort-avgLong);
    if(avgShort>avgLong){
      result.setIsPass(true);
    }else {
      result.setIsPass(false);
    }
    return result;
  }
}
