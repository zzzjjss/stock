package com.uf.store.restful;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
import com.google.gson.Gson;
import com.uf.store.Application;
import com.uf.store.restful.dto.ListProductsRequest;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CommonActionTest {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private  ObjectMapper mapper = new ObjectMapper();
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testListPagedProduct() throws Exception {
		ListProductsRequest  listProductsRequest=new ListProductsRequest();
		listProductsRequest.setKeyWord("卡西欧");
		listProductsRequest.setPageIndex(0);
		listProductsRequest.setPageIndex(0);
		listProductsRequest.setPageSize(10);
		Gson gson=new Gson();
		System.out.println(gson.toJson(listProductsRequest));
		String listPagedProductResponse=mockMvc.perform(MockMvcRequestBuilders.post("/listPagedProducts").content(mapper.writeValueAsString(listProductsRequest).getBytes()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("listPagedProductResponse:"+listPagedProductResponse);
	}
	
	@Test
	public void testGetProductInfo() throws Exception {
		String getProductInfoResponse=mockMvc.perform(MockMvcRequestBuilders.get("/getProductInfo").param("productId", "32")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("getProductInfoResponse:"+getProductInfoResponse);
	}

}
