package com.uf.store.dao.mysql;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.uf.store.dao.mysql.po.OrderItem;

public interface OrderItemRepository extends PagingAndSortingRepository<OrderItem, Long>{
	@Query("from OrderItem i where i.product.id=:pid")
	public List<OrderItem> findPagedByProductId(@Param("pid")long productId,Pageable pageable);
	@Modifying
	@Query("delete from OrderItem o where o.order.id=:oid")
	public int deleteOrderItems(@Param("oid")long orderId);

}
