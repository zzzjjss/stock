package com.uf.store.restful.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uf.store.restful.dto.ListProductsRequest;
import com.uf.store.restful.dto.ListProductsResponse;
import com.uf.store.service.ProductManageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "the api opened for public ")
public class CommonAction {
	private Logger logger=LoggerFactory.getLogger(CommonAction.class);
	@Autowired
	private ProductManageService productManage;

	
	@RequestMapping(value = "listProducts", method = RequestMethod.POST)
	@ApiOperation(value = "list products by start index and length ", notes = "maybe need add priority field to product bean")
	public ListProductsResponse listProducts(@RequestBody ListProductsRequest request) {
		ListProductsResponse response=new ListProductsResponse();
		try {
			
		} catch (Exception e) {
			logger.error("",e);
		}
		return response;
	}

}
