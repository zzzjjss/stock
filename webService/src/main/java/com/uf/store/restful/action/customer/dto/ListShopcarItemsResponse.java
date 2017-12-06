package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;
import com.uf.store.restful.dto.RestfulResponse;


public class ListShopcarItemsResponse extends RestfulResponse{
	private List<ShopcarItemInfo> items=new ArrayList<ShopcarItemInfo>();

	public List<ShopcarItemInfo> getItems() {
		return items;
	}

	public void setItems(List<ShopcarItemInfo> items) {
		this.items = items;
	}
	
}
