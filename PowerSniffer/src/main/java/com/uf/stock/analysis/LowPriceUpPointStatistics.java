package com.uf.stock.analysis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uf.stock.analysis.filter.DayAverageGoldXFilter;
import com.uf.stock.analysis.filter.KLineFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class LowPriceUpPointStatistics {
    private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
    private Logger logger = LogManager.getLogger(LowPriceUpPointStatistics.class);
    private DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
    private TargetDefinition targetDefin;
    public LowPriceUpPointStatistics(TargetDefinition targetDefin){
      this.targetDefin=targetDefin;
    }
    public float analyseAccuracy(String stockSymbol)throws IllegalArgumentException{
      SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
      int hitNum=0,nohitNum=0;
      StockInfo stock=service.findStockInfoByStockSymbol(stockSymbol);
      if (stock==null) {
        throw new IllegalArgumentException("stockSymbol is invalid");
      }
      
      List<StockTradeInfo> tradeInfos=service.findAllTradeInfosOrderByDateAsc(stock.getCode());
      if (tradeInfos!=null) {
        LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
        chain.appendStockFilter(new PriceFilter(tradeInfos,50f)).appendStockFilter(new DayAverageGoldXFilter(5, 10))
             .appendStockFilter(new KLineFilter(5));
        for (StockTradeInfo stockTradeInfo : tradeInfos) {
          boolean isPass=chain.isLowPriceUpPoint(stock,stockTradeInfo.getTradeDate());
          if (isPass) {
            logger.info("lowPrice Up  point:" + formate.format(stockTradeInfo.getTradeDate()));
            int index=tradeInfos.indexOf(stockTradeInfo);
            if(index<tradeInfos.size()-1){
              float upPercent=0f;
              int days=-1;
              for(int tmp=index+1;tmp<tradeInfos.size();tmp++){
                StockTradeInfo tmpTradeInfo=tradeInfos.get(tmp);
                upPercent=upPercent+tmpTradeInfo.getUpDownRate();
                days++;
                if(upPercent>=targetDefin.getUpPercent()){
                  break;
                }
              }
              logger.info("after days:" + days+" up to targetUpPercent");
              if (days <= targetDefin.getDays()) {
                hitNum++;
              } else {
                nohitNum++;
              }
            }
        }
        }
      }
      if(hitNum+nohitNum!=0){
        float accuracy=(float)((float)hitNum/(float)(hitNum+nohitNum));
        logger.info("hitNum:"+hitNum+" noHitNum:"+nohitNum+" ; accuracy :"+accuracy);
        return accuracy;
      }else{
        return 0f;
      }
      
    }
    
    public Map<String,BuyPointStatisticsData> statisticBuypointData(String stockSymbol,Date start,Date end){
      Map<String,BuyPointStatisticsData>  result=new HashMap<String, BuyPointStatisticsData>();
      SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
      StockInfo stock=service.findStockInfoByStockSymbol(stockSymbol);
//      StockTradeInfo info=service.findOldestStockTradeInfo(stock.getCode());
      if(start!=null){
        Calendar ca=Calendar.getInstance();
        ca.setTime(start);
       
        while(ca.getTime().getTime()<end.getTime()){
          StockTradeInfo tradeInfo=service.findOneDayTradeInfo(stock.getCode(), ca.getTime());
          if(tradeInfo!=null){
            int days = analyseService.howManyDaysToUpPercent(stock.getSymbol(), ca.getTime(), targetDefin.getUpPercent());
            System.out.println(days);
            if(days<=targetDefin.getDays()){
              BuyPointStatisticsData data=new BuyPointStatisticsData();
              float power=tradeInfo.getUpDownRate()/tradeInfo.getTurnoverRate();
              data.setUpPower(power);
              float avgPrice=analyseService.calculateAvgPriceBeforeDate(stock.getCode(), ca.getTime());
              float downPercent=tradeInfo.getClosePrice()-avgPrice/tradeInfo.getClosePrice();
              data.setDownPercentToAvgPrice(downPercent);
              result.put(formate.format(ca.getTime()), data);
            }
          }
           ca.add(Calendar.DATE, 1);
        }
      }
      return result;
    }
}
