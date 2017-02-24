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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.AlarmStock;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.restful.bean.GoUpStrategyResponse;
import com.uf.stock.restful.bean.GoUpStrategyResponseData;
import com.uf.stock.restful.bean.ResponseError;
import com.uf.stock.restful.bean.RestfulResponse;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.service.bean.StableStage;
import com.uf.stock.service.bean.StableStageDefinition;
import com.uf.stock.service.bean.StageDefinition;
import com.uf.stock.service.bean.StockStage;
import com.uf.stock.util.SpringBeanFactory;
import com.uf.stock.util.StockUtil;

@Singleton
@Path("/goUpStrategy")
public class GoUpStrategyAction {
  private DataSyncService service=SpringBeanFactory.getBean(DataSyncService.class);
  private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @GET
  @Path("/getGoUpStockInfo")
  public String getGoUpStockInfo(@QueryParam("minPeRatio")float minPeRatio,@QueryParam("maxPeRatio")float maxPeRation,
      @QueryParam("maxDownPercent")float maxDownPercent){
    GoUpStrategyResponse response=new GoUpStrategyResponse();
    try{
      List<StockInfo> stocks=service.findStocksPeRatioBetween(minPeRatio,maxPeRation);
      List<UpDownPower> powers=service.calculateStocksCurrentPower(stocks);
      List<GoUpStrategyResponseData> data=new ArrayList<GoUpStrategyResponseData>();
      for(UpDownPower power:powers){
        AlarmStock alarm=service.findAlarmStockInfoByStockCode(StockUtil.parseStockSymbolToStockCode(power.getTradeInfo().getStockSymbol()));
        float  downPercent=((power.getTradeInfo().getClosePrice()-alarm.getAlarmBuyPrice())/power.getTradeInfo().getClosePrice())*100;
        if(power.getTradeInfo().getUpDownRate()<1||downPercent>maxDownPercent){
          continue;
        }
        GoUpStrategyResponseData item=new GoUpStrategyResponseData();
        item.setDownPercent(downPercent);
        item.setPeRatio(power.getStockPeRatio());
        item.setPower(power.getUpdownPowerValue());
        item.setStockName(power.getStockName());
        item.setUpDownRate(power.getTradeInfo().getUpDownRate());
        item.setStockSymbol(power.getTradeInfo().getStockSymbol());
        data.add(item);
        //System.out.println(power.toString()+":  "+downPercent+"%");
      }
      response.setSuccess(true);
      response.setData(data);
    }catch(Exception e){
      e.printStackTrace();
      ResponseError error=new ResponseError();
      error.setCode("1");
      error.setMsg(e.getMessage());
      response.setError(error);
      response.setSuccess(false);
    }
    Gson gson=new Gson();
    return gson.toJson(response);
  }
  
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @GET
  @Path("/syncStockTradeInfo")  
  public String syncStockTradeInfo(@QueryParam("stockSymbol")String stockSymbol,@QueryParam("stockCode")Integer stockCode,@QueryParam("isSyncAll")boolean isSyncAll){
    RestfulResponse response=new RestfulResponse();
    try{
    	int syncCount=0;
      if(isSyncAll){
    		List<StockInfo> allStocks=service.findStocksPeRatioBetween(-1f, Float.MAX_VALUE);
    		ExecutorService pool = Executors.newFixedThreadPool(5);
    		List<Future<Integer>> results=new ArrayList<Future<Integer>>(); 
    		for(StockInfo stock:allStocks){
    			final String symbolTmp=stock.getSymbol();
    			Future<Integer> future=pool.submit(new Callable<Integer>() {
    				public Integer  call(){
    				  return service.syncStockTradeInfoWithAnalysisResult(symbolTmp);
    					//return service.syncStockTradeInfos(symbolTmp);
    				}
    			});	
    			results.add(future);
    		}
    		pool.shutdown();
    		for(Future<Integer> result:results){
    			try {
    				Integer count=result.get();
    				System.out.println("sync tradeInfor coutn:"+count);
    				syncCount=syncCount+count;
    			} catch (Exception e) {
    				e.printStackTrace();
    			} 
    		}
      }else{
    	  if(StringUtils.isBlank(stockSymbol)){
        	  stockSymbol=service.transToStockSymbolFromStockCode(stockCode);
          }
//          syncCount=service.syncStockTradeInfos(stockSymbol);
    	  syncCount=service.syncStockTradeInfoWithAnalysisResult(stockSymbol);
      }
      response.setMsg("total sucess :"+syncCount);
      response.setSuccess(true);
    }catch(Exception e){
      e.printStackTrace();
      ResponseError error=new ResponseError();
      error.setCode("1");
      error.setMsg(e.getMessage());
      response.setError(error);
      response.setSuccess(false);
    }
    Gson gson=new Gson();
    return gson.toJson(response);
  }
  
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @GET
  @Path("/analyseAvgPrice")
  public String analyseAvgPrice(@QueryParam("analysicDays") final int analysicDays, @QueryParam("downPercentToLowestPrice") final float downPercentToLowestPrice, @QueryParam("shortTerm") final int shortTerm, @QueryParam("mediumTerm") final int mediumTerm, @QueryParam("longTerm") final int longTerm) {
    List<StockInfo> stocks = new ArrayList<StockInfo>();
    List<StockInfo> allStocks = service.findStocksPeRatioBetween(1f, Float.MAX_VALUE);
    ExecutorService pool = Executors.newCachedThreadPool();
    List<Future<StockInfo>> results = new ArrayList<Future<StockInfo>>();
    for (final StockInfo stock : allStocks) {
      Future<StockInfo> future = pool.submit(new Callable<StockInfo>() {
        public StockInfo call() {
          Float downPercent = analyseService.calculateStockPeriodicToLowestPriceDownPercent(stock, analysicDays);
          if (downPercent != null && downPercent.floatValue() <= downPercentToLowestPrice / 100) {
            Boolean isUpPower = analyseService.calculateStockIsDayAverageGoldX(stock, shortTerm, mediumTerm, longTerm);
            if (isUpPower) {
              stock.setDownPercentToLowest(downPercent);
              return stock;
            }
          }
          return null;
        }
      });
      results.add(future);
    }
    pool.shutdown();
    for (Future<StockInfo> result : results) {
      try {
        StockInfo info = result.get();
        if (info != null) {
          stocks.add(info);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    Gson gson = new Gson();
    return gson.toJson(stocks);
  }
}
