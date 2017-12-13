package com.uf.store.restful.action.customer.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class GetDefaultAddressResponse extends RestfulResponse{
	private AddressInfo addressInfo;

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
	
}
