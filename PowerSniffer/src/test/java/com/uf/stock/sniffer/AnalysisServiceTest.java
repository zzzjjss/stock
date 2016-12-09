package com.uf.stock.sniffer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.service.bean.StableStageDefinition;
import com.uf.stock.service.bean.StageDefinition;
import com.uf.stock.service.bean.StockStage;
import com.uf.stock.util.SpringBeanFactory;

public class AnalysisServiceTest {

  @Test
  public void test() {
    DataSyncService dataService=SpringBeanFactory.getBean(DataSyncService.class);
    StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
    List<StockInfo> stocks=dataService.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
    for(StockInfo  stock:stocks){
      List<StageDefinition> stageDefi=new ArrayList<StageDefinition>();
      StableStageDefinition stable=new StableStageDefinition(60, 0.05f);
      stageDefi.add(stable);
      StockStage stage=analyseService.analyseStockStage(stageDefi, stock);
      if(stage!=null&&stage instanceof StableStageDefinition){
        StableStageDefinition sta=(StableStageDefinition)stage;
        System.out.println(stock.getName()+"-->"+sta.getAmplitude());
      }
    }
//    StockInfo stock=new StockInfo();
//    stock.setCode(55);
//    stock.setSymbol("sz000055");
//    analyseService.analyseCurrentStockStage(stock, 30);
  }
  @Test
  public void testHowManyDaysToTargetPrice(){
    StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    
    try {
      int days=analyseService.howManyDaysToTargetPrice("sz002565", format.parse("2014-04-25"), 100.0f);
      System.out.println(days);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
