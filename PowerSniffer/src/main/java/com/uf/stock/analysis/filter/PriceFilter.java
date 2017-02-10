package com.uf.stock.analysis.filter;

import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;

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
    float lowestDownPercent=0f,upDownPercent=0f;
    for (StockTradeInfo stockTradeInfo : orderedTradeInfos) {
        upDownPercent=upDownPercent+stockTradeInfo.getUpDownRate();
        if (lowestDownPercent>upDownPercent) {
          lowestDownPercent=upDownPercent;
        }
        if (stockTradeInfo.getTradeDate().getTime()>=tradeInfo.getTradeDate().getTime()) {
          break;
        }
    }
    float downPerToLowest=upDownPercent-lowestDownPercent;
    result.setFilterValue(downPerToLowest);
    if (downPerToLowest<downPercentToLowest) {
      result.setIsPass(true);
    }else {
      result.setIsPass(false);
    }
    return result;
  }

}
