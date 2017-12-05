package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

public class GenerateOrderRequest {
	private List<GenerateOrderRequestItem> orderItems=new ArrayList<GenerateOrderRequestItem>();
	private Long addressId;
	
	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public List<GenerateOrderRequestItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<GenerateOrderRequestItem> orderItems) {
		this.orderItems = orderItems;
	}

}
