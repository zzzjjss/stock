package com.uf.store.dao.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uf.store.dao.mysql.po.Address;
import com.uf.store.dao.mysql.po.Customer;

public interface AddressRepository extends JpaRepository<Address,Long>{
	@Query("from Address a where a.customer.id=:cid and a.isDefault=true")
	public Address findCustomerDefaultAddress(@Param("cid")Long customerId);
	public List<Address> findByCustomer(Customer customer);
}
