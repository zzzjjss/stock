package com.uf.store.restful;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.PotentialAssignment;
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
import com.uf.store.restful.action.customer.dto.SaveAddressRequest;
import com.uf.store.restful.action.customer.dto.SaveAddressResponse;
import com.uf.store.restful.action.manager.dto.LoginResponse;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CustomerAccountActionTest {
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
	public void testAddressAction() throws Exception{
		SaveAddressRequest request=new SaveAddressRequest();
		request.setAddressDetail("addressDetail");
		request.setArea("baoan");
		request.setCity("shenzhen");
		request.setPhone("184444884848");
		request.setDefaultAddress(false);
		request.setProvince("guangdong");
		request.setReceiverName("jason");
		String saveResponse=mockMvc.perform(MockMvcRequestBuilders.post("/customer/saveAddress").content(mapper.writeValueAsString(request).getBytes()).header("Authorization", token)
					.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("save Address Response:"+saveResponse);
		SaveAddressResponse saveAddressResponse=mapper.readValue(saveResponse, SaveAddressResponse.class);	
		
		String listAddressResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/listAddress")
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("listAddress response:"+listAddressResponse);
		
		
		String defaultAddressResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/getDefaultAddress")
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("get default address response:"+defaultAddressResponse);
		
		String deleteResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/deleteAddress").param("addressId", String.valueOf(saveAddressResponse.getAddressId()))
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("delete Address Response:"+deleteResponse);
		
	}
	
	@Test
	public void testSetDefaultAddress()throws Exception{
		String setDefaultAddressResponse=mockMvc.perform(MockMvcRequestBuilders.get("/customer/setDefautAddress").param("id","9")
				.header("Authorization", token))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println("setDefaultAddressResponse:"+setDefaultAddressResponse);
	}

}
