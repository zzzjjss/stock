package com.uf.stock.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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


public static int howmanyDaysToTargetUpPercent(List<StockTradeInfo> tradeInfos,int tradeInfoIndex,float targetUpPercent){
	int index=tradeInfoIndex;
	int days = -1;
	if (index < tradeInfos.size() - 1) {
		float upPercent = 0f;
		for (int tmp = index + 1; tmp < tradeInfos.size(); tmp++) {
			StockTradeInfo tmpTradeInfo = tradeInfos.get(tmp);
			float highestUpPercent=(float)(((tmpTradeInfo.getHighestPrice()*(1+tmpTradeInfo.getUpDownRate()*0.01))/tmpTradeInfo.getClosePrice())-1)*100;
			upPercent = upPercent + tmpTradeInfo.getUpDownRate();
			float maxUp=upPercent+highestUpPercent;
			days++;
			if (maxUp >= targetUpPercent) {
				break;
			}
		}
	}
	return days;
}
}
