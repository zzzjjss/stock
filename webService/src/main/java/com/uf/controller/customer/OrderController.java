package com.uf.controller.customer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uf.bean.Result;
import com.uf.entity.Customer;
import com.uf.entity.Order;
import com.uf.service.CustomerActionService;
import com.uf.service.ProductManageService;

@Controller
public class OrderController {
	@Autowired
	private CustomerActionService custermActionService;
	@Autowired
	private ProductManageService productManageService;

	@RequestMapping("/customer/listOrders")
	@ResponseBody
	public String listCustomerOrders(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request){
		Result<List<Order>> result = new Result<List<Order>>();
		Gson gson = new GsonBuilder().create();
		try {
			Object customer =  request.getSession().getAttribute("customer");
			if (customer == null) {
				result.setResult(Result.RESULT_OK);
				return gson.toJson(result);
			}
			Customer customer2=(Customer)customer;
			List<Order> orders= custermActionService.listOrders(customer2.getId());
			result.setResult(Result.RESULT_OK);
			result.setData(orders);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		return gson.toJson(result);
	}
	@RequestMapping("/customer/deleteOrder")
	@ResponseBody
	public String deleteOrder(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request){
		Result result = new Result();
		Gson gson = new Gson();
		try {
			Object customer =  request.getSession().getAttribute("customer");
			if (customer == null) {
				result.setResult(Result.RESULT_OK);
				return gson.toJson(result);
			}
			Customer customer2=(Customer)customer;
			custermActionService.deleteOrder(Integer.parseInt(allRequestParams.get("orderId")),customer2.getId());
			result.setResult(Result.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		return gson.toJson(result);
	}
	@RequestMapping("/customer/payForOrder")
	@ResponseBody
	public String payForOrder(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request){
		Result<PayInfo> result = new Result<PayInfo>();
		Gson gson = new Gson();
		try {
			Object customer =  request.getSession().getAttribute("customer");
			if (customer == null) {
				result.setResult(Result.RESULT_OK);
				return gson.toJson(result);
			}
			result.setResult(Result.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		return gson.toJson(result);
	}

class PayInfo{
	String payUrl;

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
}
}
