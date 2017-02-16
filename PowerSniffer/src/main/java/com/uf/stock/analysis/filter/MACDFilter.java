package com.uf.stock.analysis.filter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.k_analysis.MACD_Analysis;
import com.uf.stock.k_analysis.MACD_Analysis.MACDResult;

public class MACDFilter implements StockFilter{
  private MACD_Analysis macdAnalysis=new MACD_Analysis(12, 26, 9); 
  private Map<String, Integer> dateToIndex=new HashMap<String, Integer>();
  private SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
  private List<StockTradeInfo> orderedTradeInfos;
  public MACDFilter(List<StockTradeInfo> orderedTradeInfos){
    this.orderedTradeInfos=orderedTradeInfos;
    StockTradeInfo preDayTradeInfo=null;
    MACDResult result=new MACDResult();
    for(int i=0;i<orderedTradeInfos.size();i++){
      StockTradeInfo tradeInfo=orderedTradeInfos.get(i);
      dateToIndex.put(formate.format(tradeInfo.getTradeDate()), i);
      if (preDayTradeInfo==null) {
        result=macdAnalysis.calculateMACD(tradeInfo.getClosePrice(), tradeInfo.getClosePrice(), tradeInfo.getClosePrice(), 0);
      }else {
        result=macdAnalysis.calculateMACD(tradeInfo.getClosePrice(), preDayTradeInfo.getMacdResult().getShortEma(),preDayTradeInfo.getMacdResult().getLongEma(),
                                          preDayTradeInfo.getMacdResult().getDea());
      }
      tradeInfo.setMacdResult(result);
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
      if (preDayTradeInfo.getMacdResult().getMacd()<=0&&thisDayTradeInfo.getMacdResult().getMacd()>0) {
        result.setIsPass(true);
      }else{
        result.setIsPass(true);
      }
    }else{
      result.setIsPass(false);
    }
    return result;
  }
  
  
  
}
