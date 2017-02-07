package com.uf.stock.restful.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.uf.stock.data.bean.StockInfo;
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
	public String filterStockByPrice(@QueryParam("minPeRatio") float minPeRatio, @QueryParam("maxPeRatio") float maxPeRation,@QueryParam("downPercentToLow") float downPercentToLow, @QueryParam("periodDays") int analysisDays) {
		PriceSpeedAnalysisResultResponse response = new PriceSpeedAnalysisResultResponse();
		try {
			List<PriceSpeedAnalysisResultData> datas = new ArrayList<PriceSpeedAnalysisResultData>();
			List<StockInfo> stocks = service.findStocksPeRatioBetween(minPeRatio, maxPeRation);
			if (stocks != null && stocks.size() > 0) {
				for (StockInfo stock : stocks) {
					boolean isStop=service.isStockStopTrade(stock.getCode());
					if(isStop)
						continue;
					AnalysisResult result = StockStageAnalysis.periodAnalyseStock(stock.getCode(), new Date(), analysisDays);
					if (result.getDownRateToLowest()>=downPercentToLow) {
                      continue;
                    }
					PriceSpeedAnalysisResultData data = new PriceSpeedAnalysisResultData();
					data.setDownRateToLowest(result.getDownRateToLowest());
					data.setSidewayIndex(result.calculateSidewayIndex());
					data.setSlowUpFastDownIndex(result.calculateSlowUpFastDownIndex());
					data.setStockName(stock.getName());
					data.setStockSymbol(stock.getSymbol());
					datas.add(data);
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
	                  Boolean isUp=analyseService.isDayAverageGoldX(stock, date, 5, 10);
	                  StockStageAnalysisResultData data=new StockStageAnalysisResultData();
	                  if (isUp==null||!isUp) {
	                    continue;
                      }
	                  Float avg1=analyseService.calculateAverageTurnoverRateBeforeDate(5, date, stock.getCode());
	                  Float avg2=analyseService.calculateAverageTurnoverRateBeforeDate(10, date, stock.getCode());
                      data.setTurnOverRateUp(avg1-avg2);
	                  StockStage  stage=StockStageAnalysis.analyseStockStageByKLine(stock.getCode(),date, 5);
	                  if (stage==null||stage.getUpPower()<=stage.getDownPower()) {
                        continue;
                      }else {
                        data.setUpPower(stage.getUpPower());
                      }
	                  data.setStageName("ÉÏÕÇ½×¶Î");
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
}
