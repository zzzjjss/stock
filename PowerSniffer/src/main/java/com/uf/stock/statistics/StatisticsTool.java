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

import javassist.expr.NewArray;

import com.uf.stock.analysis.LowPriceUpStockFilterChain;
import com.uf.stock.analysis.TargetDefinition;
import com.uf.stock.analysis.filter.EXPMA_Filter;
import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.KLineTFilter;
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
	
	public void statisticBuyPointByEXPMA(){

      List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
      DateFormat format=new SimpleDateFormat("yyyyMMdd");
      float loseCount=0f,winCount=0f;
       for (StockInfo stockInfo : stocks) {
          List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAsc(stockInfo.getCode());
          Map<String, Integer> dataToIndexMap=new HashMap<String, Integer>();
          for(int i=0;i<allTradeInfos.size();i++){
            StockTradeInfo s=allTradeInfos.get(i);
            dataToIndexMap.put(format.format(s.getTradeDate()),i);
          }
          int start=allTradeInfos.size()-200;
          if (start<0) {
            start=0;
          }
          PriceFilter priceFilter=new PriceFilter(allTradeInfos, 20f);
          EXPMA_Filter  emaFilter=new EXPMA_Filter(allTradeInfos);
          for(;start<allTradeInfos.size();start++){
            StockTradeInfo tradeInfo=allTradeInfos.get(start);
//            FilterResult priceResult=priceFilter.doFilter(tradeInfo);
//            if (!priceResult.getIsPass()) {
//              continue;
//            }
            FilterResult result=emaFilter.doFilter(tradeInfo);
            if (result.getIsPass()) {
              int upDays=StockUtil.howmanyDaysToTargetUpPercent(allTradeInfos, start, 2f);
              int downDays=StockUtil.howmanyDaysToTargetDownPercent(allTradeInfos, start, 2f);
              if (downDays<=upDays) {
                loseCount++;
                System.out.println(tradeInfo.getStockSymbol()+"-->"+format.format(tradeInfo.getTradeDate())+"-->"+downDays+" to downPercent");
              }else {
                winCount++;
                System.out.println(tradeInfo.getStockSymbol()+"-->"+format.format(tradeInfo.getTradeDate())+"-->"+downDays+" to upPercent");
              }
            }
          }
        }
       System.out.println("total winRate:"+(winCount/(winCount+loseCount))*100);
      }
	
	public void statisticBuyPointByKline(String stockSymbol) {
		{
			TargetDefinition targetDefin = new TargetDefinition(5, 2f);
			StockInfo stock = service.findStockInfoByStockSymbol(stockSymbol);
			if (stock == null) {
				throw new IllegalArgumentException("stockSymbol is invalid");
			}

			List<StockTradeInfo> tradeInfos = service.findAllTradeInfosOrderByDateAsc(stock.getCode());
			
			if (tradeInfos != null) {
				LowPriceUpStockFilterChain lowPriceChain = new LowPriceUpStockFilterChain();
				lowPriceChain.appendStockFilter(new PriceFilter(tradeInfos, 50f));
				LowPriceUpStockFilterChain klineChain = new LowPriceUpStockFilterChain();
				klineChain.appendStockFilter(new KLineTFilter());
				int i = 0;
				if (tradeInfos.size() > 100) {
					i = tradeInfos.size() - 100;
				}
				List<Float> passTrade=new ArrayList<Float>();
				List<Float> noPassTrade=new ArrayList<Float>();
				for (; i < tradeInfos.size(); i++) {
					StockTradeInfo stockTradeInfo = tradeInfos.get(i);
					boolean isPass = lowPriceChain.doFilter(stockTradeInfo);
					if (isPass) {
						int days=StockUtil.howmanyDaysToTargetUpPercent(tradeInfos,i,targetDefin.getUpPercent());
						klineChain.doFilter(stockTradeInfo);
						Collection<Float> values=klineChain.getFilterChainResult().values();
						if(values!=null&&values.size()>0){
							if(days<=targetDefin.getDays()){
								passTrade.add((Float)(values.toArray()[0]));
							}else{
								noPassTrade.add((Float)(values.toArray()[0]));
							}
						}
						System.out.println("after days:" + days + " up to targetUpPercent");
					}
				}
				Collections.sort(passTrade);
				Collections.sort(noPassTrade);
				System.out.println(passTrade);
				System.out.println(noPassTrade);
			}
		}
	}
	
}
