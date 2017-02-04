package com.uf.stock.restful.bean;

import java.util.List;

public class StockStageAnalysisResultResponse extends RestfulResponse{
    private List<StockStageAnalysisResultData> data;

    public List<StockStageAnalysisResultData> getData() {
        return data;
    }

    public void setData(List<StockStageAnalysisResultData> data) {
        this.data = data;
    }
}
