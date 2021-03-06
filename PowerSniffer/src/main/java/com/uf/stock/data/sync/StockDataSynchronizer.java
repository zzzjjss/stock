package com.uf.stock.data.sync;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;
import com.uf.stock.data.bean.TradeDetail;
import com.uf.stock.data.exception.DataSyncException;

public interface StockDataSynchronizer {
  public List<StockInfo> syncAllStocksInfo();
  public StockInfo syncStockInfo(String stockCode);  
  public Map<String,StockInfo> syncStocksInfo(List<String> stockCodes);
  public StockTradeInfo syncStockCurrentTradeInfo(String stockSymbol);
  public Map<String,StockTradeInfo> syncStocksCurrentTradeInfo(List<String> stockSymbol);
  public List<StockTradeInfo> syncStockDateTradeInfos(String stockSymbol,Date start,Date end) throws DataSyncException;
  public List<StockTradeInfoWithAnalysisResult> syncStockDateTradeInfosWithAnalysisResult(String stockSymbol,Date existLatestDataDate) throws DataSyncException;
  public StockTradeInfoWithAnalysisResult syncCurrentStockTradeInfoWithAnalysisResult(String stockSymbol) throws DataSyncException ;
  public List<TradeDetail> syncTradeDetail(String stockSymbol,Date tradeDate);
}
