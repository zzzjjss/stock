package com.uf.store.service.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.store.service.dao.CustomerDao;
import com.uf.store.service.entity.Customer;
@Component("customerDao")
public class CustomerDaoImpl extends CommonDaoImpl<Customer> implements CustomerDao {

}
