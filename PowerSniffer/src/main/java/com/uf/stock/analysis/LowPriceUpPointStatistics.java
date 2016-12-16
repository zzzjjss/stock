package com.uf.stock.analysis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uf.stock.analysis.filter.DayAverageGoldXFilter;
import com.uf.stock.analysis.filter.LowPriceFilter;
import com.uf.stock.analysis.filter.PowerUpFilter;
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
    public float analyseAccuracy(){
      SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
      int hitNum=0,nohitNum=0;
      StockInfo stock=service.findStockInfoByStockSymbol("sz000850");
      StockTradeInfo info=service.findOldestStockTradeInfo(stock.getCode());
      if(info!=null){
        Date begin=info.getTradeDate();
        Calendar ca=Calendar.getInstance();
        ca.setTime(begin);
        Date now=new Date();
        LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
        chain.appendStockFilter(new LowPriceFilter())
              .appendStockFilter(new PowerUpFilter(3.0f))
             .appendStockFilter(new DayAverageGoldXFilter(2, 5));
        while(ca.getTime().getTime()<now.getTime()){
//          StockTradeInfo tradeInfo=service.findOneDayTradeInfo(stock.getCode(), ca.getTime());
//          if(tradeInfo!=null){
//            float closePrice = tradeInfo.getClosePrice();
//            float targetPrice = closePrice * (1 + targetDefinition.getUpPercent());
//            int days = analyseService.howManyDaysToTargetPrice(stock.getSymbol(), ca.getTime(), targetPrice);
//            if(days<=targetDefinition.getDays()){
//              System.out.println(formate.format(ca.getTime())+"-->"+days+"-->"+tradeInfo.getTurnoverRate()+"%");
//            }
//          }
            boolean isPass=chain.isLowPriceUpPoint(stock, ca.getTime());
            if (isPass) {
                logger.info("lowPrice Up  point:" + formate.format(ca.getTime()));
                float closePrice = service.findOneDayTradeInfo(stock.getCode(), ca.getTime()).getClosePrice();
                float targetPrice = closePrice * (1 + targetDefin.getUpPercent());
                int days = analyseService.howManyDaysToTargetPrice(stock.getSymbol(), ca.getTime(), targetPrice);
                logger.info("after days:" + days+" up to targetPrice");
                if (days <= targetDefin.getDays()) {
                  hitNum++;
                } else {
                  nohitNum++;
                }
            }
          ca.add(Calendar.DATE, 1);
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
            float closePrice = tradeInfo.getClosePrice();
            float targetPrice = closePrice * (1 + targetDefin.getUpPercent());
            int days = analyseService.howManyDaysToTargetPrice(stock.getSymbol(), ca.getTime(), targetPrice);
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
