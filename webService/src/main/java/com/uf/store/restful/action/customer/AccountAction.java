package com.uf.store.restful.action.customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uf.store.restful.action.customer.dto.SaveAddressRequest;
import com.uf.store.restful.action.customer.dto.WechatLoginRequest;
import com.uf.store.restful.action.manager.dto.LoginResponse;
import com.uf.store.restful.dto.RestfulResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;
import com.uf.store.service.CustomerAccountService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("customer")
public class AccountAction {
	private Logger logger=LoggerFactory.getLogger(AccountAction.class);
	@Autowired
	private CustomerAccountService accountService;
	
	@RequestMapping(value = "loginWithWechat", method = RequestMethod.POST)
	@ApiOperation(value = "customer login ", notes = "customer login with  wechat")
	public LoginResponse loginWithWechat(@RequestBody WechatLoginRequest request) {
		LoginResponse response=new LoginResponse();
		try {
			String token=accountService.wechatLoginGenerateToken(request.getCode());
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
	public RestfulResponse saveAddress(@RequestBody SaveAddressRequest request) {
		RestfulResponse response=new RestfulResponse();
		try {
			
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "deleteAddress", method = RequestMethod.GET)
	public RestfulResponse addAddress(String addressId) {
		RestfulResponse response=new RestfulResponse();
		try {
			
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	
}
