package com.uf.stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;

import com.uf.stock.analysis.LowPriceUpPointStatistics;
import com.uf.stock.analysis.TargetDefinition;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.k_analysis.AnalysisResult;
import com.uf.stock.k_analysis.KLineAnalysisV2.DayBuySellPower;
import com.uf.stock.k_analysis.StockStageAnalysis;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.bean.StockStage;
import com.uf.stock.util.SpringBeanFactory;

public class AnalysisTest {

  @Test
  public void testKLineAnalysisV2(){
    DataSyncService dataService=SpringBeanFactory.getBean(DataSyncService.class);
    List<StockInfo> stocks=dataService.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
    Date date=new Date();
    List<Float> upPowers=new ArrayList<Float>();
    for(StockInfo stock:stocks){
      DayBuySellPower power=StockStageAnalysis.analyseStockBuySellPower(stock.getCode(), date,1);
      float upPower=power.getBuyPower()-power.getSellPower();
      upPowers.add(upPower);
      if (upPower>0.5) {
        System.out.println(stock.getName()+":"+power.getBuyPower()+":"+power.getSellPower());
      }
    }
    Collections.sort(upPowers);
    System.out.println(upPowers);
  }
  
  @Test
  public void testLowPriceUpPointStatistics() {
    DataSyncService dataService=SpringBeanFactory.getBean(DataSyncService.class);
    DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
    LowPriceUpPointStatistics analysis=new LowPriceUpPointStatistics(new TargetDefinition(7, 2.0f)); 
    List<StockInfo> stocks=dataService.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
    for(StockInfo stock:stocks){
      Float res=analysis.analyseAccuracy(stock.getSymbol());
      if (res!=null) {
        System.out.println("res:"+res);
      }
    }
    System.out.println(LowPriceUpPointStatistics.max);

//    SimpleDateFormat  formate=new SimpleDateFormat("yyyy-MM-dd");
//    Map<String, BuyPointStatisticsData> statistics=null;
//    try {
//      statistics = analysis.statisticBuypointData("sz002250",formate.parse("2016-5-20"),formate.parse("2016-7-20"));
//    } catch (ParseException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    int hit=0,nohit=0;
//    for(String key:statistics.keySet()){
//      BuyPointStatisticsData  data=statistics.get(key);
//      if(data.getUpPower()>0){
//        hit++;
//      }else{
//        nohit++;
//      }
//    }
//    System.out.println((float)hit/(float)(hit+nohit));
  }

  
  @Test
  public void testStockStageAnalyse() throws Exception {
    DataSyncService dataService=SpringBeanFactory.getBean(DataSyncService.class);
    List<StockInfo> stocks=dataService.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
    
    SimpleDateFormat  formate=new SimpleDateFormat("yyyy-MM-dd");
    //StockStageAnalysis.analyseStockSidewayIndex(603986, formate.parse("2017-01-5"));
    List<Float> indexs=new ArrayList<Float>();
    List<Float> slowUpFastDownindexs=new ArrayList<Float>();
    for(StockInfo stock:stocks){
      AnalysisResult result= StockStageAnalysis.periodAnalyseStock(stock.getCode(),formate.parse("2017-01-5"),30000);
      float sidewayIndex=result.calculateSidewayIndex();
      slowUpFastDownindexs.add(result.calculateSlowUpFastDownIndex());
      indexs.add(sidewayIndex);
      System.out.println(stock.getName()+" sidewayIndex:"+sidewayIndex);
      System.out.println(stock.getName()+result.toString());
    }
    Collections.sort(indexs);
    Collections.reverse(indexs);
    Collections.sort(slowUpFastDownindexs);
    System.out.println(indexs);
    System.out.println(slowUpFastDownindexs);
  }
}
