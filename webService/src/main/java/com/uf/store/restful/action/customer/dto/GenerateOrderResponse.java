package com.uf.store.restful.action.customer.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class GenerateOrderResponse extends RestfulResponse{
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
