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
    
    KLine kLine=calculateByTline(tradeInfo);
    
    float lowerUpPower=kLine.getLowerShadowLength()/length;
    float upperDownPower=kLine.getUpperShadowLength()/length;
    result.setFilterValue(lowerUpPower);
    if(lowerUpPower>lowerShadowPercent&&upperDownPower<0.1f){
      result.setIsPass(true);;
    }else{
      result.setIsPass(false);;
    }
    
    return result;
  }
  private KLine calculateByTline2(StockTradeInfo tradeInfo){
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
  
  private KLine calculateByTline(StockTradeInfo tradeInfo){
    KLine k=new KLine();
    float preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
    float length=(preDayClosePrice*1.1f)-(preDayClosePrice*0.9f);
    float lowerShadowLength,upperShadowLength,realLength;
    if (tradeInfo.getUpDownPrice()>0) {
      upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getClosePrice();
      lowerShadowLength=tradeInfo.getOpenPrice()-tradeInfo.getLowestPrice();
      realLength=tradeInfo.getClosePrice()-tradeInfo.getOpenPrice();
    }else{
      upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getOpenPrice();
      lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
      realLength=tradeInfo.getOpenPrice()-tradeInfo.getClosePrice();
    }
    
    k.setLowerShadowLength(lowerShadowLength);
    k.setRealLength(realLength);
    k.setUpperShadowLength(upperShadowLength);
    return k;
  }
  private class KLine{
    float upperShadowLength,lowerShadowLength,realLength;

    public float getUpperShadowLength() {
      return upperShadowLength;
    }

    public void setUpperShadowLength(float upperShadowLength) {
      this.upperShadowLength = upperShadowLength;
    }

    public float getLowerShadowLength() {
      return lowerShadowLength;
    }

    public void setLowerShadowLength(float lowerShadowLength) {
      this.lowerShadowLength = lowerShadowLength;
    }

    public float getRealLength() {
      return realLength;
    }

    public void setRealLength(float realLength) {
      this.realLength = realLength;
    }
    
  }
}
