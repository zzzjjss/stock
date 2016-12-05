package com.uf.stock.service.bean;

public class StableStage implements StockStage{
  private float amplitude=0.0f;

  public StableStage(float amplitude){
    this.amplitude=amplitude;
  }
  
  public float getAmplitude() {
    return amplitude;
  }
  
}
