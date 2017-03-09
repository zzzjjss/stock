package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;

public class KLineTFilter implements StockFilter{
  private float lowerShadowPercent;
  public KLineTFilter(float lowerShadowPercent){
    this.lowerShadowPercent=lowerShadowPercent;
  }
  
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    float preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
    float length=(preDayClosePrice*1.1f)-(preDayClosePrice*0.9f);
    float lowerShadowLength,upperShadowLength,realLength;
    if (tradeInfo.getUpDownPrice()>0) {
      upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getClosePrice();
      lowerShadowLength=preDayClosePrice-tradeInfo.getLowestPrice();
    }else{
      lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
      upperShadowLength=tradeInfo.getHighestPrice()-preDayClosePrice;
    }
    float lowerUpPower=lowerShadowLength/length;
    float upperDownPower=upperShadowLength/length;
    result.setFilterValue(lowerUpPower);
    if(lowerUpPower>lowerShadowPercent&&upperDownPower<0.1f){
      result.setIsPass(true);;
    }else{
      result.setIsPass(false);;
    }
    
    return result;
  }

}
