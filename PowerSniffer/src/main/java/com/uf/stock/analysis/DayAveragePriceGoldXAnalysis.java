package com.uf.stock.analysis;

import java.util.Calendar;
import java.util.Date;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class DayAveragePriceGoldXAnalysis {
    private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
    private DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
    public float analyseAccuracy(){
      TargetDefinition targetDefinition=new TargetDefinition(7,0.01f);
      int hitNum=0,nohitNum=0;
      StockInfo stock=service.findStockInfoByStockSymbol("sz002565");
      StockTradeInfo info=service.findOldestStockTradeInfo(stock.getCode());
      if(info!=null){
        Date begin=info.getTradeDate();
        Calendar ca=Calendar.getInstance();
        ca.setTime(begin);
        Date now=new Date();
        while(ca.getTime().getTime()<now.getTime()){
          System.out.println(ca.getTime());
          boolean isPoint=analyseService.isDayAverageGoldX(stock, ca.getTime(), 5, 10);
          if(isPoint){
            System.out.println("isGoldX day:"+ca.getTime());
            float closePrice=service.findOneDayTradeInfo(stock.getCode(), ca.getTime()).getClosePrice();
            float targetPrice=closePrice*(1+targetDefinition.getUpPercent());
            int days=analyseService.howManyDaysToTargetPrice(stock.getSymbol(), ca.getTime(), targetPrice);
            if(days<=targetDefinition.getDays()){
              hitNum++;
            }else{
              nohitNum++;
            }
          }
          ca.add(Calendar.DATE, 1);
        }
      }
      if(hitNum+nohitNum!=0){
        System.out.println("hitNum:"+hitNum+" noHitNum:"+nohitNum);
        return hitNum/(hitNum+nohitNum);
      }else{
        return 0f;
      }
      
    } 
}
