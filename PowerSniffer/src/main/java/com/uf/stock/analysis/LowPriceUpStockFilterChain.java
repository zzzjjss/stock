package com.uf.stock.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.StockFilter;
import com.uf.stock.data.bean.StockTradeInfo;

public class LowPriceUpStockFilterChain {
  private List<StockFilter> filters=new ArrayList<StockFilter>();
  private Map<String, Object> filterResult=new HashMap<String, Object>(); 
  public LowPriceUpStockFilterChain appendStockFilter(StockFilter stockFilter){
    filters.add(stockFilter);
    return this;
  }
  
  
  public Boolean doFilter(StockTradeInfo tradeInfo){
    filterResult.clear();
    for(StockFilter filter:filters){
      FilterResult result=filter.doFilter(tradeInfo);
      if (result==null) {
        return null;
      }
      filterResult.put(filter.getClass().getName(), result.getFilterValue());
      if(!result.getIsPass()){
        return false;
      }
    }
    return true;
  }
  public Map<String, Object> getFilterChainResult(){
    return filterResult;
  }
  
  
}
