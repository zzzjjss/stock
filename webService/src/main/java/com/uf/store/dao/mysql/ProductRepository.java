package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uf.store.dao.mysql.po.Product;

public interface ProductRepository  extends JpaRepository<Product, Long> {
	public void deleteById(Integer id);
}
