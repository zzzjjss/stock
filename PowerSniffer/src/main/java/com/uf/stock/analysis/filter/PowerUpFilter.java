package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;

public class PowerUpFilter implements StockFilter{
  private float upPowerDefine;
  public PowerUpFilter(float upPowerDefine){
    this.upPowerDefine=upPowerDefine;
  }
  @Override
  public FilterResult doFilter(StockTradeInfo tradeInfo) {
    FilterResult result=new FilterResult();
    if(tradeInfo!=null&&tradeInfo.getUpDownRate()>0){
        float power=tradeInfo.getUpDownRate()/tradeInfo.getTurnoverRate();
        result.setFilterValue(power);
        if(power>upPowerDefine){
          result.setIsPass(true);
        }else {
          result.setIsPass(false);
        }
    }else{
      result.setFilterValue(-1f);
      result.setIsPass(false);
    }
    return result;
  }
  
}
