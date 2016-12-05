package com.uf.stock.service;

import java.util.List;
import java.util.Map;

import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.AlarmStock;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;

public interface DataSyncService {
    public int syncAllStocksBaseInfo();
    public List<StockInfo>  findStocksPeRatioBetween(Float min,Float max);
    public AlarmStock findAlarmStockInfoByStockCode(Integer  stockCode);
    public List<AlarmStock> findAllAlarmStocks();
    public List<UpDownPower> calculateStocksCurrentPower(List<StockInfo> stocks);
    public Map<String, StockTradeInfo> getCurrentStocksTradeInfo(List<String> stockSymbols);
    public int syncStockTradeInfos(String stockSymbol);
    public int syncAllStocksTradeInfo();
    public String transToStockSymbolFromStockCode(Integer stockCode);
}
