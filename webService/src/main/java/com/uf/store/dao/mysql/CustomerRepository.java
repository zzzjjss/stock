package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uf.store.dao.mysql.po.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long>{
	public Customer findTopByUnionid(String unionid);
	public Customer findTopByOpenid(String openId);
}
