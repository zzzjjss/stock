package com.uf.controller.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.cache.ReadWriteCache.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uf.bean.GenerateOrderRequest;
import com.uf.bean.Result;
import com.uf.entity.Address;
import com.uf.entity.BuycarProductInfo;
import com.uf.entity.Customer;
import com.uf.entity.Order;
import com.uf.entity.OrderItem;
import com.uf.entity.Product;
import com.uf.service.AddressManageService;
import com.uf.service.CustomerActionService;
import com.uf.service.ProductManageService;
import com.uf.util.GsonExcludeStrategy;

@Controller
public class BuycarController {
	@Autowired
	private CustomerActionService custermActionService;
	@Autowired
	private ProductManageService productManageService;
	@Autowired
	private AddressManageService addressManageService;

	@RequestMapping("/customer/addProductToBuycar")
	@ResponseBody
	public String addProductToBuycar(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Gson gson = new Gson();
		Result result = new Result();
		try {
			String productId = allRequestParams.get("id");
			String count = allRequestParams.get("count");
			Customer customer = null;
			Object obj = request.getSession().getAttribute("customer");
			if (obj == null) {
				result.setResult(Result.RESULT_NO_LOGIN);
				result.setMes("请登录");
				return gson.toJson(result);
			} else {
				customer = (Customer) obj;
			}
			Product product = productManageService.findProductById(Integer.parseInt(productId));
			if (product == null) {
				result.setResult(Result.RESULT_FAIL);
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
			result.setResult(Result.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		return gson.toJson(result);
	}

	@RequestMapping("/customer/removeProductFromBuycar")
	@ResponseBody
	public String removeProductFromBuycar(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		Gson gson = new Gson();
		try {
			String id = allRequestParams.get("id");
			Customer customer = null;
			Object obj = request.getSession().getAttribute("customer");
			if (obj == null) {
				result.setResult(Result.RESULT_NO_LOGIN);
				result.setMes("请登录");
				return gson.toJson(result);
			} else {
				customer = (Customer) obj;
			}
			custermActionService.removeProductFromBuycar(customer.getId(), Integer.parseInt(id));
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		result.setResult(Result.RESULT_OK);
		return gson.toJson(result);
	}

	@RequestMapping("/customer/getBuycarInfo")
	@ResponseBody
	public String getBuycarInfo(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
		Result<List<BuycarProductInfo>> result = new Result<List<BuycarProductInfo>>();
		Gson gson = new GsonBuilder().setExclusionStrategies(new GsonExcludeStrategy()).create();
		try {
			List<BuycarProductInfo> buycarInfo = new ArrayList<BuycarProductInfo>();
			Object customer =  request.getSession().getAttribute("customer");
			if (customer == null) {
				result.setResult(Result.RESULT_OK);
				return gson.toJson(result);
			}
			buycarInfo = custermActionService.findCustomerBuycarProductInfo(((Customer)customer).getId());
			result.setResult(Result.RESULT_OK);
			result.setData(buycarInfo);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		return gson.toJson(result);
	}
	@RequestMapping("/customer/generateOrder")
	@ResponseBody
	public String generateOrder(@RequestBody String body, HttpServletRequest request) {
	    Result result=new Result();
	    try {
	    	Gson gson=new Gson();
			GenerateOrderRequest requestData=gson.fromJson(body, GenerateOrderRequest.class);
			if (request!=null&&requestData.getOrderItems()!=null&&requestData.getOrderItems().size()>0) {
				Order order=new Order();
				List<OrderItem>  orderItems=new ArrayList<OrderItem>();
				float totalAmount=0f;
				for(GenerateOrderRequest.GenerateOrderRequestDataItem item:requestData.getOrderItems()){
					OrderItem  orderItem=new OrderItem();
					Product product=productManageService.findProductById(item.getProductId());
					Address address=addressManageService.getAddressById(requestData.getAddressId());
					if (product!=null) {
						product.setId(item.getProductId());
						orderItem.setProduct(product);
						orderItem.setCount(item.getCount());
						orderItem.setPrice(product.getSellPrice());
						float amount=item.getCount()*product.getSellPrice();
						totalAmount=totalAmount+amount;
						orderItem.setAmount(amount);
						orderItems.add(orderItem);
					}
					order.setItems(orderItems);
					order.setTotalAmount(totalAmount);

					if (address!=null) {
						order.setAddress(address.getAddress());
						order.setArear(address.getArear());
						order.setCity(address.getCity());
						order.setCounty(address.getCounty());
						order.setProvince(address.getProvince());
						order.setPhoneNumber(address.getPhoneNumber());
						order.setRealName(address.getRealName());
					}
				}
				order.setStatus(Order.STATUS_NOPAY);
				custermActionService.generateOrder(order);
				result.setResult(Result.RESULT_OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
	    Gson gson=new Gson();
	    return gson.toJson(result);
	}
}
