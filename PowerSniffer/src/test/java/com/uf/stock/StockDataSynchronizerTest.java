package com.uf.stock;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import com.uf.stock.data.bean.ConfigInfo;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;
import com.uf.stock.data.exception.DataSyncException;
import com.uf.stock.data.sync.QqDataSynchronizer;
import com.uf.stock.data.sync.StockDataSynchronizer;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class StockDataSynchronizerTest {

  @Test
  public void test() {
//    StockDataSynchronizer sync=SpringBeanFactory.getBean(StockDataSynchronizer.class);
//    try {
////      List<StockTradeInfoWithAnalysisResult> results=sync.syncStockDateTradeInfosWithAnalysisResult("sh601766", null);
////      System.out.println(results.size());
//      DateFormat format=new SimpleDateFormat("yyyyMMdd");
//      try {
//        List<StockTradeInfoWithAnalysisResult> result2=sync.syncStockDateTradeInfosWithAnalysisResult("sh601766", format.parse("20170110"));
//        System.out.println(result2.size());
//      } catch (ParseException e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//      }
//    } catch (DataSyncException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
//    DateFormat format=new SimpleDateFormat("yyyyMMdd");
//    try {
//      service.isTradeDate(format.parse("20170224"));
//    } catch (ParseException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
	  ConfigInfo config=new ConfigInfo();
	  config.setIsUseProxy(false);
	  QqDataSynchronizer sync=new QqDataSynchronizer(config);
	  List<StockInfo> all=sync.syncAllStocksInfo();
	  System.out.println(all);
  }

}
