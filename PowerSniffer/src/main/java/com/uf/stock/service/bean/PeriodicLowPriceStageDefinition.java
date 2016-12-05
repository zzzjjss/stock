package com.uf.stock.service.bean;

public class PeriodicLowPriceStageDefinition {
      private float toLowestMaxDownPercent=0.0f;
      private int periodicDays=30;
      public PeriodicLowPriceStageDefinition(float toLowestMaxDownPercent,int periodicDays){
        this.toLowestMaxDownPercent=toLowestMaxDownPercent;
        this.periodicDays=periodicDays;
      }
      public float getToLowestMaxDownPercent() {
        return toLowestMaxDownPercent;
      }
      public int getPeriodicDays() {
        return periodicDays;
      }
      
      
}

