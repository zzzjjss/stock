package com.uf.stock.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.uf.stock.analysis.filter.StockFilter;
import com.uf.stock.data.bean.StockInfo;

public class LowPriceUpStockFilterChain {
  private List<StockFilter> filters=new ArrayList<StockFilter>();
  public LowPriceUpStockFilterChain appendStockFilter(StockFilter stockFilter){
    filters.add(stockFilter);
    return this;
  }
  public Boolean isLowPriceUpPoint(StockInfo stock,Date date){
    for(StockFilter filter:filters){
      Boolean pass=filter.doFilter(stock, date);
      if(pass!=null&&!pass){
        return false;
      }
    }
    return true;
  }
}
