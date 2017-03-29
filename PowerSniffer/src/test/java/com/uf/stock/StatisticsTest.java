package com.uf.stock;

import java.util.List;

import org.junit.Test;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.statistics.StatisticsTool;
import com.uf.stock.util.SpringBeanFactory;

public class StatisticsTest {

	@Test
	public void test() {
		 DataSyncService dataService=SpringBeanFactory.getBean(DataSyncService.class);
		    List<StockInfo> stocks=dataService.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
		    StatisticsTool tool=new  StatisticsTool();
		    for(StockInfo stock:stocks){
		    	System.out.println(stock.getName());
		    	tool.statisticBuyPointByKline(stock.getSymbol());
		    }
	}
	@Test
	public void testPriceBuyPoint(){
	  StatisticsTool tool=new StatisticsTool();
	  tool.statisticBuyPointByLowestPrice();
	}
	@Test
    public void testExpmaPoint(){
      StatisticsTool tool=new StatisticsTool();
      tool.statisticBuyPointByEXPMA();
    }
	@Test
	public void testTKlinePoint(){
	  StatisticsTool tool=new StatisticsTool();
	  tool.statisticBuyPointByTKline();
	}
	@Test
    public void testMorningStarBuyPoint(){
      StatisticsTool tool=new StatisticsTool();
      tool.statisticBuyPointByMorningStar();
    }
}
