package com.uf.store.restful.action.manager.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class SaveProductResponse extends RestfulResponse{
	private Integer productId;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
}
