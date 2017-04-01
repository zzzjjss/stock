package com.uf.stock.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.util.StockUtil;

public class StatisticsUtil {
  public static void statisticToTarget(int targetDays,float targetUpPercent,List<StockTradeInfo> allTradeInfos,int tradeInfoIndex){
    float maxDown=0f;
    List<Float> downs=new ArrayList<Float>();
    SortedMap<Integer, Integer> statitc=new  TreeMap<Integer, Integer>();
    int upDays=StockUtil.howmanyDaysToTargetUpPercent(allTradeInfos, tradeInfoIndex, targetUpPercent);
    if (upDays>targetDays) {
      Float upDownPercent=StockUtil.updownPercentBetweenStartEnd(allTradeInfos, tradeInfoIndex+1, tradeInfoIndex+targetDays+1);
      downs.add(upDownPercent);
    }
    Integer tmp=statitc.get(upDays);
    if (tmp==null) {
      tmp=1;
    }else {
      tmp++;
    }
    statitc.put(upDays, tmp);
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
}
