package com.uf.store.restful.action.customer.dto;

public class ShopcarItem {
	private Long productId;
	private String productSnapshotImgUrl;
	private String description;
	private int amount;
	private Float sellPrice;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductSnapshotImgUrl() {
		return productSnapshotImgUrl;
	}
	public void setProductSnapshotImgUrl(String productSnapshotImgUrl) {
		this.productSnapshotImgUrl = productSnapshotImgUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Float getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(Float sellPrice) {
		this.sellPrice = sellPrice;
	}
	
}
