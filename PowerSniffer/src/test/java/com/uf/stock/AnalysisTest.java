package com.uf.stock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.junit.Test;

import com.uf.stock.analysis.BuyPointStatisticsData;
import com.uf.stock.analysis.LowPriceUpPointStatistics;
import com.uf.stock.analysis.TargetDefinition;

public class AnalysisTest {

  @Test
  public void testLowPriceUpPointStatistics() {
    LowPriceUpPointStatistics analysis=new LowPriceUpPointStatistics(new TargetDefinition(7, 0.02f)); 
   // analysis.analyseAccuracy();
    SimpleDateFormat  formate=new SimpleDateFormat("yyyy-MM-dd");
    Map<String, BuyPointStatisticsData> statistics=null;
    try {
      statistics = analysis.statisticBuypointData("sz002250",formate.parse("2016-5-20"),formate.parse("2016-7-20"));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    int hit=0,nohit=0;
    for(String key:statistics.keySet()){
      BuyPointStatisticsData  data=statistics.get(key);
      if(data.getUpPower()>0){
        hit++;
      }else{
        nohit++;
      }
    }
    System.out.println((float)hit/(float)(hit+nohit));
  }

}
