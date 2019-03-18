package com.uf.stock.restful.action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.restful.bean.RestfulResponse;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;
@Singleton
@Path("/data")
public class SyncDataAction {
  private Logger logger = LogManager.getLogger(SyncDataAction.class);
  private DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@GET
	@Path("/syncDailyTrade")
	public String startSyncDailyTradeInfo() {
		RestfulResponse response = new RestfulResponse();
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
	            int syncCount=0;
	            int total=service.syncAllStocksBaseInfo();
	            logger.info("total sync new stock:"+total);
	            ExecutorService pool = Executors.newFixedThreadPool(5);
	            List<StockInfo> allStocks=service.findAllStocks();
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
			}
		});
		thread.start();
		response.setSuccess(true);
		Gson gson = new Gson();
		return gson.toJson(response);
	}

}
