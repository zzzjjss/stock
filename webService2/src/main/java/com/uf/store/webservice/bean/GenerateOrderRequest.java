package com.uf.store.webservice.bean;

import java.util.ArrayList;
import java.util.List;

public class GenerateOrderRequest {
	private List<GenerateOrderRequestDataItem> orderItems = new ArrayList<GenerateOrderRequestDataItem>();
	private Integer addressId;
	public List<GenerateOrderRequestDataItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<GenerateOrderRequestDataItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public static class GenerateOrderRequestDataItem{
		private Integer productId;
		private Integer count;
		public Integer getProductId() {
			return productId;
		}
		public void setProductId(Integer productId) {
			this.productId = productId;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
	}
}
