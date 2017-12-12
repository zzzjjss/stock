package com.uf.store.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uf.store.dao.mysql.AddressRepository;
import com.uf.store.dao.mysql.CustomerRepository;
import com.uf.store.dao.mysql.po.Address;
import com.uf.store.dao.mysql.po.Customer;
import com.uf.store.service.cache.CacheService;
import com.uf.wechat.WechatAppApi;
import com.uf.wechat.bean.JsCode2SessionResponse;
import com.uf.wechat.bean.WechatAppConfig;

@Service
@Transactional
public class CustomerAccountService {
	@Value("wechatApp.appId")
	private String appId;
	@Value("wechatApp.appSecret")
	private String appSecret;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AddressRepository addressRepository;
	private WechatAppApi wechatAppApi=new WechatAppApi(new WechatAppConfig(appId, appSecret));
	
	public String wechatLoginGenerateToken(String code) {
		JsCode2SessionResponse response=wechatAppApi.jsCode2SessionResponse(code);
		if (response!=null) {
			Customer customer=customerRepository.findTopByUnionid(response.getUnionid());
			if (customer==null) {
				customer=new Customer();
				customer.setOpenid(response.getOpenid());
				customer.setUnionid(response.getUnionid());
				customerRepository.save(customer);
			}
			String token=UUID.randomUUID().toString();
			cacheService.putObject(token,customer);
			return token;
		}
		return null;
	}	
	public Customer findByUuid(String uuid) {
		return customerRepository.findTopByUnionid(uuid);
	}
	public void saveCustomerAddress(Address address,Customer customer) {
		address.setCustomer(customer);
		addressRepository.save(address);
	}
	public void deleteCustomerAddress(Long addressId,Long customerId) {
		addressRepository.deleteCustomerAddressById(addressId, customerId);
	}
	
	
	
	
}
