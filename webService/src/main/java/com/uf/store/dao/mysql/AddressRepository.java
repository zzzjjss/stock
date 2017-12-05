package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uf.store.dao.mysql.po.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{

}
