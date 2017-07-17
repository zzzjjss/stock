package com.uf.store.service;

import java.util.List;

import com.uf.store.service.entity.Address;

public interface AddressManageService {
	public void addAddress(Address address);
	public List<Address> listCustomerAddresses(Integer customeriD);
	public Address getAddressById(Integer addressId);
}
