package com.uf.store.service;

import org.junit.Test;

import com.uf.store.service.entity.Product;
import com.uf.store.util.SpringBeanFactory;

public class ProductManageServiceTest {
	private ProductManageService service=SpringBeanFactory.getBean(ProductManageService.class);

	@Test
	public void test_addProduct() {
		Product product=new Product();
		product.setBuyPrice(11.0f);
		product.setDescription("description");
		product.setName("heloo");
		product.setSearchKeywords("hi");
		product.setSellPrice(11.3f);
		service.addProduct(product,null);
	}

}
