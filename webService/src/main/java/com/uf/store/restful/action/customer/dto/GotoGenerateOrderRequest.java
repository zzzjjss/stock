package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

public class GotoGenerateOrderRequest {
	private List<GenerateOrderRequestItem> orderItems=new ArrayList<GenerateOrderRequestItem>();

	public List<GenerateOrderRequestItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<GenerateOrderRequestItem> orderItems) {
		this.orderItems = orderItems;
	}
	
}
