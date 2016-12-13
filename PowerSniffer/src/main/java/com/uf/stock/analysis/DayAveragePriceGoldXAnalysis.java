package com.uf.stock.analysis;

import java.text.SimpleDateFormat;
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
      TargetDefinition targetDefinition=new TargetDefinition(7,0.03f);
      SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
      int hitNum=0,nohitNum=0;
      StockInfo stock=service.findStockInfoByStockSymbol("sz000850");
      StockTradeInfo info=service.findOldestStockTradeInfo(stock.getCode());
      if(info!=null){
        Date begin=info.getTradeDate();
        Calendar ca=Calendar.getInstance();
        ca.setTime(begin);
        Date now=new Date();
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
            boolean isPoint1 = analyseService.isDayAverageGoldX(stock, ca.getTime(), 2, 5);
            boolean isPoint = analyseService.isPowerUp(stock, ca.getTime());
            boolean isPoint2 = analyseService.isCurrentAtLowPrice(stock, ca.getTime());
            if (isPoint1&&isPoint&&isPoint2) {
                System.out.println("Tree point:" + formate.format(ca.getTime()));
                float closePrice = service.findOneDayTradeInfo(stock.getCode(), ca.getTime()).getClosePrice();
                float targetPrice = closePrice * (1 + targetDefinition.getUpPercent());
                int days = analyseService.howManyDaysToTargetPrice(stock.getSymbol(), ca.getTime(), targetPrice);
                System.out.println("days:" + days);
                if (days <= targetDefinition.getDays()) {
                  hitNum++;
                } else {
                  nohitNum++;
                }
            }
          ca.add(Calendar.DATE, 1);
        }
      }
      if(hitNum+nohitNum!=0){
        System.out.println("hitNum:"+hitNum+" noHitNum:"+nohitNum);
        return (float)((float)hitNum/(float)(hitNum+nohitNum));
      }else{
        return 0f;
      }
      
    } 
}
