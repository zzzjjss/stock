package com.uf.store.restful.dto;

import java.util.List;

public class GetProductInfoResponse extends RestfulResponse {
	private ProductSellInfo productSellInfo;
	private List<ProductOrderInfo> orderInfos;
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
	
}
