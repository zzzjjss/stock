package com.uf.stock.k_analysis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class StockStageAnalysis {
  
  public static StockStage analyseStockStage(Integer stockCode,Date date){
    SimpleDateFormat  formate=new SimpleDateFormat("yyyy-MM-dd");
    DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
    Calendar calendar=Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, -50);
    while(calendar.getTime().getTime()<date.getTime()){
      Date date2=calendar.getTime();
      StockTradeInfo tradeInfo=service.findOneDayTradeInfo(stockCode, date2);
      if(tradeInfo!=null){
        KLineState state=KLineAnalysis.analyseKLineState(tradeInfo.getOpenPrice(), tradeInfo.getClosePrice(), tradeInfo.getHighestPrice(), tradeInfo.getLowestPrice());
        System.out.println(formate.format(date2)+"-->"+state.toString());
      }
      calendar.add(Calendar.DATE, 1);
    }
    return null;
  }
}
