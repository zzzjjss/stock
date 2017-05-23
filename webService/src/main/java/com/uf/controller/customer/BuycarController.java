package com.uf.controller.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uf.bean.Result;
import com.uf.entity.BuycarProductInfo;
import com.uf.entity.Customer;
import com.uf.entity.Product;
import com.uf.service.CustomerActionService;
import com.uf.service.ProductManageService;
import com.uf.util.GsonExcludeStrategy;

@Controller
public class BuycarController {
	@Autowired
	private CustomerActionService custermActionService;
	@Autowired
	private ProductManageService productManageService;

	@RequestMapping("/customer/auth/addProductToBuycar")
	@ResponseBody
	public String addProductToBuycar(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Gson gson = new Gson();
		Result result = new Result();
		String productId = allRequestParams.get("id");
		String count = allRequestParams.get("count");
		Customer customer = null;
		Object obj = request.getSession().getAttribute("customer");
		if (obj == null) {
			result.setResult("fail");
			result.setMes("请登录");
			return gson.toJson(result);
		} else {
			customer = (Customer) obj;
		}
		Product product = productManageService.findProductById(Integer.parseInt(productId));
		if (product == null) {
			result.setResult("fail");
			result.setMes("请选择产品");
			return gson.toJson(result);
		}
		BuycarProductInfo info = custermActionService.findCustomerBuycarProductInfoByProductId(product.getId(),
				customer.getId());
		if (info == null) {
			info = new BuycarProductInfo();
		}
		info.setCount(Integer.parseInt(count));
		info.setCustomer(customer);
		info.setProduct(product);
		custermActionService.saveProductToBuyCar(info);
		result.setResult("ok");
		return gson.toJson(result);
	}

	@RequestMapping("/customer/auth/removeProductFromBuycar")
	@ResponseBody
	public String removeProductFromBuycar(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		String id = allRequestParams.get("id");
		Gson gson = new Gson();
		Result result = new Result();
		Customer customer = null;
		Object obj = request.getSession().getAttribute("customer");
		if (obj == null) {
			result.setResult("fail");
			result.setMes("请登录");
			return gson.toJson(result);
		} else {
			customer = (Customer) obj;
		}
		custermActionService.removeProductFromBuycar(customer.getId(), Integer.parseInt(id));
		result.setResult("ok");
		return gson.toJson(result);
	}

	@RequestMapping("/customer/getBuycarInfo")
	@ResponseBody
	public String getBuycarInfo(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
		Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExcludeStrategy()).create();
		List<BuycarProductInfo> buycarInfo = new ArrayList<BuycarProductInfo>();
		Customer customer = (Customer) request.getSession().getAttribute("customer");
		if (customer == null) {
			return gson.toJson(buycarInfo);
		}
		buycarInfo = custermActionService.findCustomerBuycarProductInfo(customer.getId());
		return gson.toJson(buycarInfo);
	}
	@RequestMapping("/customer/generateOrder")
	@ResponseBody
	public String generateOrder(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
	    Result result=new Result();
	    try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
	    Gson gson=new Gson();
	    return gson.toJson(result);
	}
}
