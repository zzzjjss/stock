package com.uf.store.service.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.store.service.dao.OrderDao;
import com.uf.store.service.entity.Order;
@Component("orderDao")
public class OrderDaoImpl extends CommonDaoImpl<Order> implements OrderDao{

}
