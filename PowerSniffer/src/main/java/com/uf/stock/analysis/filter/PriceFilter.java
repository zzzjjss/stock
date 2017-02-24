package com.uf.stock.analysis.filter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.uf.stock.data.bean.StockTradeInfo;

public class PriceFilter implements StockFilter{
private List<StockTradeInfo> orderedTradeInfos;  
private float downPercentToLowest;
public PriceFilter(List<StockTradeInfo> orderedTradeInfos,float downPercentToLowest){
  this.orderedTradeInfos=orderedTradeInfos;
  this.downPercentToLowest=downPercentToLowest;
}
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
	SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
    FilterResult result=new FilterResult();
    float lowestDownPercent=0f,upDownPercent=0f;
    StockTradeInfo first=orderedTradeInfos.get(0);
//    float origPrice=(float)(first.getClosePrice()/(1+first.getUpDownRate()*0.01));
    for (int i=0;i<orderedTradeInfos.size();i++) {
        if (i==0) {
          continue;
        }
        StockTradeInfo   stockTradeInfo=orderedTradeInfos.get(i);
        float lowestUpDownPercent=(float)(((stockTradeInfo.getLowestPrice()*(1+stockTradeInfo.getUpDownRate()*0.01))/stockTradeInfo.getClosePrice())-1)*100;
        float tmpLowestDownPercent=upDownPercent+lowestUpDownPercent;
        upDownPercent=upDownPercent+stockTradeInfo.getUpDownRate();
        if (lowestDownPercent>tmpLowestDownPercent) {
          lowestDownPercent=tmpLowestDownPercent;
        }
        if (stockTradeInfo.getTradeDate().getTime()>=tradeInfo.getTradeDate().getTime()) {
          break;
        }
    }
    
//    float lowestPrice=(float)(origPrice*(1+lowestDownPercent*0.01));
//    float nowPrice=(float)(origPrice*(1+upDownPercent*0.01));
//    float downPerToLowest2=100*(nowPrice-lowestPrice)/nowPrice;
    float downPerToLowest=upDownPercent-lowestDownPercent;
    result.setFilterValue(downPerToLowest);
    if (downPerToLowest<downPercentToLowest) {
      result.setIsPass(true);
    }else {
      result.setIsPass(false);
    }
    return result;
  }

}
