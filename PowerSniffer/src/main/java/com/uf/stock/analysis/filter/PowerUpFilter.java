package com.uf.stock.analysis.filter;

import java.util.Date;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class PowerUpFilter implements StockFilter{
  private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
  private float upPowerDefine;
  public PowerUpFilter(float upPowerDefine){
    this.upPowerDefine=upPowerDefine;
  }
  @Override
  public Boolean doFilter(StockInfo stock, Date date) {
    return analyseService.isPowerUp(stock, date,upPowerDefine);
  }
  
}
