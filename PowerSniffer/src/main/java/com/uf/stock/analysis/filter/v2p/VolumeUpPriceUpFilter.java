package com.uf.stock.analysis.filter.v2p;

import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;

public class VolumeUpPriceUpFilter {
  public static boolean isVolumeUpPriceUp(List<StockTradeInfo> tradeInfos,int days,float volumeUpMulty){
    float avgTurnoverRate=0f,totalTurnOverRate=0f;
    for (StockTradeInfo stockTradeInfo : tradeInfos) {
      float tmp=stockTradeInfo.getTurnoverRate()==null?0f:stockTradeInfo.getTurnoverRate();
      totalTurnOverRate=totalTurnOverRate+tmp;
    }
    avgTurnoverRate=totalTurnOverRate/(float)tradeInfos.size();
    List<StockTradeInfo> analysisTradeInfos=tradeInfos.subList(tradeInfos.size()-days, tradeInfos.size());
    float tempTurnOverRate=0f;
//    for (StockTradeInfo stockTradeInfo : analysisTradeInfos) {
//      if (stockTradeInfo.getTurnoverRate()>=tempTurnOverRate) {
//        tempTurnOverRate=stockTradeInfo.getTurnoverRate();
//      }else {
//        return false;
//      }
//      if (stockTradeInfo.getUpDownRate()<0) {
//        return false;
//      }
//    }
//    tempTurnOverRate=analysisTradeInfos.get(0).getTurnoverRate();
//    
//    if (tempTurnOverRate>=volumeUpMulty*avgTurnoverRate) {
//      System.out.println("avgTurnoverRate:"+avgTurnoverRate);
//      return true;
//    }else {
//      return false;
//    }
    
    for (StockTradeInfo stockTradeInfo : analysisTradeInfos) {
      float tmp=stockTradeInfo.getTurnoverRate()==null?0f:stockTradeInfo.getTurnoverRate();
      if (tmp<avgTurnoverRate*volumeUpMulty) {
        return false;
      }
      if(stockTradeInfo.getUpDownRate()<0){
        return false;
      }
    }
    return true;
  }
  
  
}
