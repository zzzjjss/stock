package com.uf.store.restful;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uf.store.Application;
import com.uf.store.restful.action.manager.dto.LoginResponse;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CustomerShoppingActionTest {
	private MockMvc mockMvc;
	private String token;
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
	public void test() {
		
	}


}
