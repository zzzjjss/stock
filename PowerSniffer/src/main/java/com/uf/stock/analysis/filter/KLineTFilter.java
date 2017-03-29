package com.uf.stock.analysis.filter;

import com.uf.stock.bean.KLine;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.util.StockUtil;

public class KLineTFilter implements StockFilter{
  private float lowerShadowPercent;
  public KLineTFilter(float lowerShadowPercent){
    this.lowerShadowPercent=lowerShadowPercent;
  }
  
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    float preDayClosePrice=0f;
    if (tradeInfo.getClosePrice()!=null&&tradeInfo.getUpDownRate()!=null&&(1+(tradeInfo.getUpDownRate()*0.01f))!=0f) {
      preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
    }
    float length=(preDayClosePrice*1.1f)-(preDayClosePrice*0.9f);
    
//    KLine kLine=StockUtil.calculateStandardKline(tradeInfo);
    KLine kLine=calculateByTline(tradeInfo);
    if (length!=0) {
      float lowerUpPower=kLine.getLowerShadowLength()/length;
      float upperDownPower=kLine.getUpperShadowLength()/length;
      result.setFilterValue(lowerUpPower);
      if(lowerUpPower>lowerShadowPercent&&upperDownPower<0.1f){
        result.setIsPass(true);
      }else{
        result.setIsPass(false);
      }
    }else {
      result.setIsPass(false);
    }
    return result;
  }
  
  
  private KLine calculateByTline(StockTradeInfo tradeInfo){
    KLine k=new KLine();
    float preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
    float lowerShadowLength,upperShadowLength,realLength;
    if (tradeInfo.getUpDownPrice()>0) {
      upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getClosePrice();
      lowerShadowLength=preDayClosePrice-tradeInfo.getLowestPrice();
      realLength=tradeInfo.getClosePrice()-preDayClosePrice;
    }else{
      lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
      upperShadowLength=tradeInfo.getHighestPrice()-preDayClosePrice;
      realLength=preDayClosePrice-tradeInfo.getClosePrice();
    }
    k.setLowerShadowLength(lowerShadowLength);
    k.setRealLength(realLength);
    k.setUpperShadowLength(upperShadowLength);
    return k;
  }
}
