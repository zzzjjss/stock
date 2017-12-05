package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

import com.uf.store.dao.mysql.po.Address;
import com.uf.store.restful.dto.RestfulResponse;

public class GotoGenerateOrderResponse extends RestfulResponse{
	private List<ShopcarItem> orderPreItem=new ArrayList<ShopcarItem>();
	private Float totalMoney;
	private Address defautAddress;
	
	public Address getDefautAddress() {
		return defautAddress;
	}

	public void setDefautAddress(Address defautAddress) {
		this.defautAddress = defautAddress;
	}

	public Float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<ShopcarItem> getOrderPreItem() {
		return orderPreItem;
	}

	public void setOrderPreItem(List<ShopcarItem> orderPreItem) {
		this.orderPreItem = orderPreItem;
	}
	
}
