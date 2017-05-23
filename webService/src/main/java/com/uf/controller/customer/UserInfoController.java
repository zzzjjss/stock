package com.uf.controller.customer;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.uf.bean.Result;
import com.uf.entity.Customer;
import com.uf.service.CustomerActionService;
import com.uf.wechat.WechatApi;
import com.uf.wechat.bean.WechatUserInfo;

@Controller
public class UserInfoController {
	@Autowired
	private CustomerActionService custermActionService;
	private Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@RequestMapping("/customer/wechatLogin")
	@ResponseBody
	public String wechatLogin(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		try {
			String code = allRequestParams.get("code");
			if (!Strings.isNullOrEmpty(code)) {
				WechatUserInfo userInfo = WechatApi.getWechatUserInfo(code);
				if (userInfo != null) {
					Customer customer = custermActionService.findCustomerByWechatId(userInfo.getUnionid());
					if (customer == null) {
						customer = new Customer();
						customer.setWechatId(userInfo.getUnionid());
						customer.setWechatNickname(userInfo.getNickname());
						customer.setWechatOpenId(userInfo.getOpenid());
						custermActionService.saveOrUpdateCustomer(customer);
					}
					if (request.getSession().getAttribute("customer") == null) {
						request.getSession().setAttribute("customer", customer);
					}
					result.setResult(Result.RESULT_OK);
				} else {
					result.setResult(Result.RESULT_FAIL);
					result.setMes("wechat login fail");
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}

	@RequestMapping("/customer/saveAddress")
	@ResponseBody
	public String saveAddress(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		Gson gson = new Gson();
		return gson.toJson(result);

	}

	@RequestMapping("/customer/getAddressList")
	@ResponseBody
	public String getAddressList(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		Gson gson = new Gson();
		return gson.toJson(result);

	}

	@RequestMapping("/customer/deleteAddress")
	@ResponseBody
	public String deleteAddress(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}

	@RequestMapping("/customer/getOrderList")
	@ResponseBody
	public String getOrderList(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}

	@RequestMapping("/customer/deleteOrder")
	@ResponseBody
	public String deleteOrder(@RequestParam Map<String, String> allRequestParams, Model model,
			HttpServletRequest request) {
		Result result = new Result();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.RESULT_FAIL);
			result.setMes(e.getMessage());
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}

}
