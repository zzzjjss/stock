package com.uf.stock.analysis.filter;

import java.util.Date;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class DayAverageGoldXFilter implements StockFilter {
  private int shortTerm, longTerm;
  private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
  public DayAverageGoldXFilter(int shortTerm, int longTerm) {
    this.shortTerm = shortTerm;
    this.longTerm = longTerm;
  }

  public Boolean doFilter(StockInfo stock, Date date) {
    return analyseService.isDayAverageGoldX(stock, date, shortTerm, longTerm);
  }
}
