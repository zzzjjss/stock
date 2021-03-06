package com.uf.stock.service;

import java.util.Date;
import java.util.List;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.bean.StageDefinition;
import com.uf.stock.service.bean.StockStage;

public interface StockAnalysisService {
	public Boolean calculateStockIsDayAverageGoldX(StockInfo stock,int shortTerm,int mediumTerm ,int longTerm);
	public Boolean isDayAverageGoldX(StockInfo stock,Date date,int shortTerm,int longTerm);
	public Float calculateAveragePriceBeforeDate(int limit,Date date,Integer stockCode);
	public Float calculateAverageTurnoverRateBeforeDate(int limit, Date date,Integer stockCode);
	public Boolean isCurrentAtLowPrice(StockInfo stock,Date date); 
	public Boolean isPowerUp(StockInfo stock,Date date,float  upPowerDefine);
	public Float calculateStockPeriodicToLowestPriceDownPercent(StockInfo stock,int periodicDays);
	public Float calculateAvgPriceBeforeDate(Integer stockCode, Date date);
	public StockStage analyseStockStage(List<StageDefinition> stageDefines,StockInfo stock);
	public int howManyDaysToUpPercent(String stockSymbol,Date fromDate, float upPercent);
}
