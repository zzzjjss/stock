package com.uf.store.restful.action.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.uf.store.restful.action.customer.dto.AddProductToShopCarRequest;
import com.uf.store.restful.action.customer.dto.GenerateOrderRequest;
import com.uf.store.restful.action.customer.dto.GenerateOrderResponse;
import com.uf.store.restful.action.customer.dto.GotoGenerateOrderRequest;
import com.uf.store.restful.action.customer.dto.GotoGenerateOrderResponse;
import com.uf.store.restful.action.customer.dto.ListShopcarItemsResponse;
import com.uf.store.restful.dto.RestfulResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;

@RestController
@RequestMapping("customer")
public class ShoppingAction {
	private Logger logger=LoggerFactory.getLogger(ShoppingAction.class);
	@RequestMapping(value = "saveProductToShopCar", method = RequestMethod.POST)
	public RestfulResponse saveProductToShopCar(@RequestBody AddProductToShopCarRequest request,@RequestHeader(value="Authorization") String token) {
		RestfulResponse response=new RestfulResponse();
		try {
						
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "deleteItemFromShopcar", method = RequestMethod.GET)
	public RestfulResponse deleteItemFromShopcar(@RequestParam(value="itemId") Long shopCarItemId,@RequestHeader(value="Authorization") String token) {
		RestfulResponse response=new RestfulResponse();
		try {
			//must  verify the shopcar item is owned by  the  customer
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "listShopcarItems", method = RequestMethod.GET)
	public ListShopcarItemsResponse listShopcarItems(@RequestHeader(value="Authorization") String token) {
		ListShopcarItemsResponse  response=new ListShopcarItemsResponse();
		try {
			//find  Customer from cache, and return the customer's shopcar
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value = "gotoGenerateOrder", method = RequestMethod.POST)
	public GotoGenerateOrderResponse gotoGenerateOrder(@RequestBody GotoGenerateOrderRequest request,@RequestHeader(value="Authorization") String token) {
		GotoGenerateOrderResponse   response=new GotoGenerateOrderResponse();
		try {
			//generate the order total infor   by  calculating  the  price  in some rule 
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	

	@RequestMapping(value = "generateOrder", method = RequestMethod.POST)
	public GenerateOrderResponse generateOrder(@RequestBody GenerateOrderRequest request,@RequestHeader(value="Authorization") String token) {
		GenerateOrderResponse response=new GenerateOrderResponse();
		try {
			//generate the order total infor   by  calculating  the  price  in some rule 
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}	
	
}
