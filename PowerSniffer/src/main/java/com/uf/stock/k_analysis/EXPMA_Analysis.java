package com.uf.stock.k_analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class EXPMA_Analysis {
  private int days;
  public EXPMA_Analysis(int days){
    this.days=days;
  }
  public float calculateEXPMA(float closePrice,float predayEXPMA){
   float result=  (2*closePrice+(days-1)*predayEXPMA)/(days+1);
   BigDecimal   b   =   new   BigDecimal(result);
   return b.setScale(2,   RoundingMode.HALF_UP).floatValue();
  }
  
}
