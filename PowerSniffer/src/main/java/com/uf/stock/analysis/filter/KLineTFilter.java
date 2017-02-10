package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;

public class KLineTFilter implements StockFilter{
  
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    float preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
    float length=(preDayClosePrice*1.1f)-(preDayClosePrice*0.9f);
    float lowerShadowLength;
    if (tradeInfo.getUpDownPrice()>0) {
      lowerShadowLength=tradeInfo.getOpenPrice()-tradeInfo.getLowestPrice();
    }else{
      lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
    }
    float power=lowerShadowLength/length;
    result.setFilterValue(power);
    if(power>0.05){
      result.setIsPass(true);;
    }else{
      result.setIsPass(false);;
    }
    
    return result;
  }

}
