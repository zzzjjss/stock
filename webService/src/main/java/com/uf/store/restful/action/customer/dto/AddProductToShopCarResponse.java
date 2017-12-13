package com.uf.store.restful.action.customer.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class AddProductToShopCarResponse extends RestfulResponse{
	private Long shopcarItemId;

	public Long getShopcarItemId() {
		return shopcarItemId;
	}

	public void setShopcarItemId(Long shopcarItemId) {
		this.shopcarItemId = shopcarItemId;
	}
	
}
