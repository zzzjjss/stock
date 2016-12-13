package com.uf.stock.data.dao;

import java.util.Date;
import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;

public interface StockTradeInfoDao extends CommonDao<StockTradeInfo>{
  public StockTradeInfo  findLatestDateStockTradeInfo(String stockSymbol);
  public List<StockTradeInfo>  findLatestDaysStockTradeInfos(String stockSymbol,int days);
  public Float calculateDaysHighestPriceBeforeDate(int days,Date date,Integer stockCode);
  public Float calculateDaysLowestPriceBeforeDate(int days,Date date,Integer stockCode);
  public Float calculateAveragePriceBeforeDate(int limit,Date date,Integer stockCode);
  public float calculateAvgPriceBeforeDate(Integer stockCode, Date date);
  public float calculateAvgTurnoverRate(Integer stockCode);
  public float calculateLowestPrice(Integer  stockCode);
  public float calculateLowestPriceBeforeDate(Integer  stockCode,Date date);
  public int exrightBeforeDate(Integer stockCode,Date date,Float exPercent);
}
