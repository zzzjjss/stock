package com.uf.store.restful.action.customer.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {
	private List<ShopcarItemInfo> orderItemsInfo=new ArrayList<ShopcarItemInfo>();
	private Float totalMoney;
	private String statusString;
	private String status;
	private String orderNumber;
	private AddressInfo address;
	private Long id;
	
	
	

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}

	public Float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<ShopcarItemInfo> getOrderItemsInfo() {
		return orderItemsInfo;
	}

	public void setOrderItemsInfo(List<ShopcarItemInfo> orderItemsInfo) {
		this.orderItemsInfo = orderItemsInfo;
	}

	


}
