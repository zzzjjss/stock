package com.uf.stock;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import com.uf.stock.analysis.filter.v2p.VolumeUpPriceUpFilter;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class V2P_Test {

  @Test
  public void test() {
    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
    List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
    List<Float> downs=new ArrayList<Float>();
    DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    SortedMap<Integer, Integer> statitc=new  TreeMap<Integer, Integer>();
    int targetDays=10;
     for (StockInfo stockInfo : stocks) {
       if (stockInfo.getName().contains("ST")) {
        continue;
       }
       List<StockTradeInfo> tradeInfos=service.findDateAscTradeInfosBeforeDate(stockInfo.getCode(), new Date(), 200);
       boolean isPass=VolumeUpPriceUpFilter.isVolumeUpPriceUp(tradeInfos, 3, 4);
       if (isPass) {
        System.out.println(stockInfo.getSymbol());
      }
     }
  }

}
