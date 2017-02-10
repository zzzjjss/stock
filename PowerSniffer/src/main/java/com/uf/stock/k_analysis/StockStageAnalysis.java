package com.uf.stock.k_analysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.k_analysis.KLineAnalysisV2.DayBuySellPower;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class StockStageAnalysis {
  
  public static DayBuySellPower analyseStockBuySellPower(Integer stockCode, Date date, int periodDays) {
    SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors = service.findLimitTradeInfosBeforeDate(stockCode, date, periodDays);
    float upPower = 0.0f, downPower = 0.0f;
    if (infors != null) {
      for (StockTradeInfo info : infors) {
        DayBuySellPower state = KLineAnalysisV2.calculateBuySellPower(info);
        if (state!=null) {
          upPower = upPower + state.getBuyPower();
          downPower = downPower + state.getSellPower();
        }else {
          System.out.println("can't get kline state "+info.getStockSymbol()+" : "+formate.format(info.getTradeDate()));
        }
      }
    }
    DayBuySellPower power = new DayBuySellPower();
    power.setBuyPower(upPower);
    power.setSellPower(downPower);
    return power;
  }
  
  
  public static StockStage analyseStockStageByKLine(Integer stockCode, Date date, int periodDays) {
    SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors = service.findLimitTradeInfosBeforeDate(stockCode, date, periodDays);
    float upPower = 0.0f, downPower = 0.0f;
    if (infors != null) {
      for (StockTradeInfo info : infors) {
        KLineState state = KLineAnalysis.analyseKLineState(info.getOpenPrice(), info.getClosePrice(), info.getHighestPrice(), info.getLowestPrice());
        if (state!=null) {
          upPower = upPower + state.getUpPower();
          downPower = downPower + state.getDownPower();
        }else {
          System.out.println("can't get kline state "+info.getStockSymbol()+" : "+formate.format(info.getTradeDate()));
        }
      }
    }
    StockStage stage = new StockStage();
    stage.setDownPower(downPower);
    stage.setUpPower(upPower);
//    if ((downPower + upPower) != 0) {
//      stage.setDownPower(downPower / (downPower + upPower));
//      stage.setUpPower(upPower / (downPower + upPower));
//    } else {
//      stage.setDownPower(0f);
//      stage.setUpPower(0f);
//    }
    return stage;
  }
  
  
  
  public static AnalysisResult periodAnalyseStock(Integer stockCode,Date date,int period){
    AnalysisResult result=new AnalysisResult();
    DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors=service.findTradeInfosBeforeDate(stockCode, date, period);
    List<Float[]> continueUpRate=new ArrayList<Float[]>();
    List<Float[]> continueDownRate=new ArrayList<Float[]>();
    List<Float> tmpUp=new ArrayList<Float>();
    List<Float> tmpDown=new ArrayList<Float>();
    Boolean isUp=null;
    float periodUpDownRate=0f,periodMaxUpRate=0f,periodMaxDownRate=0f;
    
    SimpleDateFormat  formate=new SimpleDateFormat("yyyy-MM-dd");
    int index=0;
    for (StockTradeInfo stockTradeInfo:infors) {
      Float upDownRate=stockTradeInfo.getUpDownRate();
      index++;
      if (index==1) {
        continue;
      }
      periodUpDownRate=periodUpDownRate+upDownRate;
      if(periodUpDownRate>periodMaxUpRate){
        periodMaxUpRate=periodUpDownRate;
      }
      if (periodUpDownRate<periodMaxDownRate) {
        periodMaxDownRate=periodUpDownRate;
      }
      //System.out.println(formate.format(stockTradeInfo.getTradeDate())+":"+closePrice);
      if (upDownRate>0) {
         tmpUp.add(upDownRate);
        if(isUp!=null&&!isUp){
          continueDownRate.add(tmpDown.toArray(new Float[tmpDown.size()]));
          tmpDown.clear();
        }
        isUp=true;
      }else if (upDownRate<0) {
        tmpDown.add(upDownRate);
        if(isUp!=null&&isUp){
          continueUpRate.add(tmpUp.toArray(new Float[tmpUp.size()]));
          tmpUp.clear();
        }
        isUp=false;
      }
      if (index==infors.size()) {
        if(isUp){
          continueUpRate.add(tmpUp.toArray(new Float[tmpUp.size()]));
          tmpUp.clear();
        }else{
          continueDownRate.add(tmpDown.toArray(new Float[tmpDown.size()]));
          tmpDown.clear();
        }
      }
    }
    List<Float> upKs=new ArrayList<Float>();
    List<Float> downKs=new ArrayList<Float>();
    for (Float[] floats:continueUpRate){
      float sum=0f;
      for(Float rate:floats){
        sum=sum+rate;
      }
      int size=floats.length;
      float  k=(float)(sum/size);
      upKs.add(k);
     // System.out.println("up:"+Arrays.asList(floats)+" K:"+k);
    }
    
    for (Float[] floats:continueDownRate) {
      float sum=0f;
      for(Float rate:floats){
        sum=sum+rate;
      }
      int size=floats.length;
      float  k=(float)(sum/size);
      downKs.add(k);
      //System.out.println("down"+Arrays.asList(floats)+" K:"+k);
    }
    
    Float maxUpKs=upKs.size()>0?Collections.max(upKs):0f;
    Float maxDownKs=downKs.size()>0?Collections.min(downKs):0f;
    Float upKsSum=0f,downKsSum=0f;
    for (Float k:upKs) {
      upKsSum=upKsSum+k;
    }
    float avgUpKs=0.0f,avgDownKs=0.0f;
    if (upKsSum!=0f) {
      avgUpKs=(float)(upKsSum/upKs.size());
    }
    for(Float k:downKs){
      downKsSum=downKsSum+k;
    }
    if (downKsSum!=0.0f) {
      avgDownKs=(float)(downKsSum/downKs.size());
    }
    result.setMaxDownSpeed(Math.abs(maxDownKs));
    result.setMaxUpSpeed(Math.abs(maxUpKs));
    result.setAvgUpSpeed(Math.abs(avgUpKs));
    result.setAvgDownSpeed(Math.abs(avgDownKs));
    result.setUpDownRate(periodUpDownRate);
    result.setMaxUpRate(periodMaxUpRate);
    result.setMaxDownRate(periodMaxDownRate);
    result.setDownRateToLowest(periodUpDownRate-periodMaxDownRate);
    result.setStockCode(stockCode);
    return result;
  }
}
