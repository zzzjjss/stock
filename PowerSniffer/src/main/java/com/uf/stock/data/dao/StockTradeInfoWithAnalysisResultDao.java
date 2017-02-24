package com.uf.stock.data.dao;

import java.util.Date;
import java.util.List;

import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;

public interface StockTradeInfoWithAnalysisResultDao extends CommonDao<StockTradeInfoWithAnalysisResult>{
  public StockTradeInfoWithAnalysisResult  findLatestDateStockTradeInfo(String stockSymbol);
  public void batchInsertInfos(List<StockTradeInfoWithAnalysisResult> infors);
  public List<StockTradeInfoWithAnalysisResult>  findAllInforDateAsc(String stockSymbol);
  public List<StockTradeInfoWithAnalysisResult>  findLimitInfosBeforeDateDateAsc(String stockSymbol,Date date,int limit);
}
