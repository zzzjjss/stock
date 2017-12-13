package com.uf.store.restful.action.customer.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class GetShopcarItemInfoResponse extends RestfulResponse{
	private ShopcarItemInfo  itemInfo;

	public ShopcarItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ShopcarItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}
}
