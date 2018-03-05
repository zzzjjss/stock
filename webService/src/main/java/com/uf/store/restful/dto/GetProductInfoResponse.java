package com.uf.store.restful.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetProductInfoResponse extends RestfulResponse {
	private ProductSellInfo productSellInfo;
	private Map<String, String> productProp=new HashMap<String,String>(); 
	private List<ProductOrderInfo> orderInfos=new ArrayList<ProductOrderInfo>();
	public ProductSellInfo getProductSellInfo() {
		return productSellInfo;
	}
	public void setProductSellInfo(ProductSellInfo productSellInfo) {
		this.productSellInfo = productSellInfo;
	}
	public List<ProductOrderInfo> getOrderInfos() {
		return orderInfos;
	}
	public void setOrderInfos(List<ProductOrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	public Map<String, String> getProductProp() {
		return productProp;
	}
	public void setProductProp(Map<String, String> productProp) {
		this.productProp = productProp;
	}
}
