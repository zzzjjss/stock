package com.uf.store.restful.action.customer;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uf.store.dao.mysql.po.Address;
import com.uf.store.dao.mysql.po.Customer;
import com.uf.store.restful.action.customer.dto.AddressInfo;
import com.uf.store.restful.action.customer.dto.GetDefaultAddressResponse;
import com.uf.store.restful.action.customer.dto.ListAddressResponse;
import com.uf.store.restful.action.customer.dto.SaveAddressRequest;
import com.uf.store.restful.action.customer.dto.SaveAddressResponse;
import com.uf.store.restful.action.customer.dto.WechatLoginRequest;
import com.uf.store.restful.action.manager.dto.LoginResponse;
import com.uf.store.restful.dto.RestfulResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;
import com.uf.store.service.CustomerAccountService;
import com.uf.store.service.cache.CacheService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("customer")
public class AccountAction {
	private Logger logger=LoggerFactory.getLogger(AccountAction.class);
	@Autowired
	private CustomerAccountService accountService;
	@Autowired
	private CacheService cacheService;	
	@RequestMapping(value = "loginWithWechat", method = RequestMethod.GET)
	@ApiOperation(value = "customer login ", notes = "customer login with  wechat")
	public void  loginWithWechat(HttpServletRequest request,HttpServletResponse response) {
        String code = request.getParameter("code");
		try {
			String token=accountService.wechatLoginGenerateToken(code);
			String responseScript="<script>window.location='/weui/view/index.html?appSessionId="+token+"'; </script>";
			response.getWriter().write(responseScript);
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public LoginResponse login() {
		LoginResponse response=new LoginResponse();
		try {
			Customer customer=accountService.findByUuid("uuid");
			String token=UUID.randomUUID().toString();
			cacheService.putObject(token,customer);
			if (token!=null) {
				response.setToken(token);
				response.setResultCode(ResultCode.OK);
			}else {
				response.setResultCode(ResultCode.FAIL);
				response.setMes("wechat login fail");
			}
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}

	
	@RequestMapping(value = "saveAddress", method = RequestMethod.POST)
	public SaveAddressResponse saveAddress(@RequestBody SaveAddressRequest request,@RequestHeader(value="Authorization") String token) {
		SaveAddressResponse response=new SaveAddressResponse();
		try {
			Customer customer=(Customer)cacheService.getCachedObject(token);
			if(request!=null) {
				Address address=new Address();
				address.setAddressDetail(request.getAddressDetail());
				address.setCity(request.getCity());
				address.setCustomer(customer);
				address.setDefault(request.isDefaultAddress());
				address.setArea(request.getArea());
				address.setId(request.getId());
				address.setName(request.getReceiverName());
				address.setPhone(request.getPhone());
				address.setProvince(request.getProvince());
				accountService.saveCustomerAddress(address, customer);
				response.setAddressId(address.getId());
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "deleteAddress", method = RequestMethod.GET)
	public RestfulResponse deleteAddress(String addressId,@RequestHeader(value="Authorization") String token) {
		RestfulResponse response=new RestfulResponse();
		try {
			if (!NumberUtils.isDigits(addressId)) {
				response.setMes("addressId is wrong");
				response.setResultCode(ResultCode.FAIL);
				return response;
			}
			Customer customer=(Customer)cacheService.getCachedObject(token);
			accountService.deleteCustomerAddress(Long.parseLong(addressId), customer.getId());
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		
		return response;
	}
	@RequestMapping(value = "getDefaultAddress", method = RequestMethod.GET)
	public GetDefaultAddressResponse getDefaultAddress(@RequestHeader(value="Authorization") String token) {
		GetDefaultAddressResponse  response=new GetDefaultAddressResponse();
		try {
			Customer customer=(Customer)cacheService.getCachedObject(token);
			Address address=accountService.findCustomerDefaultAddress(customer);
			if (address!=null) {
				response.setAddressInfo(swap(address));
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "getAddressById", method = RequestMethod.GET)
	public GetDefaultAddressResponse getAddress(String addressId,@RequestHeader(value="Authorization") String token) {
		GetDefaultAddressResponse  response=new GetDefaultAddressResponse();
		try {
			Customer customer=(Customer)cacheService.getCachedObject(token);
			Address address=accountService.findCustomerAddress(customer, Long.valueOf(addressId));
			if (address!=null) {
				response.setAddressInfo(swap(address));
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}

	
	@RequestMapping(value = "setDefautAddress", method = RequestMethod.GET)
	public RestfulResponse setDefaultAddress(@RequestParam(value="id")Long defaultAddressId,@RequestHeader(value="Authorization") String token) {
		RestfulResponse response=new RestfulResponse();
		try {
			Customer customer=(Customer)cacheService.getCachedObject(token);
			accountService.setAddressToDefault(defaultAddressId, customer);
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	
	
	@RequestMapping(value = "listAddress", method = RequestMethod.GET)
	public ListAddressResponse listAddress(@RequestHeader(value="Authorization") String token) {
		ListAddressResponse response=new ListAddressResponse();
		try {
			Customer customer=(Customer)cacheService.getCachedObject(token);
			List<Address> address=accountService.listCustomerAddress(customer);
			if (address!=null) {
				for(Address a:address) {
					response.getAddressInfos().add(swap(a));
				}
			}
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	
	
private AddressInfo  swap(Address address) {
	AddressInfo info=new AddressInfo();
	info.setAddressDetail(address.getAddressDetail());
	info.setArea(address.getArea());
	info.setCity(address.getCity());
	info.setDefault(address.isDefault());
	info.setId(address.getId());
	info.setName(address.getName());
	info.setPhone(address.getPhone());
	info.setProvince(address.getProvince());
	return info;
}
}
