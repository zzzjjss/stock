package com.uf.store.restful.action.manager.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class SaveProductResponse extends RestfulResponse{
	private Long productId;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
}
