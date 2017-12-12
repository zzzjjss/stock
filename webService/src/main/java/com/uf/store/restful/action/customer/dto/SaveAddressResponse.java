package com.uf.store.restful.action.customer.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class SaveAddressResponse extends RestfulResponse{
	private Long addressId;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
}
