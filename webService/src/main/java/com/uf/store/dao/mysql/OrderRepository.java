package com.uf.store.dao.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uf.store.dao.mysql.po.Order;
import com.uf.store.dao.mysql.po.OrderStatus;

public interface OrderRepository extends JpaRepository<Order,Long>{
	@Query("from Order o where  o.customer.id=:cId and o.status=:sta")
	public List<Order> findByCustomerAndStauts(@Param("cId")Long customerId,@Param("sta")OrderStatus status);
	@Modifying
	@Query("update Order o set o.status=:s where  o.id=:oid and o.customer.id=:cid")
	public void updateOrderStatus(@Param("s")OrderStatus status,@Param("oid")Long orderId,@Param("cid")Long customerId);
}
