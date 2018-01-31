package com.uf.store.restful.action.customer.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class GetShopcartItemAmountResponse extends RestfulResponse {
	private long itemNumber;

	public long getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(long itemNumber) {
		this.itemNumber = itemNumber;
	}
	

}
