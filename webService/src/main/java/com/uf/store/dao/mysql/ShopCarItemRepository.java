package com.uf.store.dao.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uf.store.dao.mysql.po.ShopCarItem;

public interface ShopCarItemRepository extends JpaRepository<ShopCarItem,Long> {
	@Query("select i from ShopCarItem i where i.product.id=:pid and i.customer.id=:cid")
	public ShopCarItem findTopByProductAndCustomer(@Param("pid") Long productId,@Param("cid")Long customerId);
	@Modifying
	@Query("delete from ShopCarItem sc where sc.id=:id and sc.customer.id =:cid")
	public void deleteCustomerShopcarItem(@Param("id")Long itemId,@Param("cid")Long customerId);
	@Modifying
	@Query("delete from ShopCarItem sc where sc.product.id=:pId and sc.customer.id =:cid")
	public void deleteCustomerShopcarItemByProduct(@Param("pId")Long productId,@Param("cid")Long customerId);
	@Query("select i from ShopCarItem i where i.customer.id=:cid")
	public List<ShopCarItem> findByCustomer(@Param("cid")Long cusotmerId);
	@Query("select sum(amount) from ShopCarItem i where i.customer.id=:cid")
	public Long countByCustomer(@Param("cid")Long cusotmerId);
}
