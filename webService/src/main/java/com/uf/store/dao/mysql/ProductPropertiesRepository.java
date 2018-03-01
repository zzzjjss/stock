package com.uf.store.dao.mysql;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ProductProperties;

public interface ProductPropertiesRepository extends JpaRepository<ProductProperties,Long> {
	public List<ProductProperties> findByProduct(Product product);
	@Modifying
	@Query("delete from ProductProperties pi where pi.product.id =?")
	public void deleteByProductId(Long productId);
}
