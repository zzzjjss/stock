package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

import com.uf.store.restful.dto.RestfulResponse;

public class GotoGenerateOrderResponse extends RestfulResponse{
	private List<ShopcarItemInfo> orderPreItem=new ArrayList<ShopcarItemInfo>();
	private Float totalMoney;
	

	public Float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<ShopcarItemInfo> getOrderPreItem() {
		return orderPreItem;
	}

	public void setOrderPreItem(List<ShopcarItemInfo> orderPreItem) {
		this.orderPreItem = orderPreItem;
	}
	
}
