package com.uf.stock.data.dao;

import com.uf.stock.data.bean.StockInfo;


public interface StockInfoDao extends CommonDao<StockInfo>{
	public StockInfo findStockBySymbol(String stockSymbol);
	public StockInfo findStockByStockCode(Integer stockCode);
	public Integer deleteAll();
}
