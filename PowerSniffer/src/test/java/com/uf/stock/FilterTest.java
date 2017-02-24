package com.uf.stock;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.uf.stock.analysis.LowPriceUpStockFilterChain;
import com.uf.stock.analysis.filter.MACDFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class FilterTest {

  @Test
  public void priceFilterTest() {
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors=service.findDateAscTradeInfosBeforeDate(300498, new Date(), 10);
    LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
    chain.appendStockFilter(new PriceFilter(infors, 10f));
    chain.doFilter(infors.get(infors.size()-1));
  }
  @Test
  public void macdFilterTest() {
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockTradeInfo> infors=service.findDateAscTradeInfosBeforeDate(300482, new Date(), 1000);
    LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
    chain.appendStockFilter(new MACDFilter(infors));
    chain.doFilter(infors.get(infors.size()-1));
  }
  

}
