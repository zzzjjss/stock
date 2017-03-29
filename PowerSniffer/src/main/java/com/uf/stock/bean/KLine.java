package com.uf.stock.bean;

public class KLine {
  float upperShadowLength,lowerShadowLength,realLength,totalLength;

  
  public float getTotalLength() {
    return totalLength;
  }

  public void setTotalLength(float totalLength) {
    this.totalLength = totalLength;
  }

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
