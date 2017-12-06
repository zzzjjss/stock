package com.uf.store.restful.action.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.uf.store.restful.action.manager.dto.ListPagedOrdersRequest;
import com.uf.store.restful.action.manager.dto.ListPagedOrdersResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;

@RestController
@RequestMapping("manager")
public class OrderManageAction {
	private Logger logger=LoggerFactory.getLogger(OrderManageAction.class);
	@RequestMapping(value="listPagedOrders",method=RequestMethod.POST)
	public  ListPagedOrdersResponse listPagedOrders(@RequestBody ListPagedOrdersRequest request) {
		 ListPagedOrdersResponse  response=new ListPagedOrdersResponse();
		try {
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;	
	}

}
