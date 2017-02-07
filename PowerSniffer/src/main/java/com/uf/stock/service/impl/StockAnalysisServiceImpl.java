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
import com.uf.stock.service.DataSyncService;
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
	@Autowired
	private DataSyncService service;
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
  public Boolean isDayAverageGoldX(StockInfo stock, Date date, int shortTerm, int longTerm) {
//    Calendar calen = Calendar.getInstance();
//    calen.setTime(date);
//    calen.add(Calendar.DATE, -1);
//    Date before = calen.getTime();
//    Float beforeShortAvg = tradeInfoDao.calculateAveragePriceBeforeDate(shortTerm, before, stock.getCode());
//    Float beforeMedAvg = tradeInfoDao.calculateAveragePriceBeforeDate(longTerm, before, stock.getCode());
    Float shortAvg = tradeInfoDao.calculateAveragePriceBeforeDate(shortTerm, date, stock.getCode());
    Float medAvg = tradeInfoDao.calculateAveragePriceBeforeDate(longTerm, date,stock.getCode());
//    if (shortAvg > medAvg && beforeShortAvg <= beforeMedAvg) {
    if (shortAvg > medAvg ) {
      return true;
    }
    if (shortAvg==medAvg) {
      return null;
    }
    return false;
  }
  @Override
  public int howManyDaysToUpPercent(String stockSymbol,Date fromDate, float targetUpPercent){
    List<StockTradeInfo> result=tradeInfoDao.findByHql("from StockTradeInfo s where s.stockSymbol=? and  s.tradeDate>?  order by s.tradeDate asc", stockSymbol,fromDate);
    //List<StockTradeInfo> result= tradeInfoDao.findLimitByHql("from StockTradeInfo s where s.stockSymbol=? and  s.tradeDate>? and s.highestPrice>=? order by s.tradeDate asc",1, stockSymbol,fromDate,targetPrice);
//    if(result!=null&&result.size()>0){
//      StockTradeInfo info=result.get(0);
//      long days=tradeInfoDao.countHql("from StockTradeInfo s where s.stockSymbol=? and  s.tradeDate>? and s.tradeDate<?  ", stockSymbol,fromDate,info.getTradeDate()) ;
//      return (int)days;
//    }
    int days=-1;
    if(result!=null&&result.size()>0){
      float upPercent=0f;
      for (StockTradeInfo stockTradeInfo : result) {
        upPercent=upPercent+stockTradeInfo.getUpDownRate();
        days++;
        if(upPercent>=targetUpPercent){
          break;
        }
      }
    }
    return days;
  }
@Override
public Boolean isPowerUp(StockInfo stock, Date date,float upPowerDefine) {
	StockTradeInfo tradeInfo=service.findOneDayTradeInfo(stock.getCode(), date);	
//	if(tradeInfo!=null&&tradeInfo.getTurnoverRate()>tradeInfoDao.calculateAvgTurnoverRate(stock.getCode())&&tradeInfo.getUpDownRate()>0){
//	  float lowest=tradeInfoDao.calculateLowestPriceBeforeDate(stock.getCode(),date);
//	  float downPercent=(tradeInfo.getClosePrice()-lowest)/tradeInfo.getClosePrice();
//	  if(downPercent<0.2){
//	    return true;
//	  }
//	}
	if(tradeInfo!=null&&tradeInfo.getUpDownRate()>0){
		float power=tradeInfo.getUpDownRate()/tradeInfo.getTurnoverRate();
		if(power>upPowerDefine){
			return true;
		}
	}
	return false;
}
@Override
public Boolean isCurrentAtLowPrice(StockInfo stock, Date date) {
 StockTradeInfo tradeInfo=service.findOneDayTradeInfo(stock.getCode(), date);
//  if(tradeInfo!=null&&tradeInfo.getUpDownRate()>0){
//    float lowest=tradeInfoDao.calculateLowestPriceBeforeDate(stock.getCode(),date);
//    float downPercent=(tradeInfo.getClosePrice()-lowest)/tradeInfo.getClosePrice();
//    if(downPercent<0.2){
//      return true;
//    }
//  }
//  return false;
  if(tradeInfo!=null){
    Float avg=tradeInfoDao.calculateAvgPriceBeforeDate( stock.getCode(),date);
    if(avg!=null&&avg.floatValue()!=0&&tradeInfo.getClosePrice()<avg.floatValue()){
      return true;
    }else{
      return false;
    }
  }
  return false;
}
@Override
public Float calculateAvgPriceBeforeDate(Integer stockCode, Date date){
  return tradeInfoDao.calculateAvgPriceBeforeDate(stockCode, date);
}

}
