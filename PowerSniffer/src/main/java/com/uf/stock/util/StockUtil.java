package com.uf.stock.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.bean.KLine;
import com.uf.stock.data.bean.StockTradeInfo;

public class StockUtil {
public static Integer parseStockSymbolToStockCode(String stockSymbol){
  return Integer.parseInt(stockSymbol.substring(2));
}
public static boolean isOpenTime(Date date){
  List<String> openDaysOfWeek=Arrays.asList("Mon","Tue","Wed","Thu","Fri");
  SimpleDateFormat  formate=new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat  dayOfWeekFormate=new SimpleDateFormat("E");
  SimpleDateFormat  hourseFormate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  try {
    long nowTime=date.getTime();
    String day=formate.format(date);
    String dayOfWeek=dayOfWeekFormate.format(date);
    if(!openDaysOfWeek.contains(dayOfWeek)){
      return false;
    }
    Date begin1=hourseFormate.parse(day+" 09:30:00");
    Date end1=hourseFormate.parse(day+" 11:30:00");
    
    Date begin2=hourseFormate.parse(day+" 13:00:00");
    Date end2=hourseFormate.parse(day+" 15:00:00");
    if((nowTime>=begin1.getTime()&&nowTime<=end1.getTime())||(nowTime>=begin2.getTime()&&nowTime<=end2.getTime())){
      return true;
    }
  } catch (ParseException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  return false;
}

public static float calculateDownPercentToLowest(List<StockTradeInfo> orderedTradeInfos,StockTradeInfo tradeInfo){
  float lowestDownPercent=0f,upDownPercent=0f;
  for (int i=0;i<orderedTradeInfos.size();i++) {
      if (i==0) {
        continue;
      }
      StockTradeInfo   stockTradeInfo=orderedTradeInfos.get(i);
      float lowestUpDownPercent=(float)(((stockTradeInfo.getLowestPrice()*(1+stockTradeInfo.getUpDownRate()*0.01))/stockTradeInfo.getClosePrice())-1)*100;
      float tmpLowestDownPercent=upDownPercent+lowestUpDownPercent;
      upDownPercent=upDownPercent+stockTradeInfo.getUpDownRate();
      if (lowestDownPercent>tmpLowestDownPercent) {
        lowestDownPercent=tmpLowestDownPercent;
      }
      if (stockTradeInfo.getTradeDate().getTime()>=tradeInfo.getTradeDate().getTime()) {
        break;
      }
  }
  
  return upDownPercent-lowestDownPercent;

}

public static float calculateMa(List<StockTradeInfo> tradeInfos,Map<String, Integer> dateToIndex,StockTradeInfo tradeInfo,int periodDays){
  SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
  Integer index=dateToIndex.get(formate.format(tradeInfo.getTradeDate()));
  if (index==null) {
    return -1f;
  }
  StockTradeInfo info=tradeInfos.get(index);
  if (info==null) {
    return -1f;
  }else {
    int start=(index-periodDays)+1;
    if (start<0) {
      start=0;
    }
    float sum=0f;
    int realDays=0;
    for(int i=start;i<=index;i++){
      StockTradeInfo tmpInfo=tradeInfos.get(i);
      sum=sum+tmpInfo.getClosePrice();
      realDays++;
    }
    return sum/realDays;
  }
}

public static int howmanyDaysToTargetUpPercent(List<StockTradeInfo> tradeInfos,int tradeInfoIndex,float targetUpPercent){
	int index=tradeInfoIndex;
	int days = -1;
	if (index < tradeInfos.size() - 1) {
		float upPercent = 0f;
		for (int tmp = index + 1; tmp < tradeInfos.size(); tmp++) {
			StockTradeInfo tmpTradeInfo = tradeInfos.get(tmp);
			float highestUpPercent=(float)(((tmpTradeInfo.getHighestPrice()*(1+tmpTradeInfo.getUpDownRate()*0.01))/tmpTradeInfo.getClosePrice())-1)*100;
			float maxUp=upPercent+highestUpPercent;
			upPercent = upPercent + tmpTradeInfo.getUpDownRate();
			days++;
			if (maxUp >= targetUpPercent) {
				break;
			}
		}
	}
	return days;
}
public static int howmanyDaysToTargetDownPercent(List<StockTradeInfo> tradeInfos,int tradeInfoIndex,float targetDownPercent){
  int index=tradeInfoIndex;
  int days = -1;
  if (index < tradeInfos.size() - 1) {
      float downPercent = 0f;
      for (int tmp = index + 1; tmp < tradeInfos.size(); tmp++) {
          StockTradeInfo tmpTradeInfo = tradeInfos.get(tmp);
          float lowestUpDownPercent=(float)(((tmpTradeInfo.getLowestPrice()*(1+tmpTradeInfo.getUpDownRate()*0.01))/tmpTradeInfo.getClosePrice())-1)*100;
          float maxDown=downPercent+lowestUpDownPercent;
          downPercent = downPercent + tmpTradeInfo.getUpDownRate();
          days++;
          if (maxDown <= (0-targetDownPercent)) {
              break;
          }
      }
  }
  return days;
}
public static Float updownPercentBetweenStartEnd(List<StockTradeInfo> allTradeInfos ,int startIndex,int endIndex){
  if (startIndex<0||endIndex>=allTradeInfos.size()||startIndex>endIndex) {
    return null;
  }
  float result=0f;
  for(;startIndex<=endIndex;startIndex++){
    StockTradeInfo info=allTradeInfos.get(startIndex);
    result=result+info.getUpDownRate();
  }
  return result;
}


public static  KLine calculateStandardKline(StockTradeInfo tradeInfo){
  KLine k=new KLine();
  float preDayClosePrice=0f;
  if (tradeInfo.getClosePrice()!=null&&tradeInfo.getUpDownRate()!=null&&(1+(tradeInfo.getUpDownRate()*0.01f))!=0f) {
    preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
  }
  float length=(preDayClosePrice*1.1f)-(preDayClosePrice*0.9f);
  float lowerShadowLength,upperShadowLength,realLength;
  if (tradeInfo.getOpenPrice()<tradeInfo.getClosePrice()) {
    upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getClosePrice();
    lowerShadowLength=tradeInfo.getOpenPrice()-tradeInfo.getLowestPrice();
    realLength=tradeInfo.getClosePrice()-tradeInfo.getOpenPrice();
  }else{
    lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
    upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getOpenPrice();
    realLength=tradeInfo.getOpenPrice()-tradeInfo.getClosePrice();
  }
  k.setLowerShadowLength(lowerShadowLength);
  k.setRealLength(realLength);
  k.setUpperShadowLength(upperShadowLength);
  k.setTotalLength(length);
  return k;
}
}
