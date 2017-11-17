package com.uf.store.dao.mysql.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@ManyToOne
	private Product product;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=10000000)
	private byte[] imageContent;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public byte[] getImageContent() {
		return imageContent;
	}
	public void setImageContent(byte[] imageContent) {
		this.imageContent = imageContent;
	}
	
}
