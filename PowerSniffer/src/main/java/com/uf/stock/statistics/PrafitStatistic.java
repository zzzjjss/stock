package com.uf.stock.statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.KLineTFilter;
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
      KLineTFilter tFilter = new KLineTFilter(0.6f);
      for (; start < allTradeInfos.size(); start++) {
        StockTradeInfo tradeInfo = allTradeInfos.get(start);
        if (tradeInfo.getUpDownRate()>=9.8) {
          continue;
        }
        FilterResult priceResult = priceFilter.doFilter(tradeInfo);
        if (!priceResult.getIsPass()) {
          continue;
        }
        if (start+1>=allTradeInfos.size()) {
         break; 
        }
        FilterResult result = tFilter.doFilter(tradeInfo);
        if (result.getIsPass()) {
          StockTradeInfo tradeDayInfo=allTradeInfos.get(start+1);
          float targetDownPercent=-5f,targetUpPercent=6f;
//          float winPercent=StockUtil.calculateWinPercentOnedayTrade(tradeDayInfo, targetUpPercent, targetDownPercent);
          float winPercent=StockUtil.calculatePrafit(allTradeInfos, start, targetUpPercent, targetDownPercent);
          System.out.println(winPercent);
          totalPrafitPercent=totalPrafitPercent+winPercent;
          float prafit=(winPercent/100)*totalMoney;
          totalPrafit=totalPrafit+prafit;
        }
      }
    }
    System.out.println("totalPrafit:"+totalPrafit+"---> totalPrafit percent:"+totalPrafitPercent+"%");
  }
}
