package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.uf.store.dao.mysql.po.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	@Modifying
	@Query("delete from ProductImage pi where pi.product.id =?")
	public void deleteByProductId(Integer productId);
}
