package com.uf.store.restful.action.manager.dto;

import java.util.List;

import com.uf.store.dao.mysql.po.Product;

public class SaveProductRequest  {
	private Product product;
	private List<String> imageNames;

	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public List<String> getImageNames() {
		return imageNames;
	}
	public void setImageNames(List<String> imageNames) {
		this.imageNames = imageNames;
	}
	
}
