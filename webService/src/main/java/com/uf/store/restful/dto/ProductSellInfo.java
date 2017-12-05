package com.uf.store.restful.dto;

import java.util.List;

public class ProductSellInfo {
	private Long id;
	private String name;
	private String description;
	private Float sellPrice;
	private String snapshotImgUrl;
	private List<String> imgUrls;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Float getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(Float sellPrice) {
		this.sellPrice = sellPrice;
	}
	public List<String> getImgUrls() {
		return imgUrls;
	}
	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}
	public String getSnapshotImgUrl() {
		return snapshotImgUrl;
	}
	public void setSnapshotImgUrl(String snapshotImgUrl) {
		this.snapshotImgUrl = snapshotImgUrl;
	}
}
