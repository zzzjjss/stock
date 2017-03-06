package com.uf.stock.analysis.filter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.k_analysis.EXPMA_Analysis;

public class EXPMA_Filter implements StockFilter{
  private EXPMA_Analysis emaShort=new EXPMA_Analysis(12);
  private EXPMA_Analysis emaLong=new EXPMA_Analysis(50);
  private Map<String, Integer> dateToIndex=new HashMap<String, Integer>();
  private SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
  private List<StockTradeInfo> orderedTradeInfos;
  public EXPMA_Filter(List<StockTradeInfo> orderedTradeInfos){
    this.orderedTradeInfos=orderedTradeInfos;
    StockTradeInfo preDayTradeInfo=null;
    for(int i=0;i<orderedTradeInfos.size();i++){
      StockTradeInfo tradeInfo=orderedTradeInfos.get(i);
      dateToIndex.put(formate.format(tradeInfo.getTradeDate()), i);
      float ema1=0,ema2=0;
      if (preDayTradeInfo==null) {
        ema1=emaShort.calculateEXPMA(tradeInfo.getClosePrice(), tradeInfo.getClosePrice());
        ema2=emaLong.calculateEXPMA(tradeInfo.getClosePrice(), tradeInfo.getClosePrice());
      }else {
        ema1=emaShort.calculateEXPMA(tradeInfo.getClosePrice(), preDayTradeInfo.getShortEMA());
        ema2=emaLong.calculateEXPMA(tradeInfo.getClosePrice(), preDayTradeInfo.getLongEMA());
      }
      tradeInfo.setShortEMA(ema1);
      tradeInfo.setLongEMA(ema2);
      preDayTradeInfo=tradeInfo;
    }
  }
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    Integer index=dateToIndex.get(formate.format(tradeInfo.getTradeDate()));
    if (index!=null&&index>0) {
      Integer preDayIndex=index-1;
      StockTradeInfo thisDayTradeInfo=orderedTradeInfos.get(index);
      StockTradeInfo preDayTradeInfo=orderedTradeInfos.get(preDayIndex);
      if(preDayTradeInfo.getShortEMA()<preDayTradeInfo.getLongEMA()&&tradeInfo.getClosePrice()>thisDayTradeInfo.getShortEMA()&&thisDayTradeInfo.getShortEMA()>=thisDayTradeInfo.getLongEMA()){
        result.setIsPass(true);
      }else{
        result.setIsPass(false);
      }
    }else{
      result.setIsPass(false);
    }
    return result;
  }

}
