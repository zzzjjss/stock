package com.uf.stock.analysis.filter;

import com.uf.stock.bean.KLine;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.util.StockUtil;

public class MorningStarFilter {
  public FilterResult doFilter(StockTradeInfo tradeInfo0, StockTradeInfo tradeInfo1, StockTradeInfo tradeInfo2) {
    FilterResult result = new FilterResult();
    KLine kline0 = StockUtil.calculateStandardKline(tradeInfo0);
    KLine kline1 = StockUtil.calculateStandardKline(tradeInfo1);
    KLine kline2 = StockUtil.calculateStandardKline(tradeInfo2);
    if (tradeInfo0.getUpDownRate() < 0f && kline0.getUpperShadowLength() > 0 && kline0.getLowerShadowLength() > 0 && (kline0.getRealLength() / kline0.getTotalLength()) > 0.02) {
      System.out.println("1pass");
      if ((kline1.getLowerShadowLength()/kline1.getTotalLength())>0.2) {
        System.out.println("2pass");
        if (tradeInfo2.getUpDownPrice()>0f&& tradeInfo2.getClosePrice() >= (tradeInfo0.getClosePrice() + ((tradeInfo0.getOpenPrice() - tradeInfo0.getClosePrice()) / 2f))) {
          System.out.println("3pass");
          result.setIsPass(true);
          return result;
        }
      }
    } 
      result.setIsPass(false);
    
    return result;
  }
}
