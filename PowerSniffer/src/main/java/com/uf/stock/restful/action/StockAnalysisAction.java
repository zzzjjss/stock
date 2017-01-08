package com.uf.stock.restful.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.k_analysis.AnalysisResult;
import com.uf.stock.k_analysis.StockStageAnalysis;
import com.uf.stock.restful.bean.PriceSpeedAnalysisResultData;
import com.uf.stock.restful.bean.PriceSpeedAnalysisResultResponse;
import com.uf.stock.restful.bean.ResponseError;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

@Singleton
@Path("/analysis")
public class StockAnalysisAction {
	private DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@GET
	@Path("/priceSpeedAnalysis")
	public String priceSpeedAnalysis(@QueryParam("minPeRatio") float minPeRatio, @QueryParam("maxPeRatio") float maxPeRation, @QueryParam("periodDays") int analysisDays) {
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

}
