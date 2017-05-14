package com.uf.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.dao.CustomerDao;
import com.uf.entity.Customer;
@Component("customerDao")
public class CustomerDaoImpl extends CommonDaoImpl<Customer> implements CustomerDao {

}
