package com.uf.stock.service;

import java.util.Date;
import java.util.List;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.bean.StageDefinition;
import com.uf.stock.service.bean.StockStage;

public interface StockAnalysisService {
	public Boolean calculateStockIsDayAverageGoldX(StockInfo stock,int shortTerm,int mediumTerm ,int longTerm);
	public Boolean isDayAverageGoldX(StockInfo stock,Date date,int shortTerm,int longTerm);
	public Float calculateStockPeriodicToLowestPriceDownPercent(StockInfo stock,int periodicDays);
	public StockStage analyseStockStage(List<StageDefinition> stageDefines,StockInfo stock);
	
}
