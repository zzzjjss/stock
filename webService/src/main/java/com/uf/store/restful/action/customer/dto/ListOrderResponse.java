package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

import com.uf.store.restful.dto.RestfulResponse;

public class ListOrderResponse extends RestfulResponse{
	List<OrderInfo> orders=new ArrayList<OrderInfo>();

	public List<OrderInfo> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderInfo> orders) {
		this.orders = orders;
	}
	
}
