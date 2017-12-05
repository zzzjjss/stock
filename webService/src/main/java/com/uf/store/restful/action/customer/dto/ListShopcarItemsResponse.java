package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;
import com.uf.store.restful.dto.RestfulResponse;


public class ListShopcarItemsResponse extends RestfulResponse{
	private List<ShopcarItem> items=new ArrayList<ShopcarItem>();

	public List<ShopcarItem> getItems() {
		return items;
	}

	public void setItems(List<ShopcarItem> items) {
		this.items = items;
	}
	
}
