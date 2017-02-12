package com.uf.stock;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.uf.stock.analysis.LowPriceUpPointStatistics;
import com.uf.stock.analysis.TargetDefinition;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.statistics.StatisticsTool;
import com.uf.stock.util.SpringBeanFactory;

public class StatisticsTest {

	@Test
	public void test() {
		 DataSyncService dataService=SpringBeanFactory.getBean(DataSyncService.class);
		    DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
		    LowPriceUpPointStatistics analysis=new LowPriceUpPointStatistics(new TargetDefinition(7, 2.0f)); 
		    List<StockInfo> stocks=dataService.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
		    StatisticsTool tool=new  StatisticsTool();
		    for(StockInfo stock:stocks){
		    	System.out.println(stock.getName());
		    	tool.statisticBuyPointByKline(stock.getSymbol());
		    }
		
		
	}

}
