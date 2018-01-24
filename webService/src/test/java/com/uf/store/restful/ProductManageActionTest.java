package com.uf.store.restful;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uf.store.Application;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.restful.action.manager.dto.LoginRequest;
import com.uf.store.restful.action.manager.dto.LoginResponse;
import com.uf.store.restful.action.manager.dto.SaveProductRequest;
import com.uf.store.restful.action.manager.dto.SaveProductResponse;
import com.uf.store.restful.action.manager.dto.UploadImageResponse;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ProductManageActionTest {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private String token;
	private  ObjectMapper mapper = new ObjectMapper();
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		LoginRequest request=new LoginRequest();
		request.setUserName("jason");
		request.setPassword("zhang");
		String content=mockMvc.perform(post("/manager/login").content(mapper.writeValueAsString(request).getBytes()).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println(content);
		LoginResponse response=mapper.readValue(content, LoginResponse.class);
		token=response.getToken();
	}

	@Test
	public void testProductManage() throws Exception {
		for(int i=0;i<11;i++) {
			List<String> imgFileNames=new ArrayList<String>();
			for(int j=1;j<4;j++) {
				String fileName=j+".jpg";
				InputStream inputStream=this.getClass().getResourceAsStream(fileName);
				MockMultipartFile file=new MockMultipartFile("imageFile",fileName,null, inputStream);
				String result=mockMvc.perform(MockMvcRequestBuilders.fileUpload("/manager/uploadProductImage").file(file))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
				UploadImageResponse response=mapper.readValue(result, UploadImageResponse.class);
				String path=response.getImagePath();
				String imgFileName=path.substring(path.lastIndexOf("/")+1);
				System.out.println(imgFileName);
				imgFileNames.add(imgFileName);
			}
			
			SaveProductRequest saveProductRequest=new SaveProductRequest();
			saveProductRequest.setImageNames(imgFileNames);
			Product product=new Product();
			product.setBuyPrice(10f);
			product.setDescription("descript");
			product.setName("first pro");
			product.setOnLine(true);
			product.setBrand("浪琴");
			product.setSearchKeywords("langines 浪琴 G-SHOCK");
			product.setSellPrice(11f);
			product.setUpdateTime(new Date());
			saveProductRequest.setProduct(product);
			String content=mockMvc.perform(post("/manager/saveProduct").content(mapper.writeValueAsString(saveProductRequest).getBytes()).contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			System.out.println(content);
			SaveProductResponse saveProductResponse= mapper.readValue(content, SaveProductResponse.class);
			System.out.println(saveProductResponse.getProductId());
			String getProDetailResponse=mockMvc.perform(MockMvcRequestBuilders.get("/manager/getProductDetail").param("id",String.valueOf(saveProductResponse.getProductId())))
								.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			System.out.println(getProDetailResponse);
			
			
			product.setSearchKeywords(product.getSearchKeywords()+" 石英表");
			product.setId(saveProductResponse.getProductId());
			String editProductResponse=mockMvc.perform(post("/manager/saveProduct").content(mapper.writeValueAsString(saveProductRequest).getBytes()).contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			System.out.println("edit product Response:"+editProductResponse);
			
//		String deleteResponse=mockMvc.perform(MockMvcRequestBuilders.get("/manager/deleteProduct").param("id", String.valueOf(saveProductResponse.getProductId())))
//				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//		System.out.println(deleteResponse);
			
		}
		
		
	}
}
