package com.uf.store.restful.action.manager.dto;

import java.util.ArrayList;
import java.util.List;

import com.uf.store.restful.dto.RestfulResponse;

public class ListPagedOrdersResponse extends RestfulResponse {
	private List<OrderItemInfo> orderInfos=new ArrayList<OrderItemInfo>();
	private String receiverName;
	private String receiveAddress;
	public List<OrderItemInfo> getOrderInfos() {
		return orderInfos;
	}
	public void setOrderInfos(List<OrderItemInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	
}
