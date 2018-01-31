package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uf.store.dao.mysql.po.OrderAddress;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, Long>{

}
