package com.uf.stock.data.sync;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.uf.stock.data.bean.ConfigInfo;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.exception.DataSyncException;

public class StockDataSynchronizerImpl implements StockDataSynchronizer {
  @Autowired
  private ConfigInfo configInfo;

  private SinaDataSynchronizer sinaSync;
  private SoHuDataSynchronizer sohuSyn;

  @PostConstruct
  private void afterConstruct() {
    sinaSync = new SinaDataSynchronizer(configInfo);
    sohuSyn = new SoHuDataSynchronizer(configInfo);
  }

  public List<StockInfo> syncAllStocksInfo() {

    return sinaSync.syncAllStocksInfo();
  }

  public StockInfo syncStockInfo(String stockCode) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, StockInfo> syncStocksInfo(List<String> stockCodes) {
    // TODO Auto-generated method stub
    return null;
  }

  public StockTradeInfo syncStockCurrentTradeInfo(String stockSymbol) {
    Map<String, StockTradeInfo> result = syncStocksCurrentTradeInfo(Arrays.asList(stockSymbol));
    return result.get(stockSymbol);
  }

  public Map<String, StockTradeInfo> syncStocksCurrentTradeInfo(List<String> stockSymbol) {
    SinaDataSynchronizer sinaSync = new SinaDataSynchronizer(configInfo);
    return sinaSync.syncStocksCurrentTradeInfo(stockSymbol);
  }

  @Override
  public List<StockTradeInfo> syncStockDateTradeInfos(String stockSymbol, Date start, Date end)  throws DataSyncException{

    return sohuSyn.syncStockDateTradeInfos(stockSymbol, start, end);
  }



}
