package com.uf.stock.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.AlarmStock;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;

public interface DataSyncService {
    public int syncAllStocksBaseInfo();
    public List<StockInfo>  findStocksPeRatioBetween(Float min,Float max);
    public List<StockInfo>  findStocksInMonitor();
    public AlarmStock findAlarmStockInfoByStockCode(Integer  stockCode);
    public List<AlarmStock> findAllAlarmStocks();
    public List<UpDownPower> calculateStocksCurrentPower(List<StockInfo> stocks);
    public Map<String, StockTradeInfo> getCurrentStocksTradeInfo(List<String> stockSymbols);
    public int syncStockTradeInfos(String stockSymbol);
    public String transToStockSymbolFromStockCode(Integer stockCode);
    public StockTradeInfo findOldestStockTradeInfo(Integer stockCode);
    public StockInfo findStockInfoByStockSymbol(String stockSymbol);
    public StockTradeInfo findOneDayTradeInfo(Integer stockCode,Date date);
    public float calculateAvgPriceBeforeDate(Integer stockCode,Date date);
    public void setAlarmStock(StockInfo stock);
    public List<StockTradeInfo> findTradeInfosBeforeDate(Integer stockCode,Date date,int limitDays);
    public List<StockTradeInfo> findLimitTradeInfosBeforeDate(Integer stockCode,Date date,int limitTradeInfos);
    public boolean isStockStopTrade(Integer stockCode);
}
