package com.uf.stock.k_analysis;

public class AnalysisResult {
  private float avgUpSpeed;
  private float maxUpSpeed;
  private float avgDownSpeed;
  private float maxDownSpeed;
  private float maxUpDownRate;
  private float minUpDownRate;
  private float upDownRate;
  
  public float getAvgUpSpeed() {
    return avgUpSpeed;
  }
  public void setAvgUpSpeed(float avgUpSpeed) {
    this.avgUpSpeed = avgUpSpeed;
  }
  public float getMaxUpSpeed() {
    return maxUpSpeed;
  }
  public void setMaxUpSpeed(float maxUpSpeed) {
    this.maxUpSpeed = maxUpSpeed;
  }
  public float getAvgDownSpeed() {
    return avgDownSpeed;
  }
  public void setAvgDownSpeed(float avgDownSpeed) {
    this.avgDownSpeed = avgDownSpeed;
  }
  public float getMaxDownSpeed() {
    return maxDownSpeed;
  }
  public void setMaxDownSpeed(float maxDownSpeed) {
    this.maxDownSpeed = maxDownSpeed;
  }
  public float getUpDownRate() {
    return upDownRate;
  }
  public void setUpDownRate(float upDownRate) {
    this.upDownRate = upDownRate;
  }
  
  public float getMaxUpDownRate() {
    return maxUpDownRate;
  }
  public void setMaxUpDownRate(float maxUpDownRate) {
    this.maxUpDownRate = maxUpDownRate;
  }
  public float getMinUpDownRate() {
    return minUpDownRate;
  }
  public void setMinUpDownRate(float minUpDownRate) {
    this.minUpDownRate = minUpDownRate;
  }
  public float calculateSidewayIndex(){
    return (maxUpSpeed+maxDownSpeed)+(Math.abs(upDownRate))/100f;
  } 
  
  
  @Override
  public String toString(){
    return "maxUpSpeed:"+maxUpSpeed+";avgUpSpeed:"+avgUpSpeed+";maxDownSpeed:"+maxDownSpeed+";avgDownSpeed:"+avgDownSpeed+";upDownRate:"+upDownRate+"%";
  }
}
