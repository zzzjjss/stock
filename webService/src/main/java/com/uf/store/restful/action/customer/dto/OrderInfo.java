package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

import com.uf.store.dao.mysql.po.Address;

public class OrderInfo {
	private List<ShopcarItemInfo> orderPreItem=new ArrayList<ShopcarItemInfo>();
	private Float totalMoney;
	private String status;
	private Address address;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

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
