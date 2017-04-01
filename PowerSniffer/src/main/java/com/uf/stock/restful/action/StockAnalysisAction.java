package com.uf.stock.restful.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uf.stock.analysis.LowPriceUpStockFilterChain;
import com.uf.stock.analysis.filter.EXPMA_Filter;
import com.uf.stock.analysis.filter.FilterResult;
import com.uf.stock.analysis.filter.KLineTFilter;
import com.uf.stock.analysis.filter.MACDFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.bean.KLineUpDownPower;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;
import com.uf.stock.k_analysis.AnalysisResult;
import com.uf.stock.k_analysis.StockStage;
import com.uf.stock.k_analysis.StockStageAnalysis;
import com.uf.stock.restful.bean.PriceSpeedAnalysisResultData;
import com.uf.stock.restful.bean.PriceSpeedAnalysisResultResponse;
import com.uf.stock.restful.bean.ResponseError;
import com.uf.stock.restful.bean.StockStageAnalysisResultData;
import com.uf.stock.restful.bean.StockStageAnalysisResultResponse;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.util.SpringBeanFactory;

@Singleton
@Path("/analysis")
public class StockAnalysisAction {
	private DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
	private StockAnalysisService analyseService=SpringBeanFactory.getBean(StockAnalysisService.class);
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@GET
	@Path("/filterStockByPrice")
	public String filterStockByPrice(@QueryParam("minPeRatio") float minPeRatio, @QueryParam("maxPeRatio") float maxPeRation,@QueryParam("downPercentToLow") float downPercentToLow, @QueryParam("periodDays") int analysisDays
	                                  ,@QueryParam("useCurrentInfo")boolean useCurrentInfo ) {
		PriceSpeedAnalysisResultResponse response = new PriceSpeedAnalysisResultResponse();
		try {
			List<PriceSpeedAnalysisResultData> datas = new ArrayList<PriceSpeedAnalysisResultData>();
			List<StockInfo> stocks = service.findStocksPeRatioBetween(minPeRatio, maxPeRation);
			
			KLineTFilter tFilter=new KLineTFilter(0.2f);
			if (stocks != null && stocks.size() > 0) {
			  Map<String, StockTradeInfo> currentTradeInfos=null;
			  if (useCurrentInfo) {
			    List<String> stockSymbols=new ArrayList<String>();
			    for (StockInfo stock : stocks) {
			      stockSymbols.add(stock.getSymbol());
			    }
			    currentTradeInfos=service.getCurrentStocksTradeInfo(stockSymbols);
              }
				for (StockInfo stock : stocks) {
					boolean isStop=service.isStockStopTrade(stock.getCode());
					if(isStop)
						continue;
					List<StockTradeInfo> infors=service.findDateAscTradeInfosBeforeDate(stock.getCode(), new Date(), analysisDays);
					if (useCurrentInfo) {
					  StockTradeInfo currentTradeInfo=currentTradeInfos.get(stock.getSymbol());
					  if (currentTradeInfo!=null) {
					    currentTradeInfo.setTradeDate(new Date());
					    infors.add(currentTradeInfo);
					  }
                    }
					LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
					chain.appendStockFilter(new PriceFilter(infors, downPercentToLow));
					PriceSpeedAnalysisResultData data = new PriceSpeedAnalysisResultData();
					if (chain.doFilter(infors.get(infors.size()-1))) {
					  Map<String, Object>  result=chain.getFilterChainResult();
					  data.setDownRateToLowest((Float)result.get(PriceFilter.class.getName()));
					  data.setStockName(stock.getName());
					  data.setStockSymbol(stock.getSymbol());
  					  MACDFilter macdFilter=new MACDFilter(infors);
  					  StockTradeInfo latestTradeInfo=infors.get(infors.size()-1);
  					  FilterResult<Float> filterResult=macdFilter.doFilter(latestTradeInfo);
  					  Float macd=filterResult.getFilterValue();
  					  FilterResult<KLineUpDownPower> tResult=tFilter.doFilter(latestTradeInfo);
  					  data.settKLine(tResult.getFilterValue().getUpPowerValue());
  					  data.setMacd(macd);
					  datas.add(data);
                    }
					//AnalysisResult result = StockStageAnalysis.periodAnalyseStock(stock.getCode(), new Date(), analysisDays);
//					if (result.getDownRateToLowest()>=downPercentToLow) {
//                      continue;
//                    }
//					data.setSidewayIndex(result.calculateSidewayIndex());
//					data.setSlowUpFastDownIndex(result.calculateSlowUpFastDownIndex());
				}
			}
			response.setSuccess(true);
			response.setData(datas);
		} catch (Exception e) {
			e.printStackTrace();
		      ResponseError error=new ResponseError();
		      error.setCode("1");
		      error.setMsg(e.getMessage());
		      response.setError(error);
		      response.setSuccess(false);
		}
//		GsonBuilder gb=new GsonBuilder();
//		gb.serializeSpecialFloatingPointValues();
		Gson gson=new Gson();
	    return gson.toJson(response);
	}
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Path("/analyseStockStageByDayAvg")
	public String analyseStockStageByDayAvg(@FormParam("stockSymbols") String stockSymbols){
	     StockStageAnalysisResultResponse response = new StockStageAnalysisResultResponse();
	        try {
	            List<StockStageAnalysisResultData> datas = new ArrayList<StockStageAnalysisResultData>();
	            if (StringUtils.isNotBlank(stockSymbols)) {
	              String symbols[]=stockSymbols.split(";");
	              Date date=new Date();
	              for (String symbol:symbols) {
	                StockInfo stock=service.findStockInfoByStockSymbol(symbol.trim());
	                if(stock!=null){
//	                  Boolean isUp=analyseService.isDayAverageGoldX(stock, date, 5, 10);
	                  Float shortPrice=analyseService.calculateAveragePriceBeforeDate(5, date, stock.getCode());
	                  Float longPrice=analyseService.calculateAveragePriceBeforeDate(10, date, stock.getCode());
	                  List<StockTradeInfo> latestInfoEs=service.findLimitTradeInfosBeforeDate(stock.getCode(), date, 1);
	                  if (latestInfoEs!=null&&latestInfoEs.size()>0&&latestInfoEs.get(0).getClosePrice()>shortPrice&&shortPrice>longPrice) {
	                    
                      }else {
                        continue;
                      }
	                  StockStageAnalysisResultData data=new StockStageAnalysisResultData();
	                  Float avg1=analyseService.calculateAverageTurnoverRateBeforeDate(5, date, stock.getCode());
	                  Float avg2=analyseService.calculateAverageTurnoverRateBeforeDate(10, date, stock.getCode());
                      data.setTurnOverRateUp(avg1-avg2);
	                  StockStage  stage=StockStageAnalysis.analyseStockStageByKLine(stock.getCode(),date, 5);
	                  if (stage==null||stage.getUpPower()<=stage.getDownPower()) {
                        continue;
                      }else {
                        data.setUpPower((stage.getUpPower())/(stage.getUpPower()+stage.getDownPower())*100);
                      }
	                  data.setStageName("upStage");
	                  data.setStockName(stock.getName());
	                  data.setStockSymbol(stock.getSymbol());
	                  datas.add(data);
	                }
                  }
                }
	            response.setSuccess(true);
	            response.setData(datas);
	        } catch (Exception e) {
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
    @POST
    @Path("/analyseStockByEXPMA")
    public String analyseStockByEXPMA(@FormParam("stockSymbols") String stockSymbols,@FormParam("periodDays")  int periodDays){
         StockStageAnalysisResultResponse response = new StockStageAnalysisResultResponse();
            try {
                List<StockStageAnalysisResultData> datas = new ArrayList<StockStageAnalysisResultData>();
                if (StringUtils.isNotBlank(stockSymbols)) {
                  String symbols[]=stockSymbols.split(";");
                  Date date=new Date();
                  for (String symbol:symbols) {
                    StockInfo stock=service.findStockInfoByStockSymbol(symbol.trim());
                    if(stock!=null){
                      StockStageAnalysisResultData data=new StockStageAnalysisResultData();
                      List<StockTradeInfo> infors=service.findDateAscTradeInfosBeforeDate(stock.getCode(), new Date(), periodDays);
                      if (infors!=null&&infors.size()>0) {
                        LowPriceUpStockFilterChain  chain=new LowPriceUpStockFilterChain();
                        chain.appendStockFilter(new EXPMA_Filter(infors));
                        Boolean isPass=chain.doFilter(infors.get(infors.size()-1));
                        if (isPass) {
                          data.setStockName(stock.getName());
                          data.setStockSymbol(stock.getSymbol());
                          datas.add(data);
                        }
                      }
                    }
                  }
                }
                response.setSuccess(true);
                response.setData(datas);
            } catch (Exception e) {
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
    @Path("/analyseUpDownPowerByKLine")
    public String analyseUpDownPowerByKLine(@QueryParam("stockSymbol") String stockSymbol,@QueryParam("periodDays") int periodDays) {
	  StockInfo stockInfo=service.findStockInfoByStockSymbol(stockSymbol);
	  StockStage stage=new StockStage();
	  if(stockInfo!=null){
	    stage=StockStageAnalysis.analyseStockStageByKLine(stockInfo.getCode(), new Date(), periodDays);
	  }
	  GsonBuilder gBuilder=new GsonBuilder();
	  gBuilder.serializeSpecialFloatingPointValues();
	  Gson gson=gBuilder.create();
	  return gson.toJson(stage);
	}
	
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @GET
    @Path("/addToMonitor")
    public String addToMonitor(@QueryParam("stockSymbol") String stockSymbol,@QueryParam("buyPrice") float buyPrice,@QueryParam("alarmType") String alarmType) {
	  if ("buyAlarm".equals(alarmType)) {
	    service.addStockToMonitor(stockSymbol, null, buyPrice);
      }else {
        service.addStockToMonitor(stockSymbol, buyPrice*1.02f, null); 
      }
	 return "{}";
	}
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @GET
    @Path("/removeFromMonitor")
    public String removeFromMonitor(@QueryParam("stockSymbol") String stockSymbol) {
     service.removeFromMonitor(stockSymbol); 
     return "{}";
    }
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @GET
    @Path("/listStocksInMonitor")
    public String listStocksInMonitor() {
     List<StockInfo> stocks=service.findStocksInMonitor(); 
     if (stocks!=null&&stocks.size()>0) {
       GsonBuilder gBuilder=new GsonBuilder();
       gBuilder.serializeSpecialFloatingPointValues();
       Gson gson=gBuilder.create();
       return gson.toJson(stocks);
    }else {
      return "";
    }
    }
}
