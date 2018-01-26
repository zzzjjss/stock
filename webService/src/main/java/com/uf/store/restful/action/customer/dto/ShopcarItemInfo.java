package com.uf.store.restful.action.customer.dto;

public class ShopcarItemInfo {
	private Long id;
	private Long productId;
	private String name;
	private String productSnapshotImgUrl;
	private String description;
	private int amount;
	private Float sellPrice;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
