package com.uf.stock.k_analysis;


public class KLineAnalysis {
  private static boolean isInPureUp(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice==closePrice&&openPrice==lowestPrice) {
      return true;
    }
    return false;
  }
  private static  boolean  isInPureDown(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if(closePrice<openPrice&&openPrice==highestPrice&&closePrice==lowestPrice){
      return true;
    }
    return false;
  }
  private static  boolean isInUpperShadownUp_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if (closePrice>openPrice&&highestPrice>closePrice&&openPrice==lowestPrice) {
      float entityDistance=closePrice-openPrice;
      float shadowDistance=highestPrice-closePrice;
      if(entityDistance>shadowDistance){
        return true;
      }
    }
    return false;
  }
  private static boolean isInUpperShadownUp_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if (closePrice>openPrice&&highestPrice>closePrice&&openPrice==lowestPrice) {
      float entityDistance=closePrice-openPrice;
      float shadowDistance=highestPrice-closePrice;
      if(entityDistance==shadowDistance){
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInUpperShadownUp_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if (closePrice>openPrice&&highestPrice>closePrice&&openPrice==lowestPrice) {
      float entityDistance=closePrice-openPrice;
      float shadowDistance=highestPrice-closePrice;
      if(entityDistance<shadowDistance){
        return true;
      }
    }
    return false;
  }
  
  
  private static boolean isInUpperShadownDown_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if (closePrice<openPrice&&highestPrice>openPrice&&closePrice==lowestPrice) {
      float entityDistance=openPrice-closePrice;
      float shadowDistance=highestPrice-openPrice;
      if(entityDistance>shadowDistance){
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInUpperShadownDown_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if (closePrice<openPrice&&highestPrice>openPrice&&closePrice==lowestPrice) {
      float entityDistance=openPrice-closePrice;
      float shadowDistance=highestPrice-openPrice;
      if(entityDistance==shadowDistance){
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInUpperShadownDown_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice) {
    if (closePrice<openPrice&&highestPrice>openPrice&&closePrice==lowestPrice) {
      float entityDistance=openPrice-closePrice;
      float shadowDistance=highestPrice-openPrice;
      if(entityDistance<shadowDistance){
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInLowerShadowUp_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&closePrice==highestPrice&&lowestPrice<openPrice){
      float  entityDistance=closePrice-openPrice;
      float shadowDistance=openPrice-lowestPrice;
      if(entityDistance>shadowDistance)
        return true;
    } 
    return false;
  }
  private static boolean isInLowerShadowUp_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&closePrice==highestPrice&&lowestPrice<openPrice){
      float  entityDistance=closePrice-openPrice;
      float shadowDistance=openPrice-lowestPrice;
      if(entityDistance==shadowDistance)
        return true;
    } 
    return false;
  }
  private static boolean isInLowerShadowUp_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&closePrice==highestPrice&&lowestPrice<openPrice){
      float  entityDistance=closePrice-openPrice;
      float shadowDistance=openPrice-lowestPrice;
      if(entityDistance<shadowDistance)
        return true;
    } 
    return false;
  }
  
  private static boolean isInLowerShadowDown_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice==openPrice&&lowestPrice<closePrice) {
      float entityDis=openPrice-closePrice;
      float shadowDis=closePrice-lowestPrice;
      if (entityDis>shadowDis) {
        return true;
      }
    }
    return false;
  }
  private static boolean isInLowerShadowDown_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice==openPrice&&lowestPrice<closePrice) {
      float entityDis=openPrice-closePrice;
      float shadowDis=closePrice-lowestPrice;
      if (entityDis==shadowDis) {
        return true;
      }
    }
    return false;
  }
  private static boolean isInLowerShadowDown_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice==openPrice&&lowestPrice<closePrice) {
      float entityDis=openPrice-closePrice;
      float shadowDis=closePrice-lowestPrice;
      if (entityDis<shadowDis) {
        return true;
      }
    }
    return false;
  }
  
  
  private static boolean isInTwoShadowUp_UpShadowLonger_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      float entityDis=closePrice-openPrice;
      if (upShadowDis>lowerShadowDis&&entityDis>upShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowUp_UpShadowLonger_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      float entityDis=closePrice-openPrice;
      if (upShadowDis>lowerShadowDis&&entityDis==upShadowDis) {
        return true;
      }
    }
    return false;
  }
  private static boolean isInTwoShadowUp_UpShadowLonger_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      float entityDis=closePrice-openPrice;
      if (upShadowDis>lowerShadowDis&&entityDis<upShadowDis) {
        return true;
      }
    }
    return false;
  }
  private static boolean isInTwoShadowUp_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      if (upShadowDis==lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  public static boolean isInTwoShadowUp_UpperShadowShorter_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      float entityDis=closePrice-openPrice;
      if (upShadowDis<lowerShadowDis&&entityDis>lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  private static boolean isInTwoShadowUp_UpperShadowShorter_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      float entityDis=closePrice-openPrice;
      if (upShadowDis<lowerShadowDis&&entityDis==lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  private static boolean isInTwoShadowUp_UpperShadowShorter_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice>openPrice&&highestPrice>closePrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-closePrice;
      float lowerShadowDis=openPrice-lowestPrice;
      float entityDis=closePrice-openPrice;
      if (upShadowDis<lowerShadowDis&&entityDis<lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  
  private static boolean isInTwoShadowDown_UpperShadowLonger_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      float entityDis=openPrice-closePrice;
      if (upShadowDis>lowerShadowDis&&entityDis>upShadowDis) {
        return true;
      }
    }
    return false;
  }

  private static boolean isInTwoShadowDown_UpperShadowLonger_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      float entityDis=openPrice-closePrice;
      if (upShadowDis>lowerShadowDis&&entityDis==upShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowDown_UpperShadowLonger_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      float entityDis=openPrice-closePrice;
      if (upShadowDis>lowerShadowDis&&entityDis<upShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowDown_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      if (upShadowDis==lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  
  private static boolean isInTwoShadowDown_UpperShadowShorter_Long(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      float entityDis=openPrice-closePrice;
      if (upShadowDis<lowerShadowDis&&entityDis>lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowDown_UpperShadowShorter_Middle(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      float entityDis=openPrice-closePrice;
      if (upShadowDis<lowerShadowDis&&entityDis==lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowDown_UpperShadowShorter_Short(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (closePrice<openPrice&&highestPrice>openPrice&&lowestPrice<closePrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      float entityDis=openPrice-closePrice;
      if (upShadowDis<lowerShadowDis&&entityDis<lowerShadowDis) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowFloat_UpperShadowLonger(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (openPrice==closePrice&&highestPrice>openPrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      if(upShadowDis>lowerShadowDis){
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInTwoShadowFlat(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (openPrice==closePrice&&highestPrice>openPrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      if(upShadowDis==lowerShadowDis){
        return true;
      }
    }
    return false;
  }
  private static boolean isInTwoShadowFlat_LowerShadowLonger(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (openPrice==closePrice&&highestPrice>openPrice&&lowestPrice<openPrice) {
      float upShadowDis=highestPrice-openPrice;
      float lowerShadowDis=closePrice-lowestPrice;
      if(upShadowDis<lowerShadowDis){
        return true;
      }
    }
    return false;
  }
  
  private static boolean isInUpperShadowFlat(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (openPrice==closePrice&&highestPrice>openPrice&&lowestPrice==openPrice) {
      return true;
    }
    return false;
  }
  
  private static boolean isInLowerShadowFlat(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if (openPrice==closePrice&&lowestPrice<openPrice&&highestPrice==openPrice) {
      return true;
    }
    return false;
  }
  
  
  private static boolean isInPureFlat(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if(openPrice==closePrice&&lowestPrice==openPrice&&highestPrice==openPrice)
      return true;
    return false;
  }
  
  
  
  public static KLineState  analyseKLineState(float openPrice,float closePrice,float highestPrice,float lowestPrice){
    if(KLineAnalysis.isInLowerShadowDown_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_DOWN_LONG;
    }
    if(KLineAnalysis.isInLowerShadowDown_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_DOWN_MIDDLE;
    }
    if(KLineAnalysis.isInLowerShadowDown_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_DOWN_SHORT;
    }
    if(KLineAnalysis.isInLowerShadowFlat(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_FLAT;
    }
    
    if(KLineAnalysis.isInLowerShadowUp_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_UP_LONG;
    }
    if(KLineAnalysis.isInLowerShadowUp_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_UP_MIDDLE;
    }
    if(KLineAnalysis.isInLowerShadowUp_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.LOWER_SHADOW_UP_SHORT;
    }
    if(KLineAnalysis.isInPureDown(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.PURE_DOWN;
    }
    if(KLineAnalysis.isInPureFlat(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.PURE_FLAT;
    }
    if(KLineAnalysis.isInPureUp(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.PURE_UP;
    }
    if(KLineAnalysis.isInTwoShadowDown_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_MIDDLE;
    }
    if(KLineAnalysis.isInTwoShadowDown_UpperShadowLonger_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_LONG;
    }
    if(KLineAnalysis.isInTwoShadowDown_UpperShadowLonger_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_MIDDLE;
    }
    if(KLineAnalysis.isInTwoShadowDown_UpperShadowLonger_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_SHORT;
    }
    
    if(KLineAnalysis.isInTwoShadowDown_UpperShadowShorter_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_LONG;
    }
    if(KLineAnalysis.isInTwoShadowDown_UpperShadowShorter_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_MIDDLE;
    }
    if(KLineAnalysis.isInTwoShadowDown_UpperShadowShorter_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_SHORT;
    }
    
    if(KLineAnalysis.isInTwoShadowFlat(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_FLAT;
    }
    if(KLineAnalysis.isInTwoShadowUp_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_UP_MIDDLE;
    }
    if(KLineAnalysis.isInTwoShadowUp_UpperShadowShorter_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_LONG;
    }
    if(KLineAnalysis.isInTwoShadowUp_UpperShadowShorter_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_MIDDLE;
    }
    if(KLineAnalysis.isInTwoShadowUp_UpperShadowShorter_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_SHORT;
    }
    if (KLineAnalysis.isInTwoShadowUp_UpShadowLonger_Long(openPrice, closePrice, highestPrice, lowestPrice)) {
      return KLineState.TWO_SHADOW_UP_UPPER_SHADOW_LONGER_LONG;
    }
    if (KLineAnalysis.isInTwoShadowUp_UpShadowLonger_Middle(openPrice, closePrice, highestPrice, lowestPrice)) {
      return KLineState.TWO_SHADOW_UP_UPPER_SHADOW_LONGER_MIDDLE;
    }
    if (KLineAnalysis.isInTwoShadowUp_UpShadowLonger_Short(openPrice, closePrice, highestPrice, lowestPrice)) {
      return KLineState.TWO_SHADOW_UP_UPPER_SHADOW_LONGER_SHORT;
    }
    if (KLineAnalysis.isInUpperShadowFlat(openPrice, closePrice, highestPrice, lowestPrice)) {
      return KLineState.UPPER_SHADOW_FLAT;
    }
    if(KLineAnalysis.isInUpperShadownDown_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.UPPER_SHADOW_DOWN_LONG;
    }
    if (KLineAnalysis.isInUpperShadownDown_Middle(openPrice, closePrice, highestPrice, lowestPrice)) {
      return KLineState.UPPER_SHADOW_DOWN_MIDDLE;
    }
    if(KLineAnalysis.isInUpperShadownDown_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.UPPER_SHADOW_DOWN_SHORT;
    }
    
    if(KLineAnalysis.isInUpperShadownUp_Long(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.UPPER_SHADOW_UP_LONG;
    }
    if(KLineAnalysis.isInUpperShadownUp_Middle(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.UPPER_SHADOW_UP_MIDDLE;
    }
    if(KLineAnalysis.isInUpperShadownUp_Short(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.UPPER_SHADOW_UP_SHORT;
    }
    if(KLineAnalysis.isInTwoShadowFlat_LowerShadowLonger(openPrice, closePrice, highestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_FLAT_LOWER_SHADOW_LONGER;
    }
    if(KLineAnalysis.isInTwoShadowFloat_UpperShadowLonger(lowestPrice, lowestPrice, lowestPrice, lowestPrice)){
      return KLineState.TWO_SHADOW_FLAT_UPPER_SHADOW_LONGER;
    }
    return null;
  }
  
  
}
