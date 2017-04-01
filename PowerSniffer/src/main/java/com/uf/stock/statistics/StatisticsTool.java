package com.uf.stock.statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.uf.stock.analysis.LowPriceUpStockFilterChain;
import com.uf.stock.analysis.TargetDefinition;
import com.uf.stock.analysis.filter.EXPMA_Filter;
import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.KLineTFilter;
import com.uf.stock.analysis.filter.MorningStarFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;
import com.uf.stock.util.StockUtil;

public class StatisticsTool {
	private DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
	public void statisticBuyPointByLowestPrice(){
	  List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
	  DateFormat format=new SimpleDateFormat("yyyyMMdd");
	  Map<Integer, List<StockTradeInfo>> stockCodeToTradeInfos=new HashMap<Integer, List<StockTradeInfo>>();
	  Map<Integer, Map<String, Integer>> stockCodeToMap=new HashMap<Integer, Map<String, Integer>>();
	  for (StockInfo stockInfo : stocks) {
	    List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
	    stockCodeToTradeInfos.put(stockInfo.getCode(), allTradeInfos);
	    Map<String, Integer> dataToIndexMap=new HashMap<String, Integer>();
	    for(int i=0;i<allTradeInfos.size();i++){
	      StockTradeInfo s=allTradeInfos.get(i);
	      dataToIndexMap.put(format.format(s.getTradeDate()),i);
	    }
	    stockCodeToMap.put(stockInfo.getCode(), dataToIndexMap);
      }
	  
	  Set<String> allTradeDates=service.findAllTradeDate();
	  int today=Integer.parseInt(format.format(new Date()));
	  Calendar calendar=Calendar.getInstance();
	  calendar.setTime(new Date());
	  calendar.set(Calendar.HOUR_OF_DAY, 0);
	  calendar.set(Calendar.MINUTE, 0);
	  calendar.set(Calendar.SECOND, 0);
	  calendar.add(Calendar.DATE, 0-100);
	  while(Integer.parseInt(format.format(calendar.getTime()))<today){
	    Date tmpDate=calendar.getTime();
	    calendar.add(Calendar.DATE, 1);
	    if(!allTradeDates.contains(format.format(tmpDate))){
	      continue;
	    }
	    float lowestPercent=0f;
	    //StockTradeInfo lowestTradeInfo=null;
	    List<StockTradeInfo> lowestTradeInfos=null;
	    Map<String, Integer> lowestDataToIndexMap=null;
	    for (StockInfo stockInfo : stocks) {
	      Map<String, Integer> dataToIndexMap=stockCodeToMap.get(stockInfo.getCode());
	      List<StockTradeInfo> allTradeInfos=stockCodeToTradeInfos.get(stockInfo.getCode());
	        List<StockTradeInfo> tradeInfos=getLimitTradeInfosBeforeDate(dataToIndexMap,allTradeInfos,tmpDate,500);
	        if (tradeInfos!=null&&tradeInfos.size()>1) {
	          float percent=StockUtil.calculateDownPercentToLowest(tradeInfos, tradeInfos.get(tradeInfos.size()-1));
	          if (lowestPercent==0f||percent<lowestPercent) {
                lowestPercent=percent;
          //      lowestTradeInfo=tradeInfos.get(tradeInfos.size()-1);
                lowestTradeInfos=allTradeInfos;
                lowestDataToIndexMap=dataToIndexMap;
              }
            }
	    }
	    if (lowestTradeInfos!=null) {
	      int days=StockUtil.howmanyDaysToTargetUpPercent(getLimitTradeInfosAfterDate(lowestDataToIndexMap,lowestTradeInfos,tmpDate,500), 0, 2f);
	      System.out.println(lowestTradeInfos.get(0).getStockSymbol()+"-->"+format.format(tmpDate)+"-->"+lowestPercent+"-->"+days);
        }
	  }
	  
	  for (StockInfo stockInfo : stocks) {
	    List<StockTradeInfo> tradeInfos=service.findDateAscTradeInfosBeforeDate(stockInfo.getCode(), new Date(), 500);
	    for (int i = 150; i >0;i--) {
          int index=500-i;
          if (index>=tradeInfos.size()) {
            break;
          }
          
        }
        
      }
	}
	
	private List<StockTradeInfo> getLimitTradeInfosBeforeDate(Map<String, Integer> dateToIndex,List<StockTradeInfo>  allTradeInfos,Date date,int limit){
	  List<StockTradeInfo> result=new LinkedList<StockTradeInfo>();
	  DateFormat formate=new SimpleDateFormat("yyyyMMdd");
	  String dateString=formate.format(date);
	  Integer index=dateToIndex.get(dateString);
	  if (index!=null) {
	    int start=index-limit;
	    if (start<0) {
          start=0;
        }
        for(;start<=index;start++){
          result.add(allTradeInfos.get(start));
        }
      }
	  return result;
	}
	
	private List<StockTradeInfo> getLimitTradeInfosAfterDate(Map<String, Integer> dateToIndex,List<StockTradeInfo>  allTradeInfos,Date date,int limit){
      List<StockTradeInfo> result=new LinkedList<StockTradeInfo>();
      DateFormat formate=new SimpleDateFormat("yyyyMMdd");
      String dateString=formate.format(date);
      Integer index=dateToIndex.get(dateString);
      if (index!=null) {
        
        for(;index<=limit+index;index++){
          if (index<allTradeInfos.size()) {
            result.add(allTradeInfos.get(index));
          }
        }
      }
      return result;
    }
	public void statisticBuyPointByTKline(){

      List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
      float maxDown=0f;
      List<Float> downs=new ArrayList<Float>();
      DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
      SortedMap<Integer, Integer> statitc=new  TreeMap<Integer, Integer>();
      int targetDays=10;
       for (StockInfo stockInfo : stocks) {
         if (stockInfo.getName().contains("ST")) {
          continue;
         }
          List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
          Map<String, Integer> dateToIndexMap=new HashMap<String,Integer>();
          for (int index=0;index<allTradeInfos.size();index++) {
            dateToIndexMap.put(format.format(allTradeInfos.get(index).getTradeDate()) ,index);
          }
          int start=allTradeInfos.size()-200;
          if (start<0) {
            start=0;
          }
          int priceFilterStart=allTradeInfos.size()-300;
          if (priceFilterStart<0) {
            priceFilterStart=0;
          }
          PriceFilter priceFilter=new PriceFilter(allTradeInfos.subList(priceFilterStart,allTradeInfos.size()-1), 50f);
          KLineTFilter tFilter=new KLineTFilter(0.3f);
          for(;start<allTradeInfos.size();start++){
            StockTradeInfo tradeInfo=allTradeInfos.get(start);
            FilterResult priceResult=priceFilter.doFilter(tradeInfo);
            if (!priceResult.getIsPass()) {
              continue;
            }
//            float ma5=StockUtil.calculateMa(allTradeInfos, dateToIndexMap, tradeInfo, 5);
//            float ma10=StockUtil.calculateMa(allTradeInfos, dateToIndexMap, tradeInfo, 10);
//            float ma20=StockUtil.calculateMa(allTradeInfos, dateToIndexMap, tradeInfo, 20);
            if (start-1>=0) {
              StockTradeInfo info=allTradeInfos.get(start-1);
              if (info.getUpDownRate()>0) {
                continue;
              }
            }
            FilterResult result=tFilter.doFilter(tradeInfo);
            if (result.getIsPass()) {
              int upDays=StockUtil.howmanyDaysToTargetUpPercent(allTradeInfos, start, 2f);
              if (upDays>targetDays) {
                Float upDownPercent=StockUtil.updownPercentBetweenStartEnd(allTradeInfos, start+1, start+targetDays+1);
                downs.add(upDownPercent);
                if (upDownPercent<maxDown) {
                  maxDown=upDownPercent;
                }
                if (upDownPercent!=null&&upDownPercent<-7f) {
                  System.out.println(tradeInfo.getStockSymbol()+":"+format.format(tradeInfo.getTradeDate())+" updown:--->"+upDownPercent);
                }
              }
              Integer tmp=statitc.get(upDays);
              if (tmp==null) {
                tmp=1;
              }else {
                tmp++;
              }
              statitc.put(upDays, tmp);
            }
          }
        }
       System.out.println(statitc);
       int win=0,lose=0;
       for (Map.Entry<Integer, Integer> sta : statitc.entrySet()) {
         Integer key=sta.getKey();
         if (key<=targetDays) {
           win=win+sta.getValue();
        }else {
          lose=lose+sta.getValue();
        }
      }
       System.out.println("win:"+win+" lose:"+lose);
       float rate=((float)(win)/(float)(win+lose))*100;
       System.out.println("winRate:"+rate);
       float accept=(rate*2f)/(100-rate);
       System.out.println("accept downPercent:"+accept+"%");
       System.out.println("maxDownPercent:"+maxDown);
       float sum=0f;
       for(Float updown:downs){
         sum=sum+updown;
       }
       System.out.println("avgDownPercent:"+(sum/downs.size())+"%");
      }	
	
	public void statisticBuyPointByMorningStar(){

      List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
      float maxDown=0f;
      List<Float> downs=new ArrayList<Float>();
      DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
      SortedMap<Integer, Integer> statitc=new  TreeMap<Integer, Integer>();
      int targetDays=5;
       for (StockInfo stockInfo : stocks) {
         if (stockInfo.getName().contains("ST")) {
          continue;
         }
          List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
          Map<String, Integer> dateToIndexMap=new HashMap<String,Integer>();
          for (int index=0;index<allTradeInfos.size();index++) {
            dateToIndexMap.put(format.format(allTradeInfos.get(index).getTradeDate()) ,index);
          }
          int start=allTradeInfos.size()-300;
          if (start<0) {
            start=0;
          }
          int priceFilterStart=allTradeInfos.size()-300;
          if (priceFilterStart<0) {
            priceFilterStart=0;
          }
          PriceFilter priceFilter=new PriceFilter(allTradeInfos.subList(priceFilterStart,allTradeInfos.size()-1), 50f);
          MorningStarFilter morningStartFilter=new MorningStarFilter();
          for(;start<allTradeInfos.size();start++){
            StockTradeInfo tradeInfo=allTradeInfos.get(start);
            int before=start-1,beforeBefore=start-2;
           if (beforeBefore<0){
             continue;
           }
           StockTradeInfo  beforeTradeInfo=allTradeInfos.get(before);
           StockTradeInfo beforeBeforeTradeInfo=allTradeInfos.get(beforeBefore);
            FilterResult priceResult=priceFilter.doFilter(tradeInfo);
            if (!priceResult.getIsPass()) {
              continue;
            }
            FilterResult result=morningStartFilter.doFilter(beforeBeforeTradeInfo,beforeTradeInfo,tradeInfo);
            if (result.getIsPass()) {
              int upDays=StockUtil.howmanyDaysToTargetUpPercent(allTradeInfos, start, 2f);
              if (upDays>targetDays) {
                Float upDownPercent=StockUtil.updownPercentBetweenStartEnd(allTradeInfos, start+1, start+targetDays+1);
                downs.add(upDownPercent);
                if (upDownPercent<maxDown) {
                  maxDown=upDownPercent;
                }
                if (upDownPercent!=null&&upDownPercent<-7f) {
                  System.out.println(tradeInfo.getStockSymbol()+":"+format.format(tradeInfo.getTradeDate())+" updown:--->"+upDownPercent);
                }
              }
              Integer tmp=statitc.get(upDays);
              if (tmp==null) {
                tmp=1;
              }else {
                tmp++;
              }
              statitc.put(upDays, tmp);
            }
          }
        }
       System.out.println(statitc);
       int win=0,lose=0;
       for (Map.Entry<Integer, Integer> sta : statitc.entrySet()) {
         Integer key=sta.getKey();
         if (key<=targetDays) {
           win=win+sta.getValue();
        }else {
          lose=lose+sta.getValue();
        }
      }
       System.out.println("win:"+win+" lose:"+lose);
       float rate=((float)(win)/(float)(win+lose))*100;
       System.out.println("winRate:"+rate);
       float accept=(rate*2f)/(100-rate);
       System.out.println("accept downPercent:"+accept+"%");
       System.out.println("maxDownPercent:"+maxDown);
       float sum=0f;
       for(Float updown:downs){
         sum=sum+updown;
       }
       System.out.println("avgDownPercent:"+(sum/downs.size())+"%");
      }
	
	public void statisticByTurnoverRate(){
      List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
      float maxDown=0f;
      List<Float> downs=new ArrayList<Float>();
      DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
      SortedMap<Integer, Integer> statitc=new  TreeMap<Integer, Integer>();
      int targetDays=5;
       for (StockInfo stockInfo : stocks) {
         if (stockInfo.getName().contains("ST")) {
          continue;
         }
          List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
          Map<String, Integer> dateToIndexMap=new HashMap<String,Integer>();
          for (int index=0;index<allTradeInfos.size();index++) {
            dateToIndexMap.put(format.format(allTradeInfos.get(index).getTradeDate()) ,index);
          }
          int start=allTradeInfos.size()-300;
          if (start<0) {
            start=0;
          }
          int priceFilterStart=allTradeInfos.size()-300;
          if (priceFilterStart<0) {
            priceFilterStart=0;
          }
          PriceFilter priceFilter=new PriceFilter(allTradeInfos.subList(priceFilterStart,allTradeInfos.size()-1), 20f);
          for(;start<allTradeInfos.size();start++){
            StockTradeInfo tradeInfo=allTradeInfos.get(start);
            FilterResult priceResult=priceFilter.doFilter(tradeInfo);
            if (!priceResult.getIsPass()) {
              continue;
            }
            
            if (tradeInfo.getTurnoverRate()!=null&&tradeInfo.getTurnoverRate()>10f){
              int upDays=StockUtil.howmanyDaysToTargetUpPercent(allTradeInfos, start, 2f);
              if (upDays>targetDays) {
                Float upDownPercent=StockUtil.updownPercentBetweenStartEnd(allTradeInfos, start+1, start+targetDays+1);
                downs.add(upDownPercent);
                if (upDownPercent<maxDown) {
                  maxDown=upDownPercent;
                }
                if (upDownPercent!=null&&upDownPercent<-7f) {
                  System.out.println(tradeInfo.getStockSymbol()+":"+format.format(tradeInfo.getTradeDate())+" updown:--->"+upDownPercent);
                }
              }
              Integer tmp=statitc.get(upDays);
              if (tmp==null) {
                tmp=1;
              }else {
                tmp++;
              }
              statitc.put(upDays, tmp);
            }
          }
        }
       System.out.println(statitc);
       int win=0,lose=0;
       for (Map.Entry<Integer, Integer> sta : statitc.entrySet()) {
         Integer key=sta.getKey();
         if (key<=targetDays) {
           win=win+sta.getValue();
        }else {
          lose=lose+sta.getValue();
        }
      }
       System.out.println("win:"+win+" lose:"+lose);
       float rate=((float)(win)/(float)(win+lose))*100;
       System.out.println("winRate:"+rate);
       float accept=(rate*2f)/(100-rate);
       System.out.println("accept downPercent:"+accept+"%");
       System.out.println("maxDownPercent:"+maxDown);
       float sum=0f;
       for(Float updown:downs){
         sum=sum+updown;
       }
       System.out.println("avgDownPercent:"+(sum/downs.size())+"%");
      
	}
	
	public void statisticBuyPointByEXPMA(){
      List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
      DateFormat format=new SimpleDateFormat("yyyyMMdd");
      int loseCount=0,winCount=0;
       for (StockInfo stockInfo : stocks) {
          List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
          int start=allTradeInfos.size()-200;
          if (start<0) {
            start=0;
          }
          PriceFilter priceFilter=new PriceFilter(allTradeInfos, 20f);
          EXPMA_Filter  emaFilter=new EXPMA_Filter(allTradeInfos);
          int win=0,lose=0;
          for(;start<allTradeInfos.size();start++){
            StockTradeInfo tradeInfo=allTradeInfos.get(start);
//            FilterResult priceResult=priceFilter.doFilter(tradeInfo);
//            if (!priceResult.getIsPass()) {
//              continue;
//            }
            FilterResult result=emaFilter.doFilter(tradeInfo);
            if (result.getIsPass()) {
              int upDays=StockUtil.howmanyDaysToTargetUpPercent(allTradeInfos, start, 2f);
                if (upDays<=10) {
                  winCount++;
                }else {
                  loseCount++;
                }
//              int downDays=StockUtil.howmanyDaysToTargetDownPercent(allTradeInfos, start, 2f);
//              if (downDays<=upDays) {
//                loseCount++;
//                System.out.println(tradeInfo.getStockSymbol()+"-->"+format.format(tradeInfo.getTradeDate())+"-->"+downDays+" to downPercent");
//              }else {
//                winCount++;
//                System.out.println(tradeInfo.getStockSymbol()+"-->"+format.format(tradeInfo.getTradeDate())+"-->"+downDays+" to upPercent");
//              }
            }
          }
        }
       if ((winCount+loseCount)!=0) {
         System.out.println("win:"+winCount+"lose:"+loseCount);
         System.out.println("total winRate:"+((float)winCount/(float)(winCount+loseCount))*100);
      }
      }
	
	}
	
