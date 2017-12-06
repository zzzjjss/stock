package com.uf.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uf.store.dao.mysql.ShopCarItemRepository;
import com.uf.store.dao.mysql.po.Customer;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ShopCarItem;

@Transactional
@Service
public class ShoppingService {
	@Autowired
	private ShopCarItemRepository shopCarItemRepository;

	public void saveProductToShopCar(Long productId, int amount, Customer customer) {
		ShopCarItem scit = shopCarItemRepository.findTopByProductAndCustomer(productId, customer.getId());
		if (scit != null) {
			scit.setAmount(scit.getAmount() + amount);
		} else {
			scit = new ShopCarItem();
			scit.setAmount(amount);
			scit.setCustomer(customer);
			Product product = new Product();
			product.setId(productId);
			scit.setProduct(product);
		}
		shopCarItemRepository.save(scit);
	}

	public void removeShopcarItem(Long itemId, Customer customer) {
		shopCarItemRepository.deleteCustomerShopcarItem(itemId, customer.getId());
	}
}
