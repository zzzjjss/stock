package com.uf.store.restful.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uf.store.dao.mysql.po.Product;
import com.uf.store.restful.dto.ListProductsRequest;
import com.uf.store.restful.dto.ProductSellInfo;
import com.uf.store.restful.dto.ListPagedProductsResponse;
import com.uf.store.service.ProductManageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "the api opened for public ")
public class CommonAction {
	private Logger logger=LoggerFactory.getLogger(CommonAction.class);
	@Autowired
	private ProductManageService productManage;
	@Value("${webServer.imageSavePath}") 
	private String imagePath;
	@Value("${webServer.imageBaseUrl}") 
	private String imageBaseUrl;

	
	@RequestMapping(value = "listPagedProducts", method = RequestMethod.POST)
	@ApiOperation(value = "list products by start index and length ", notes = "maybe need add priority field to product bean")
	public ListPagedProductsResponse listPagedProducts(@RequestBody ListProductsRequest request) {
		ListPagedProductsResponse response=new ListPagedProductsResponse();
		try {
			Page<Product>  products=productManage.getPagedProducts(request.getPageIndex(), request.getPageSize(), request.getKeyWord());
			List<ProductSellInfo> productInfors=new ArrayList<ProductSellInfo>();
			response.setPageIndex(products.getNumber());
			response.setProductInfors(productInfors);
			if (products!=null) {
				products.forEach(p->{
					ProductSellInfo info=new ProductSellInfo();
					info.setDescription(p.getDescription());
					info.setId(p.getId());
					info.setName(p.getName());
					info.setSellPrice(p.getSellPrice());
					List<String> productImgUrls=new ArrayList<String>();
					File productImgFolder=new File(imagePath,"/"+p.getId());
					if(productImgFolder.exists()) {
						File []imgs=productImgFolder.listFiles();
						if(imgs!=null&&imgs.length>0) {
							for(File img:imgs) {
								productImgUrls.add(imageBaseUrl+"/"+p.getId()+"/"+img.getName());
							}
						}
					}
					info.setImgUrls(productImgUrls);
					productInfors.add(info);
				});
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return response;
	}

}
