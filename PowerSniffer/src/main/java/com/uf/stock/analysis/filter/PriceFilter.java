package com.uf.stock.analysis.filter;

import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.util.StockUtil;

public class PriceFilter implements StockFilter{
private List<StockTradeInfo> orderedTradeInfos;  
private float downPercentToLowest;
public PriceFilter(List<StockTradeInfo> orderedTradeInfos,float downPercentToLowest){
  this.orderedTradeInfos=orderedTradeInfos;
  this.downPercentToLowest=downPercentToLowest;
}
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    float downPerToLowest=StockUtil.calculateDownPercentToLowest(orderedTradeInfos, tradeInfo);
    result.setFilterValue(downPerToLowest);
    if (downPerToLowest<downPercentToLowest) {
      result.setIsPass(true);
    }else {
      result.setIsPass(false);
    }
    return result;
  }

}
