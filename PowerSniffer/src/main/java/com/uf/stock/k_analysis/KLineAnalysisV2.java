package com.uf.stock.k_analysis;

import com.uf.stock.data.bean.StockTradeInfo;

public class KLineAnalysisV2 {
  public static float rate=0.5f;
  
    public static DayBuySellPower calculateBuySellPower(StockTradeInfo tradeInfo){
      DayBuySellPower result=new DayBuySellPower();
      float preDayClosePrice=tradeInfo.getClosePrice()/(1+(tradeInfo.getUpDownRate()*0.01f));
      float length=(preDayClosePrice*1.1f)-(preDayClosePrice*0.9f);
      if (tradeInfo.getUpDownPrice()>0) {
        float upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getClosePrice();
        float realUpLength=tradeInfo.getUpDownPrice();
        float lowerShadowLength=preDayClosePrice-tradeInfo.getLowestPrice();
        if (lowerShadowLength<0) {
          lowerShadowLength=0f;
        }
        float upPower=realUpLength+(lowerShadowLength*rate);
        float downPower=upperShadowLength*rate;
        result.setBuyPower(upPower/length);
        result.setSellPower(downPower/length);
      }else if (tradeInfo.getUpDownPrice()==0) {
        float upperShadowLength=tradeInfo.getHighestPrice()-tradeInfo.getClosePrice();
        float lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
        float upPower=lowerShadowLength*rate;
        float downPower=upperShadowLength*rate;
        result.setBuyPower(upPower/length);
        result.setSellPower(downPower/length);
      }else{
        float upperShadowLength=tradeInfo.getHighestPrice()-preDayClosePrice;
        if(upperShadowLength<0){
          upperShadowLength=0f;
        }
        float realDownLength=0-tradeInfo.getUpDownPrice();
        float lowerShadowLength=tradeInfo.getClosePrice()-tradeInfo.getLowestPrice();
        float upPower=(lowerShadowLength*rate);
        float downPower=realDownLength+upperShadowLength*rate;
        result.setBuyPower(upPower/length);
        result.setSellPower(downPower/length);
      }
      return result;
    }
    
    public static class DayBuySellPower{
      private Float buyPower,sellPower;

      public Float getBuyPower() {
        return buyPower;
      }

      public void setBuyPower(Float buyPower) {
        this.buyPower = buyPower;
      }

      public Float getSellPower() {
        return sellPower;
      }

      public void setSellPower(Float sellPower) {
        this.sellPower = sellPower;
      }
      
    }
}
