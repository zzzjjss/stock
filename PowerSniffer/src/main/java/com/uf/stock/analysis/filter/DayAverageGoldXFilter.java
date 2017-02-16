package com.uf.stock.analysis.filter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class DayAverageGoldXFilter implements StockFilter {
  private int shortTerm, longTerm;
  private List<StockTradeInfo> orderedTradeInfos;
  private Map<String, Integer> dateToIndexMap=new HashMap<String,Integer>();
  private SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
  public DayAverageGoldXFilter(List<StockTradeInfo> orderedTradeInfos,int shortTerm, int longTerm) {
    this.shortTerm = shortTerm;
    this.longTerm = longTerm;
    this.orderedTradeInfos=orderedTradeInfos;
    if (orderedTradeInfos!=null&&orderedTradeInfos.size()>=0) {
      for (int index=0;index<orderedTradeInfos.size();index++) {
        dateToIndexMap.put(formate.format(orderedTradeInfos.get(index).getTradeDate()) ,index);
      }
    }
  }

  public FilterResult  doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    Calendar calen = Calendar.getInstance();
    calen.setTime(tradeInfo.getTradeDate());
    calen.add(Calendar.DATE, -1);
    Date before = calen.getTime();
    
    Float beforeShortAvg = calculateAveragePriceBeforeDate(before,shortTerm);
    Float beforeMedAvg = calculateAveragePriceBeforeDate( before,longTerm);
    
    
    Float avgShort=calculateAveragePriceBeforeDate(tradeInfo.getTradeDate(),shortTerm);
    Float avgLong=calculateAveragePriceBeforeDate(tradeInfo.getTradeDate(),longTerm);
    
    result.setFilterValue(avgShort-avgLong);
    if(beforeShortAvg<=beforeMedAvg&&avgShort>avgLong){
      result.setIsPass(true);
    }else {
      result.setIsPass(false);
    }
    return result;
  }
  
  private Float calculateAveragePriceBeforeDate(Date date,int term){
    Integer index=dateToIndexMap.get(formate.format(date));
    if (index==null) {
      return -1f;
    }
    StockTradeInfo info=orderedTradeInfos.get(index);
    if (info==null) {
      return -1f;
    }else {
      int start=(index-term)+1;
      if (start<0) {
        start=0;
      }
      float sum=0f;
      int realDays=0;
      for(int i=start;i<=index;i++){
        StockTradeInfo tmpInfo=orderedTradeInfos.get(i);
        sum=sum+tmpInfo.getClosePrice();
        realDays++;
      }
      return sum/realDays;
    }
    
  }
  
}
