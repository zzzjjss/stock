package com.uf.stock.k_analysis;

public class MACD_Analysis {
private EXPMA_Analysis shortEMA,longEMA,diffEMA;
public MACD_Analysis(int shortEmaDays,int longEmaDays,int diffDays){
  this.shortEMA=new EXPMA_Analysis(shortEmaDays);
  this.longEMA=new EXPMA_Analysis(longEmaDays);
  this.diffEMA=new EXPMA_Analysis(diffDays);
}

public MACDResult calculateMACD(float closePrice,float predayShortEXPMA,float predayLongEXPMA,float predayDiffEXPMA){
  MACDResult  result=new MACDResult();
  float shortEmaValue=shortEMA.calculateEXPMA(closePrice, predayShortEXPMA);
  float longEmaValue=longEMA.calculateEXPMA(closePrice, predayLongEXPMA);
  float diff=shortEmaValue-longEmaValue;
  float dea=diffEMA.calculateEXPMA(diff, predayDiffEXPMA);
  float macd=2*(diff-dea);
  result.setShortEma(shortEmaValue);
  result.setLongEma(longEmaValue);
  result.setDea(dea);
  result.setDiff(diff);
  result.setMacd(macd);
  return result;
}



public static class MACDResult{
  private float shortEma,longEma,diff,dea,macd;

  
  public float getShortEma() {
    return shortEma;
  }

  public void setShortEma(float shortEma) {
    this.shortEma = shortEma;
  }

  public float getLongEma() {
    return longEma;
  }

  public void setLongEma(float longEma) {
    this.longEma = longEma;
  }

  public float getDiff() {
    return diff;
  }

  public void setDiff(float diff) {
    this.diff = diff;
  }

  public float getDea() {
    return dea;
  }

  public void setDea(float dea) {
    this.dea = dea;
  }

  public float getMacd() {
    return macd;
  }

  public void setMacd(float macd) {
    this.macd = macd;
  }
  
}

}
