package com.uf.stock.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.dao.StockInfoDao;
import com.uf.stock.data.dao.StockTradeInfoDao;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.service.bean.StableStage;
import com.uf.stock.service.bean.StableStageDefinition;
import com.uf.stock.service.bean.StageDefinition;
import com.uf.stock.service.bean.StockStage;

@Service
public class StockAnalysisServiceImpl implements StockAnalysisService {
	@Autowired
	private StockTradeInfoDao tradeInfoDao;
	@Autowired
	private StockInfoDao stockInfoDao;
	
	public Boolean calculateStockIsDayAverageGoldX(StockInfo stock,int shortTerm,int mediumTerm ,int longTerm){
		Calendar calen=Calendar.getInstance();
		StockTradeInfo latestInfo=tradeInfoDao.findLatestDateStockTradeInfo(stock.getSymbol());
		if(latestInfo!=null){
			calen.setTime(latestInfo.getTradeDate());
			calen.add(Calendar.DATE, -1);
			Date before=calen.getTime();
			Float beforeShortAvg=tradeInfoDao.calculateAveragePriceBeforeDate(shortTerm, before, stock.getCode());
			//System.out.println(beforeShortAvg);
			Float shortAvg=tradeInfoDao.calculateAveragePriceBeforeDate(shortTerm,latestInfo.getTradeDate() , stock.getCode());
			Float beforeMedAvg=tradeInfoDao.calculateAveragePriceBeforeDate(mediumTerm, before, stock.getCode());
			Float medAvg=tradeInfoDao.calculateAveragePriceBeforeDate(mediumTerm,latestInfo.getTradeDate() , stock.getCode());
			//Float beforeLongAvg=tradeInfoDao.calculateAveragePriceBeforeDate(longTerm, before, stock.getCode());
			//Float longAvg=tradeInfoDao.calculateAveragePriceBeforeDate(longTerm,latestInfo.getTradeDate() , stock.getCode());
			if(shortAvg>medAvg&&beforeShortAvg<=beforeMedAvg){
				return true;
			}
		}
		return false;
	}
  @Override
  public StockStage analyseStockStage(List<StageDefinition> stageDefines,StockInfo stock){
    if(stageDefines!=null&&stageDefines.size()>0){
      for(StageDefinition stageDefi:stageDefines){
        if(stageDefi instanceof StableStageDefinition){
          StableStageDefinition defi=(StableStageDefinition)stageDefi;
          Date date=new Date();
          float avgPrice=tradeInfoDao.calculateAveragePriceBeforeDate(defi.getDays(), date, stock.getCode());
          float highestPrice=tradeInfoDao.calculateDaysHighestPriceBeforeDate(defi.getDays(), date, stock.getCode());
          float lowestPrice=tradeInfoDao.calculateDaysLowestPriceBeforeDate(defi.getDays(), date, stock.getCode());
          float amplitude=calculateAmplitude(avgPrice,highestPrice,lowestPrice);
          if(amplitude<=defi.getAmplitude()){
            return new StableStage(amplitude);
          }
        }
        
      }
    }
    return null;
  }
  public Float calculateAmplitude(float avgPrice,float highestPrice,float lowestPrice){
      float amplitude=((highestPrice-avgPrice)/avgPrice)+((avgPrice-lowestPrice)/avgPrice);
      return amplitude;
}
  @Override
  public Float calculateStockPeriodicToLowestPriceDownPercent(StockInfo stock,int periodicDays){
    Date date=new Date();
    StockTradeInfo info=tradeInfoDao.findLatestDateStockTradeInfo(stock.getSymbol());
    Float lowestPrice=tradeInfoDao.calculateDaysLowestPriceBeforeDate(periodicDays,date, stock.getCode());
    if(info!=null&&info.getClosePrice()!=null){
      float downPercent=(info.getClosePrice()-lowestPrice)/info.getClosePrice();
      return downPercent;
    }
    return null;
  }
@Override
public Float calculateStockUpPower(Integer stockCode) {
	
	return null;
}
@Override
public Float calculateStockDownPower(Integer stockCode) {
	// TODO Auto-generated method stub
	return null;
}

}
