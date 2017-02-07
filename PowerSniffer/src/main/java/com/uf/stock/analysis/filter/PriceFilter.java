package com.uf.stock.analysis.filter;

import java.util.Date;
import java.util.List;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;

public class PriceFilter implements StockFilter{
private List<StockTradeInfo> orderedTradeInfos;  
private float downPercentToLowest;
public PriceFilter(List<StockTradeInfo> orderedTradeInfos,float downPercentToLowest){
  this.orderedTradeInfos=orderedTradeInfos;
  this.downPercentToLowest=downPercentToLowest;
}
  @Override
  public Boolean doFilter(StockInfo stock, Date date) {
    float lowestDownPercent=0f,upDownPercent=0f;
    for (StockTradeInfo stockTradeInfo : orderedTradeInfos) {
        upDownPercent=upDownPercent+stockTradeInfo.getUpDownRate();
        if (lowestDownPercent>upDownPercent) {
          lowestDownPercent=upDownPercent;
        }
        if (stockTradeInfo.getTradeDate().getTime()>=date.getTime()) {
          break;
        }
    }
    float downPerToLowest=upDownPercent-lowestDownPercent;
    if (downPerToLowest<downPercentToLowest) {
      return true;
    }else {
      return false;
    }
  }

}
