package com.uf.store.restful.action.customer.dto;

public class GenerateOrderRequestItem {
	private Long productId;
	private int amount;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
