package com.uf.store.restful;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uf.store.Application;
import com.uf.store.dao.mysql.po.OrderStatus;
import com.uf.store.restful.action.customer.dto.AddProductToShopCarRequest;
import com.uf.store.restful.action.customer.dto.AddProductToShopCarResponse;
import com.uf.store.restful.action.customer.dto.GenerateOrderRequest;
import com.uf.store.restful.action.customer.dto.GenerateOrderRequestItem;
import com.uf.store.restful.action.customer.dto.GenerateOrderResponse;
import com.uf.store.restful.action.customer.dto.GetShopcarItemInfoResponse;
import com.uf.store.restful.action.customer.dto.GotoGenerateOrderRequest;
import com.uf.store.restful.action.customer.dto.GotoGenerateOrderResponse;
import com.uf.store.restful.action.customer.dto.ListOrderResponse;
import com.uf.store.restful.action.customer.dto.ListShopcarItemsResponse;
import com.uf.store.restful.action.customer.dto.ShopcarItemInfo;
import com.uf.store.restful.action.manager.dto.LoginResponse;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CustomerShoppingActionTest {
	private MockMvc mockMvc;
	private String token;
	private Long pruductId=32L;
	private Long addressId=7L;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private  ObjectMapper mapper = new ObjectMapper();
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		String content=mockMvc.perform(MockMvcRequestBuilders.get("/customer/login")) .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println(content);
		LoginResponse response=mapper.readValue(content, LoginResponse.class);
		token=response.getToken();
	}

	@Test
	public void testShopCar()throws Exception {
		AddProductToShopCarRequest request=new AddProductToShopCarRequest();
		request.setAmount(3);
		request.setProductId(pruductId);
		String addToShopCarResponse=mockMvc.perform(MockMvcRequestBuilders.post("/customer/saveProductToShopCar").content(mapper.writeValueAsString(request).getBytes()).header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("add to shop car response:"+addToShopCarResponse);
		AddProductToShopCarResponse addResponse=mapper.readValue(addToShopCarResponse, AddProductToShopCarResponse.class);
		String getInfoResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/getShopcarItemInfo").param("id", String.valueOf(addResponse.getShopcarItemId()))
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("get shopcar item info response:"+getInfoResponse);
		GetShopcarItemInfoResponse itemInfoResponse=mapper.readValue(getInfoResponse, GetShopcarItemInfoResponse.class);
		
		String listItemsResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/listShopcarItems").header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("list item response :"+listItemsResponse);
		ListShopcarItemsResponse  shopCarItems=mapper.readValue(listItemsResponse, ListShopcarItemsResponse.class);
		
		String deleteItem=mockMvc.perform(MockMvcRequestBuilders.get("/customer/deleteItemFromShopcar").param("itemId", String.valueOf(addResponse.getShopcarItemId()))
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("delete item response :"+deleteItem);
		
	}
	@Test
	public void testShopping()throws Exception{
		GotoGenerateOrderRequest request=new GotoGenerateOrderRequest();
		GenerateOrderRequestItem item=new GenerateOrderRequestItem();
		item.setAmount(3);
		item.setProductId(pruductId);
		request.getOrderItems().add(item);
		String gotoGenOrderResponse=mockMvc.perform(MockMvcRequestBuilders.post("/customer/gotoGenerateOrder").content(mapper.writeValueAsString(request).getBytes()).header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();	
		System.out.println("goto generate order response:"+gotoGenOrderResponse);
		GotoGenerateOrderResponse response=mapper.readValue(gotoGenOrderResponse, GotoGenerateOrderResponse.class);
		

		
		GenerateOrderRequest geneRequest=new GenerateOrderRequest();
		List<ShopcarItemInfo> infos=response.getOrderPreItem();
		if (infos!=null) {
			for(ShopcarItemInfo i:infos) {
				GenerateOrderRequestItem orderRequestItem=new GenerateOrderRequestItem();
				orderRequestItem.setAmount(i.getAmount());
				orderRequestItem.setProductId(i.getProductId());
				geneRequest.getOrderItems().add(orderRequestItem);
			}
		}
		geneRequest.setAddressId(addressId);
		String generateOrderResponse=mockMvc.perform(MockMvcRequestBuilders.post("/customer/generateOrder").content(mapper.writeValueAsString(geneRequest).getBytes()).header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();	
		System.out.println("generate order response:"+generateOrderResponse);
		GenerateOrderResponse geneResponse=mapper.readValue(generateOrderResponse, GenerateOrderResponse.class);
		
		String listOrderResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/listOrdersByStatus").param("status", OrderStatus.NOPAY.toString())
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();	
		System.out.println("list order response:"+listOrderResponse);
		ListOrderResponse listOrder=mapper.readValue(listOrderResponse, ListOrderResponse.class);
		
		String cancelOrderResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/cancelOrder").param("orderId", "4")
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();	
		System.out.println("cancel order response:"+cancelOrderResponse);
	}


}
