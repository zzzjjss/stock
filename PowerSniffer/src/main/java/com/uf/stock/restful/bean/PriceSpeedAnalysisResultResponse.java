package com.uf.stock.restful.bean;

import java.util.List;

public class PriceSpeedAnalysisResultResponse extends RestfulResponse{
	private List<PriceSpeedAnalysisResultData> data;

	public List<PriceSpeedAnalysisResultData> getData() {
		return data;
	}

	public void setData(List<PriceSpeedAnalysisResultData> data) {
		this.data = data;
	}
	
}
