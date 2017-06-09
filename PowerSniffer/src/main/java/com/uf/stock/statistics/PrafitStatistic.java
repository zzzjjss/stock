package com.uf.stock.statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;
import com.uf.stock.util.StockUtil;

public class PrafitStatistic {
  private static DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);

  public static void statisticOnedayTrade() {
    List<StockInfo> stocks = service.findStocksPeRatioBetween(-1f, 100000f);
    float maxDown = 0f;
    List<Float> downs = new ArrayList<Float>();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SortedMap<Integer, Integer> statitc = new TreeMap<Integer, Integer>();
    int targetDays = 0;
    float totalPrafit=0f,totalMoney=50000f,totalPrafitPercent=0f;
    for (StockInfo stockInfo : stocks) {
      if (stockInfo.getName().contains("ST")) {
        continue;
      }
      List<StockTradeInfo> allTradeInfos = service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
      if (allTradeInfos.size() == 0) {
        continue;
      }
      Map<String, Integer> dateToIndexMap = new HashMap<String, Integer>();
      for (int index = 0; index < allTradeInfos.size(); index++) {
        dateToIndexMap.put(format.format(allTradeInfos.get(index).getTradeDate()), index);
      }
      int start = allTradeInfos.size() - 300;
      if (start < 0) {
        start = 0;
      }
      int priceFilterStart = allTradeInfos.size() - 500;
      if (priceFilterStart < 0) {
        priceFilterStart = 0;
      }
      PriceFilter priceFilter = new PriceFilter(allTradeInfos.subList(priceFilterStart, allTradeInfos.size() - 1), 50f);
//      KLineTFilter tFilter = new KLineTFilter(0.6f);
      for (; start < allTradeInfos.size(); start++) {
        StockTradeInfo tradeInfo = allTradeInfos.get(start);
//        float highestUpDownPercent=(float)(((tradeInfo.getHighestPrice()*(1+tradeInfo.getUpDownRate()*0.01))/tradeInfo.getClosePrice())-1)*100;
//        if (!(tradeInfo.getHighestPrice()>tradeInfo.getClosePrice()&&highestUpDownPercent>=9.8)) {
//         continue; 
//        }
//        if (tradeInfo.getUpDownRate()>=9.8) {
//          continue;
//        }
        if (tradeInfo.getUpDownRate()<=9.8) {
         continue; 
        }
        FilterResult priceResult = priceFilter.doFilter(tradeInfo);
        if (!priceResult.getIsPass()) {
          continue;
        }
        if (start+1>=allTradeInfos.size()) {
         break; 
        }
//        FilterResult result = tFilter.doFilter(tradeInfo);
//        if (result.getIsPass()) {
          StockTradeInfo tradeDayInfo=allTradeInfos.get(start+1);
          float targetDownPercent=-5f,targetUpPercent=6f;
//          float winPercent=StockUtil.calculateWinPercentOnedayTrade(tradeDayInfo, targetUpPercent, targetDownPercent);
//          float winPercent=StockUtil.calculatePrafit(allTradeInfos, start, targetUpPercent, targetDownPercent);
          float winPercent=StockUtil.calculatePrafitAfterFullUp(allTradeInfos, start, targetUpPercent, targetDownPercent);
          System.out.println(winPercent);
          totalPrafitPercent=totalPrafitPercent+winPercent;
          float prafit=(winPercent/100)*totalMoney;
          totalPrafit=totalPrafit+prafit;
//        }
      }
    }
    System.out.println("totalPrafit:"+totalPrafit+"---> totalPrafit percent:"+totalPrafitPercent+"%");
  }
  //跌停打开，止损止盈
  public static void fullDownOpenedStatistic() {
    List<StockInfo> stocks = service.findStocksPeRatioBetween(-1f, 100000f);
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, List<Float>> tradeDayMap=new HashMap<String,List<Float>>(); 
    float totalPrafit=0f,totalMoney=50000f,totalPrafitPercent=0f;
    int totalInvestTime=0;
    for (StockInfo stockInfo : stocks) {
      if (stockInfo.getName().contains("ST")) {
        continue;
      }
      List<StockTradeInfo> allTradeInfos = service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
      if (allTradeInfos.size() == 0) {
        continue;
      }
      Map<String, Integer> dateToIndexMap = new HashMap<String, Integer>();
      for (int index = 0; index < allTradeInfos.size(); index++) {
        dateToIndexMap.put(format.format(allTradeInfos.get(index).getTradeDate()), index);
      }
      int start = allTradeInfos.size() - 300;
      if (start < 0) {
        start = 0;
      }
      int priceFilterStart = allTradeInfos.size() - 500;
      if (priceFilterStart < 0) {
        priceFilterStart = 0;
      }
      PriceFilter priceFilter = new PriceFilter(allTradeInfos.subList(priceFilterStart, allTradeInfos.size() - 1), 50f);
      for (; start < allTradeInfos.size(); start++) {
        StockTradeInfo tradeInfo = allTradeInfos.get(start);
        String tradeDate=format.format(tradeInfo.getTradeDate());
        float lowestUpDownPercent=(float)(((tradeInfo.getLowestPrice()*(1+tradeInfo.getUpDownRate()*0.01))/tradeInfo.getClosePrice())-1)*100;
        float highestUpDownPercent=(float)(((tradeInfo.getHighestPrice()*(1+tradeInfo.getUpDownRate()*0.01))/tradeInfo.getClosePrice())-1)*100;
        if (!(lowestUpDownPercent<=-9.8&&tradeInfo.getUpDownRate()>-9.8)) {
         continue; 
        }
        FilterResult priceResult = priceFilter.doFilter(tradeInfo);
        if (!priceResult.getIsPass()) {
          continue;
        }
        if (start+1>=allTradeInfos.size()) {
         break; 
        }
          float targetDownPercent=-5f,targetUpPercent=20f;
          totalInvestTime++;
          float winPercent=StockUtil.calculatePrafit(allTradeInfos, start, targetUpPercent, targetDownPercent);
          List<Float> prafitList=tradeDayMap.get(tradeDate);
          if(prafitList==null) {
           prafitList=new ArrayList<Float>(); 
           tradeDayMap.put(tradeDate, prafitList);
          }
          prafitList.add(winPercent);
          System.out.println(tradeInfo.getTradeDate()+":"+tradeInfo.getStockSymbol()+":"+winPercent);
          totalPrafitPercent=totalPrafitPercent+winPercent;
          float prafit=(winPercent/100)*totalMoney;
          totalPrafit=totalPrafit+prafit;
//        }
      }
    }
    System.out.println("totalPrafit:"+totalPrafit+"---> totalPrafit percent:"+totalPrafitPercent+"%"+"  totalInveset time:"+totalInvestTime);
    Random random=new Random();
    float total=0f;
    for(Map.Entry<String, List<Float>> entry:tradeDayMap.entrySet()){
      System.out.println(entry.getKey()+":"+entry.getValue().size());
      if(entry.getValue().size()<=5){
       for(Float prafit:entry.getValue()){
         total=total+prafit;
       }
      }else{
        Set<Integer> indexs=new HashSet<Integer>();
        List<Float> prafits=entry.getValue();
        while(indexs.size()<=5){
          int index=random.nextInt(prafits.size());   
          indexs.add(index);
        }
        for(Integer index:indexs){
          total=total+prafits.get(index);
        }
      }
    }
   System.out.println("random select prafit:"+total); 
  }
}
