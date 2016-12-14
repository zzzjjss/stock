package com.uf.stock.sniffer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class Schedule {
  private DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
  private Logger logger = LogManager.getLogger(Schedule.class);
  public void startSchedule(){
    Thread autoSyncData=new Thread(new Runnable() {
      
      @Override
      public void run() {
        Calendar   cal=Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);
        Date  targetTime=cal.getTime();
        long sleepTime=targetTime.getTime()-System.currentTimeMillis();
        
        while(true){
          try{
            Thread.sleep(sleepTime);
            long begin=System.currentTimeMillis();
            service.syncAllStocksBaseInfo();
            int syncCount=0;
            List<StockInfo> allStocks=service.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
            ExecutorService pool = Executors.newFixedThreadPool(5);
            List<Future<Integer>> results=new ArrayList<Future<Integer>>(); 
            for(StockInfo stock:allStocks){
                final String symbolTmp=stock.getSymbol();
                Future<Integer> future=pool.submit(new Callable<Integer>() {
                    public Integer  call(){
                        return service.syncStockTradeInfos(symbolTmp);
                    }
                }); 
                results.add(future);
            }
            pool.shutdown();
            for(Future<Integer> result:results){
                try {
                    Integer count=result.get();
                    logger.info("sync tradeInfor count:"+count);
                    syncCount=syncCount+count;
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            }
            logger.info("total sycn  tradeInfor "+syncCount);
            
            long spendTime=System.currentTimeMillis()-begin;
            
            sleepTime=(24*3600*1000)-spendTime;
          }catch(Exception e){
            e.printStackTrace();
          }
        }
      }
    },"auto sync stock data");
    autoSyncData.start();
    logger.info("auto sycn threas started");
  }
}
