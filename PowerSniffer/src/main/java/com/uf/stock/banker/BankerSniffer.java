package com.uf.stock.banker;

import java.util.Date;

import com.uf.stock.k_analysis.AnalysisResult;
import com.uf.stock.k_analysis.StockStageAnalysis;

public class BankerSniffer {
	public float calculateOpenPositionIndex(Integer stockCode){
		int period=100;
		AnalysisResult result=StockStageAnalysis.periodAnalyseStock(stockCode, new Date(), period);
		return result.getAvgDownSpeed()/result.getAvgUpSpeed();
	}
}
