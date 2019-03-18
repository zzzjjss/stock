package com.uf.stock.picker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.KLineTFilter;
import com.uf.stock.bean.KLineUpDownPower;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class KLinePicker {
  private DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
  public PickResult pickByTKLine(Date tradeDate){
    PickResult pickResult=new PickResult();
    if (service.isTradeDate(tradeDate)) {
      DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
      List<StockInfo> stocks=service.findAllStocks();
      float tmpDownPower=Float.MAX_VALUE;
      for (StockInfo stockInfo : stocks) {
        if (stockInfo.getName().contains("ST")) {
          continue;
         }
          List<StockTradeInfo> allTradeInfos=service.findAllTradeInfosOrderByDateAscByCache(stockInfo.getCode());
          Map<String, Integer> dateToIndexMap=new HashMap<String,Integer>();
          for (int index=0;index<allTradeInfos.size();index++) {
            dateToIndexMap.put(format.format(allTradeInfos.get(index).getTradeDate()) ,index);
          }
          if (dateToIndexMap.containsKey(format.format(tradeDate))) {
            Integer index=dateToIndexMap.get(format.format(tradeDate));
            StockTradeInfo infor=allTradeInfos.get(index);
            KLineTFilter tFilter=new KLineTFilter(0.3f);
            FilterResult<KLineUpDownPower> filterResult=tFilter.doFilter(infor);
            if (filterResult.getIsPass()) {
              if (filterResult.getFilterValue().getDownPowerValue()<tmpDownPower) {
                pickResult.setAllTradeInfos(allTradeInfos);
                pickResult.setPickIndex(index);
                tmpDownPower=filterResult.getFilterValue().getDownPowerValue();
              }
            }
          }
      }
    }
    return pickResult;
  }
}
