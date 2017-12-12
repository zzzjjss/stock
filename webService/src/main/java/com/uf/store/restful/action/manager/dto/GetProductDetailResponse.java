package com.uf.store.restful.action.manager.dto;

import com.uf.store.restful.dto.ProductSellInfo;
import com.uf.store.restful.dto.RestfulResponse;

public class GetProductDetailResponse extends RestfulResponse{
	private ProductSellInfo  productInfo;
	private String searchKeywords;
	private Float buyPrice;
	private boolean onLine;
	
	public boolean isOnLine() {
		return onLine;
	}
	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}
	public ProductSellInfo getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(ProductSellInfo productInfo) {
		this.productInfo = productInfo;
	}
	public String getSearchKeywords() {
		return searchKeywords;
	}
	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}
	public Float getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(Float buyPrice) {
		this.buyPrice = buyPrice;
	}
	
	
}
