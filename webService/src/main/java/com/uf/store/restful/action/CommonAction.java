package com.uf.store.restful.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uf.store.dao.mysql.po.OrderItem;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ProductImage;
import com.uf.store.dao.mysql.po.ProductProperties;
import com.uf.store.restful.dto.ListProductsRequest;
import com.uf.store.restful.dto.ProductOrderInfo;
import com.uf.store.restful.dto.ProductSellInfo;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;
import com.uf.store.restful.dto.GetProductInfoResponse;
import com.uf.store.restful.dto.ListPagedProductsResponse;
import com.uf.store.service.ProductManageService;
import com.uf.store.util.ImageUtil;

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
			response.setPageIndex(request.getPageIndex());
			response.setProductInfors(productInfors);
			if (products!=null) {
				response.setTotalPage(products.getTotalPages());
				products.forEach(p->{
					ProductSellInfo info=new ProductSellInfo();
					info.setDescription(p.getDescription());
					info.setId(p.getId());
					info.setName(p.getName());
					info.setSellPrice(p.getSellPrice());
					info.setSnapshotImgUrl(imageBaseUrl+"/"+p.getId()+"/snapshot.jpg");
					productInfors.add(info);
					generateImgToFolder(p);
				});
			}
		} catch (Exception e) {
			logger.error("",e);
			response.setMes(e.getMessage());
			response.setResultCode(ResultCode.FAIL);
		}
		return response;
	}
	private void generateImgToFolder(Product p) {
		Long productId=p.getId();
		File productImgFolder=new File(imagePath+"/"+productId);
		if (productImgFolder.exists()&&productImgFolder.listFiles().length>0) {
			return;
		}else {
			productImgFolder.mkdirs();
		}
		List<ProductImage> images=productManage.listProductImages(p);
		images.forEach(image->{
			try {
				File destination=new File(productImgFolder,image.getFileName());
				if (!destination.exists()) {
					FileUtils.writeByteArrayToFile(destination, image.getImageContent());
				}
			} catch (IOException e) {
				logger.error("generate product image fail ",e);
			}
		});
		File imgs[]=productImgFolder.listFiles();
		if (imgs!=null&&imgs.length>0) {
			File file=imgs[0];
			ImageUtil.resize(file, new File(productImgFolder,"/snapshot."+FilenameUtils.getExtension(file.getName())), 100, 100);
		}
	}
	@RequestMapping(value = "getProductInfo", method = RequestMethod.GET)	
	public GetProductInfoResponse getProductInfo(String productId) {
		GetProductInfoResponse response=new GetProductInfoResponse();
		try {
			Product product=productManage.getProductById(Long.parseLong(productId));
			if (product==null) {
				response.setMes("noProdut");
				response.setResultCode(ResultCode.FAIL);
				return response;
			}
			ProductSellInfo info=new ProductSellInfo();
			info.setDescription(product.getDescription());
			info.setId(product.getId());
			info.setName(product.getName());
			info.setSellPrice(product.getSellPrice());
			List<String> productImgUrls=new ArrayList<String>();
			File productImgFolder=new File(imagePath,"/"+product.getId());
			if(productImgFolder.exists()) {
				File []imgs=productImgFolder.listFiles();
				if(imgs!=null&&imgs.length>0) {
					for(File img:imgs) {
						if (img.getName().startsWith("snapshot")) {
							info.setSnapshotImgUrl(imageBaseUrl+"/"+product.getId()+"/"+img.getName());
							continue;
						}
						productImgUrls.add(imageBaseUrl+"/"+product.getId()+"/"+img.getName());
					}
				}
			}
			info.setImgUrls(productImgUrls);
			response.setProductSellInfo(info);
			List<OrderItem> items=productManage.listProductOrderItems(product.getId(), 0, 10);
			if (items!=null) {
				items.forEach(it->{
					ProductOrderInfo orderInfo=new ProductOrderInfo();
					orderInfo.setBuyProductNumber(it.getAmount());
					orderInfo.setCustomerName(it.getOrder().getCustomer().getName());
					orderInfo.setEvaluate("");
					response.getOrderInfos().add(orderInfo);
				});
			}
			
			List<ProductProperties> props=productManage.listProductProperties(product);
			if (props!=null&&props.size()>0) {
				for(ProductProperties p:props) {
					if (p.getPropKey().equalsIgnoreCase("jdUrl")) {
						continue;
					}
					response.getProductProp().put(p.getPropKey(), p.getPropValue());
				}
			}
		} catch (Exception e) {
			logger.error("",e);
			response.setMes(e.getMessage());
			response.setResultCode(ResultCode.FAIL);
		}
		return response;	
	}
}
