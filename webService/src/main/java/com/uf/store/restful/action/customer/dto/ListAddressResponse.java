package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

import com.uf.store.restful.dto.RestfulResponse;

public class ListAddressResponse extends RestfulResponse{
	private List<AddressInfo> addressInfos=new ArrayList<AddressInfo>();

	public List<AddressInfo> getAddressInfos() {
		return addressInfos;
	}

	public void setAddressInfos(List<AddressInfo> addressInfos) {
		this.addressInfos = addressInfos;
	}
	
}
