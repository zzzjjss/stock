package com.uf.stock;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.uf.stock.analysis.LowPriceUpStockFilterChain;
import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.KLineTFilter;
import com.uf.stock.analysis.filter.MACDFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class FilterTest {

  @Test
  public void priceFilterTest() {
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors=service.findDateAscTradeInfosBeforeDate(300498, new Date(), 300);
    PriceFilter filter=new PriceFilter(infors, 10f);
    FilterResult result=filter.doFilter(infors.get(infors.size()-1));
    System.out.println(result.getFilterValue());
  }
  @Test
  public void macdFilterTest() {
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors=service.findDateAscTradeInfosBeforeDate(300482, new Date(), 1000);
    LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
    chain.appendStockFilter(new MACDFilter(infors));
    chain.doFilter(infors.get(infors.size()-1));
  }

  @Test
  public void tKlineFilterTest(){
    DateFormat format=new SimpleDateFormat("yyyyMMdd");
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    KLineTFilter filter=new KLineTFilter(0.2f);
    try {
      StockTradeInfo info= service.findOneDayTradeInfo(2703, format.parse("20151230"));
      filter.doFilter(info);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
