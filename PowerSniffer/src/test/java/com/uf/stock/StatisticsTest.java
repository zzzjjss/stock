package com.uf.stock;

import org.junit.Test;

import com.uf.stock.statistics.StatisticsTool;

public class StatisticsTest {

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
    public void testTurnoverRatePoint(){
      StatisticsTool tool=new StatisticsTool();
      tool.statisticByTurnoverRate();
    }
	@Test
    public void testMorningStarBuyPoint(){
      StatisticsTool tool=new StatisticsTool();
      tool.statisticBuyPointByMorningStar();
    }
}
