package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uf.store.dao.mysql.po.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{

}
