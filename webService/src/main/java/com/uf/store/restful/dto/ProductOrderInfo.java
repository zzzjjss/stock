package com.uf.store.restful.dto;

public class ProductOrderInfo {
	private String customerName;
	private int buyProductNumber;
	private String evaluate;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getBuyProductNumber() {
		return buyProductNumber;
	}
	public void setBuyProductNumber(int buyProductNumber) {
		this.buyProductNumber = buyProductNumber;
	}
	public String getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	
}
