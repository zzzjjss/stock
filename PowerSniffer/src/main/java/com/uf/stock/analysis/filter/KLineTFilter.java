package com.uf.stock.analysis.filter;

import com.uf.stock.bean.KLine;
import com.uf.stock.bean.KLineUpDownPower;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.util.StockUtil;

public class KLineTFilter implements StockFilter{
  private float lowerShadowPercent;
  public KLineTFilter(float lowerShadowPercent){
    this.lowerShadowPercent=lowerShadowPercent;
  }
  
  @Override
  public FilterResult<KLineUpDownPower> doFilter(StockTradeInfo tradeInfo) {
    FilterResult<KLineUpDownPower> result=new FilterResult<KLineUpDownPower>();
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
      KLineUpDownPower power=new KLineUpDownPower();
      power.setDownPowerValue(upperDownPower);
      power.setUpPowerValue(lowerUpPower);
      result.setFilterValue(power);
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
    float lowerShadowLength,upperShadowLength;
      upperShadowLength=Math.abs(tradeInfo.getHighestPrice()-tradeInfo.getClosePrice());
      lowerShadowLength=Math.abs(tradeInfo.getClosePrice()-tradeInfo.getLowestPrice());
    k.setLowerShadowLength(lowerShadowLength);
    //k.setRealLength(realLength);
    k.setUpperShadowLength(upperShadowLength);
    return k;
  }
}
