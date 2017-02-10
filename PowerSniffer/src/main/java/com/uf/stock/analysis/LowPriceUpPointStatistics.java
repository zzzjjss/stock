package com.uf.stock.analysis;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uf.stock.analysis.filter.KLineTFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

public class LowPriceUpPointStatistics {
    private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
    private Logger logger = LogManager.getLogger(LowPriceUpPointStatistics.class);
    private DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
    private TargetDefinition targetDefin;
    public LowPriceUpPointStatistics(TargetDefinition targetDefin){
      this.targetDefin=targetDefin;
    }
    public Float analyseAccuracy(String stockSymbol)throws IllegalArgumentException{
      SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
      int hitNum=0,nohitNum=0;
      StockInfo stock=service.findStockInfoByStockSymbol(stockSymbol);
      if (stock==null) {
        throw new IllegalArgumentException("stockSymbol is invalid");
      }
      
      List<StockTradeInfo> tradeInfos=service.findAllTradeInfosOrderByDateAsc(stock.getCode());
      if (tradeInfos!=null) {
        LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
        //chain.appendStockFilter(new PriceFilter(tradeInfos,20f)).appendStockFilter(new DayAverageGoldXFilter(5, 10));
        //chain.appendStockFilter(new PriceFilter(tradeInfos,15f)).appendStockFilter(new KLineFilter(5));
//        chain.appendStockFilter(new PriceFilter(tradeInfos,15f)).appendStockFilter(new KLineFilterV2(5));
        chain.appendStockFilter(new PriceFilter(tradeInfos,15f)).appendStockFilter(new KLineTFilter());
//        chain.appendStockFilter(new PriceFilter(tradeInfos,5f));
//             .appendStockFilter(new KLineFilter(5));
        int i=0;
        if (tradeInfos.size()>100) {
          i=tradeInfos.size()-100;
        }
        for (;i<tradeInfos.size();i++) {
          StockTradeInfo stockTradeInfo= tradeInfos.get(i);
          boolean isPass=chain.isLowPriceUpPoint(stockTradeInfo);
          if (isPass) {
            System.out.println(chain.getFilterChainResult());
            logger.info("lowPrice Up  point:" + formate.format(stockTradeInfo.getTradeDate()));
            int index=tradeInfos.indexOf(stockTradeInfo);
            if(index<tradeInfos.size()-1){
              float upPercent=0f;
              int days=-1;
              for(int tmp=index+1;tmp<tradeInfos.size();tmp++){
                StockTradeInfo tmpTradeInfo=tradeInfos.get(tmp);
                upPercent=upPercent+tmpTradeInfo.getUpDownRate();
                days++;
                if(upPercent>=targetDefin.getUpPercent()){
                  break;
                }
              }
              logger.info("after days:" + days+" up to targetUpPercent");
              if (days <= targetDefin.getDays()) {
                hitNum++;
              } else {
                nohitNum++;
              }
            }
        }
        }
      }
      if(hitNum+nohitNum!=0){
        float accuracy=(float)((float)hitNum/(float)(hitNum+nohitNum));
        logger.info("hitNum:"+hitNum+" noHitNum:"+nohitNum+" ; accuracy :"+accuracy);
        return accuracy;
      }else{
        return null;
      }
      
    }
}
