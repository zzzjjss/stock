package com.uf.stock.service.bean;

public class StableStageDefinition implements StageDefinition{
  private float amplitude=0.0f;
  private int days=30;
  public StableStageDefinition(int days,float amplitude){
    this.days=days;
    this.amplitude=amplitude;
  }
  public float getAmplitude() {
    return amplitude;
  }
  public int getDays() {
    return days;
  }
  
}
