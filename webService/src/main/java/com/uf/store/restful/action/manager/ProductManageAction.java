package com.uf.store.restful.action.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uf.store.dao.mysql.po.Product;
import com.uf.store.restful.action.manager.dto.GetProductDetailResponse;
import com.uf.store.restful.action.manager.dto.LoginRequest;
import com.uf.store.restful.action.manager.dto.LoginResponse;
import com.uf.store.restful.action.manager.dto.SaveProductRequest;
import com.uf.store.restful.action.manager.dto.SaveProductResponse;
import com.uf.store.restful.action.manager.dto.UploadImageResponse;
import com.uf.store.restful.dto.ProductSellInfo;
import com.uf.store.restful.dto.RestfulResponse;
import com.uf.store.restful.dto.RestfulResponse.ResultCode;
import com.uf.store.service.ManagerAccountService;
import com.uf.store.service.ProductManageService;
import com.uf.store.util.ImageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("manager")
@Api(tags = "manage  product interface")
public class ProductManageAction {
	private Logger logger=LoggerFactory.getLogger(ProductManageAction.class);
	@Autowired
	private ProductManageService productManage;
	@Autowired
	private ManagerAccountService managerAccountService;
	@Value("${webServer.imageSavePath}") 
	private String imagePath;
	@Value("${webServer.imageBaseUrl}") 
	private String imageBaseUrl;
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ApiOperation(value = "manager login ", notes = "manager login  with username and password")
	public LoginResponse login(@RequestBody LoginRequest request) {
		LoginResponse response=new LoginResponse();
		try {
			String token=managerAccountService.loginGenerateToken(request.getUserName(),request.getPassword());	
			if (token!=null) {
				response.setToken(token);
				response.setResultCode(ResultCode.OK);
			}else {
				response.setResultCode(ResultCode.FAIL);
				response.setMes("userName or password was  wrong");
			}
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}

	
	@RequestMapping(value="uploadProductImage",method=RequestMethod.POST)
	public UploadImageResponse uploadProductImage(@RequestParam("imageFile") MultipartFile uploadFile) {
		UploadImageResponse  response=new UploadImageResponse();
		File imageFile=null;
		try {
			if (uploadFile!=null) {
				String uuid=UUID.randomUUID().toString();
				String uploadFileName=uploadFile.getOriginalFilename();
				String fileExt=FilenameUtils.getExtension(uploadFileName);
				imageFile=new File(imagePath,uuid+"."+fileExt);
				FileUtils.copyInputStreamToFile(uploadFile.getInputStream(),imageFile);
				//TODO  adjust the image to fixed size
				response.setImagePath(imageBaseUrl+"/"+imageFile.getName());
				response.setResultCode(ResultCode.OK);
			}else {
				response.setResultCode(ResultCode.FAIL);
				response.setMes("Upload file is empty");
			}
		} catch (Exception e) {
			logger.error("",e);
			if (imageFile!=null) {
				imageFile.delete();
			}
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}

	
	@RequestMapping(value="saveProduct",method=RequestMethod.POST)
	public SaveProductResponse  saveProduct(@RequestBody SaveProductRequest request) {
		SaveProductResponse  response=new SaveProductResponse();
		try {
			List<File> imageFiles=new ArrayList<File>();
			if (request.getProduct()!=null) {
				Map<String,byte[]> images=new HashMap<String,byte[]>();
				request.getImageNames().forEach(imageName->{
					File imageFile=new File(imagePath+"/"+imageName);
					if(!imageFile.exists()&&request.getProduct().getId()!=null) {
						imageFile=new File(imagePath+"/"+request.getProduct().getId()+"/"+imageName);
					}
					if (imageFile.exists()) {
						try {
							byte[] content=FileUtils.readFileToByteArray(imageFile);
							images.put(imageFile.getName(), content);
							imageFiles.add(imageFile);
						} catch (IOException e) {
							logger.error("",e);
						}
					}
				});
				productManage.saveProduct(request.getProduct(),images);
				Long productId=request.getProduct().getId();
				if (imageFiles!=null&&imageFiles.size()>0) {
					File file=imageFiles.get(0);
					ImageUtil.resize(file, new File(imagePath+"/"+productId+"/snapshot."+FilenameUtils.getExtension(file.getName())), 100, 100);
				}
				imageFiles.forEach(file->{
					try {
						File destination=new File(imagePath+"/"+productId+"/"+file.getName());
						if (!destination.exists()) {
							FileUtils.moveFile(file, destination);
						}
					} catch (IOException e) {
						logger.error("move file :"+file.getAbsolutePath()+" fail ",e);
					}
				});
				response.setProductId(productId);
				response.setResultCode(ResultCode.OK);
			}else {
				response.setResultCode(ResultCode.FAIL);
				response.setMes("product is empty");	
			}
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;
	}
	@RequestMapping(value="deleteProduct",method=RequestMethod.GET)
	public RestfulResponse deleteProduct(Long id) {
		RestfulResponse  response=new RestfulResponse();
		try {
			productManage.deleteProduct(id);
			response.setResultCode(ResultCode.OK);
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;	
	}
	@RequestMapping(value="getProductDetail",method=RequestMethod.GET)
	public GetProductDetailResponse getProductDetail(Integer id) {
		GetProductDetailResponse response=new GetProductDetailResponse();
		try {
			Product product=productManage.getProductById(id);
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
						}
						productImgUrls.add(imageBaseUrl+"/"+product.getId()+"/"+img.getName());
					}
				}
			}
			info.setImgUrls(productImgUrls);
			response.setBuyPrice(product.getBuyPrice());
			response.setOnLine(product.getOnLine());
			response.setProductInfo(info);
			response.setSearchKeywords(product.getSearchKeywords());
		} catch (Exception e) {
			logger.error("",e);
			response.setResultCode(ResultCode.FAIL);
			response.setMes(e.getMessage());
		}
		return response;	
	}
}
