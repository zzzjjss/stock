package com.uf.stock.analysis;

public class TargetDefinition {
  private float upPercent;
  private int days;
  
  public TargetDefinition(int days,float upPecent){
    this.days=days;
    this.upPercent=upPecent;
  }
  public float getUpPercent() {
    return upPercent;
  }
  public int getDays() {
    return days;
  }
  
}
