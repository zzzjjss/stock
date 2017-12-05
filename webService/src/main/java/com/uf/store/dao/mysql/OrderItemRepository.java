package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uf.store.dao.mysql.po.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{

}
