package com.uf.controller.storemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import com.uf.bean.Result;
import com.uf.entity.Manager;
import com.uf.entity.Product;
import com.uf.entity.ProductImage;
import com.uf.service.ProductManageService;
import com.uf.util.FileUtil;
import com.uf.util.PageQueryResult;
import com.uf.util.ParamsValueUtil;
import com.uf.util.StringUtil;

@Controller
public class ProductManageController {
	  @Autowired
	  private ProductManageService productManageService;

	  @RequestMapping("/manager/loginPage")
	  public String managerLoginPage(){
		  return "manager/managerLogin";
	  }
	  @RequestMapping("/manager/auth/logOut")
	  @ResponseBody
	  public String managerLogout(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request){
	    request.getSession().removeAttribute("manager");
	    Result result=new Result();
	    result.setResult("ok");
	    Gson gson=new Gson();
	    return gson.toJson(result);
	  }

	  @RequestMapping("/manager/login")
	  @ResponseBody
	  public String managerLogin(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request){
		  Result result=new Result();
	    String userName=allRequestParams.get("userName");
	    String password=allRequestParams.get("password");
	    Manager manager=productManageService.findManagerByName(userName);
	    if(manager!=null&&password.equals(manager.getPassword())){
	      request.getSession().setAttribute("manager", manager);
	      result.setResult("ok");
	    }else{
	      result.setResult("fail");
	      result.setMes("用户或密码错误");
	    }
		    Gson gson=new Gson();
		    return gson.toJson(result);
	  }

	  @RequestMapping("/manager/auth/productAdd")
	  public String productAdd(@RequestParam Map<String,String> allRequestParams,Model model) {
	      return "manager/productAdd";
	  }
	  @RequestMapping("/manager/auth/productQuery")
	  public String productQuery(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request) {
	      Object manager=request.getSession().getAttribute("manager");
	      if(manager!=null&&manager instanceof Manager){
	        model.addAttribute("manager", manager);
	      }
	      return "manager/productQuery";
	  }

	  @RequestMapping("/manager/auth/productEdit")
	  public String productEdit(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request) {
	      String id=allRequestParams.get("id");
	      Product pro=productManageService.findProductById(Integer.parseInt(id));
	      File imgFolder=new File(request.getServletContext().getRealPath("/")+"img");
	      String contextPath=request.getServletContext().getContextPath();
	      setImgPathToProduct(pro,imgFolder,contextPath);
	      model.addAttribute("product", pro);
	      return "manager/productEdit";
	  }
	  @RequestMapping("/manager/auth/productDetail")
	  public String productDetail(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request) {
	      String id=allRequestParams.get("id");
	      Product pro=productManageService.findProductById(Integer.parseInt(id));
	      File imgFolder=new File(request.getServletContext().getRealPath("/")+"img");
	      String contextPath=request.getServletContext().getContextPath();
	      setImgPathToProduct(pro,imgFolder,contextPath);
	      model.addAttribute("product", pro);
	      return "manager/productDetail";
	  }


	  @RequestMapping("/manager/auth/deleteProduct")
	  @ResponseBody
	  public String deleteProduct(@RequestParam Map<String,String> allRequestParams,HttpServletRequest request){
	    Result result=new Result();
	    try{
	      String id=allRequestParams.get("id");
	      productManageService.deleteProductById(Integer.valueOf(id));
	      File imgFolder=new File(request.getServletContext().getRealPath("/")+"img/"+id);
	      if(imgFolder.exists()){
	        FileUtil.deleteFolderRecusive(imgFolder);
	      }
	      result.setResult("ok");
	    }catch(Exception e){
	      e.printStackTrace();
	      result.setResult("fail");
	    }

	    Gson gson=new Gson();
	    return gson.toJson(result);
	  }
	  @RequestMapping("/manager/auth/queryProducts")
	  @ResponseBody
	  public String queryProducts(@RequestParam Map<String,String> allRequestParams,HttpServletRequest request){
	    String queryKeyword=allRequestParams.get("keyword");
	    String pageIndex=allRequestParams.get("pageIndex");
	    String contextPath=request.getServletContext().getContextPath();
	    PageQueryResult<Product> products=productManageService.findProducsByKeyword(queryKeyword, 8, Integer.valueOf(pageIndex));
	    File imgFolder=new File(request.getServletContext().getRealPath("/")+"img");
	    if(!imgFolder.exists()){
	      imgFolder.mkdirs();
	    }
	    if(products!=null){
	      List<Product> pageProducts=products.getPageData();
	      for(Product p:pageProducts){
	        setImgPathToProduct(p,imgFolder,contextPath);
	      }
	    }
	    Gson gson=new Gson();
	    return gson.toJson(products);
	  }

	  private void setImgPathToProduct(Product pro,File imgFolder,String contextPath){
	    File prodImg=new File(imgFolder,String.valueOf(pro.getId()));
	    if(!prodImg.exists()||prodImg.listFiles().length==0){
	        List<ProductImage> imgs=productManageService.findProductImages(pro.getId());
	        if(imgs!=null&&imgs.size()>0){
	          for(ProductImage img:imgs){
	            File imgFile=new File(prodImg,img.getFileName());
	            FileUtil.writeBytesToFile(img.getImage(), imgFile);
	          }
	        }
	    }
	    File imgs[]=prodImg.listFiles();
	    if(imgs!=null&&imgs.length>0){
	      List<String> imgPaths=new ArrayList<String>();
	      for(File img:imgs){
	        imgPaths.add(contextPath+"/img/"+pro.getId()+"/"+img.getName());
	      }
	      pro.setImgsPath(imgPaths);
	    }

	  }
	  @RequestMapping("/manager/auth/uploadProductImg")
	  @ResponseBody
	  public String uploadProductImg(@RequestParam("imgFile") MultipartFile file,Model model,HttpServletRequest request) {
	    UploadImgResult result=new UploadImgResult();
	    try{
	      String sessionId=request.getSession().getId();
	      File imgPath=new File(request.getServletContext().getRealPath("/")+"tmp/"+sessionId);
	      if(!imgPath.exists()){
	          imgPath.mkdirs();
	      }
	      String extend=FileUtil.getFileExtendName(file.getOriginalFilename());
	      File productImg=new File(imgPath,UUID.randomUUID().toString()+extend);
	      productImg.createNewFile();
	      FileOutputStream  fileOut=new FileOutputStream(productImg);
	      fileOut.write(file.getBytes());
	      fileOut.close();
	      result.setResult("ok");
	      result.setImgPath(request.getServletContext().getContextPath()+"/tmp/"+sessionId+"/"+productImg.getName());
	    }catch(Exception e){
	      e.printStackTrace();
	      result.setResult("fail");
	    }
	    Gson gson=new Gson();
	    return gson.toJson(result);
	  }

	  @RequestMapping("/manager/auth/addProduct")
	  @ResponseBody
	  public String addProduct(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request) {
	    Result  result=new Result();
	    try{
	      String buyPrice=allRequestParams.get("buyPrice");
	      String sellPrice=allRequestParams.get("sellPrice");
	      String desc=allRequestParams.get("description");
	      String searchKeywords=allRequestParams.get("searchKeywords");
	      String name=allRequestParams.get("name");
	      String imgsPath=allRequestParams.get("imgs");

	      Product product=new Product();
	      product.setBuyPrice(ParamsValueUtil.getFloadValue(buyPrice));
	      product.setDescription(desc);
	      product.setName(name);
	      if(!StringUtil.isNullOrEmpty(searchKeywords)){
	        product.setSearchKeywords(StringUtil.replaceAllSpecialCharToWhitespace(searchKeywords));
	      }

	      product.setSellPrice(ParamsValueUtil.getFloadValue(sellPrice));
	      product.setUpdateTime(new  Date());

	      List<ProductImage> proImages=new ArrayList<ProductImage>();
	      File realPath=new File(request.getServletContext().getRealPath("/"));
	      String parent=realPath.getParent();
	      if(!StringUtil.isNullOrEmpty(imgsPath)){
	        String pathes[]=imgsPath.split(",");

	        for(String path:pathes){
	          ProductImage proImage=new ProductImage();
	          File imgFile=new File(parent,path);
	          proImage.setImage(FileUtil.readFileBytes(imgFile));
	          proImage.setFileName(imgFile.getName());
	          proImages.add(proImage);
	          imgFile.delete();
	        }
	      }
	      productManageService.addProduct(product, proImages);
	      result.setResult("ok");
	    }catch(Exception e){
	      e.printStackTrace();
	      result.setResult("fail");
	    }
	    Gson gson=new Gson();
	    return gson.toJson(result);
	  }

	  @RequestMapping("/manager/auth/saveProduct")
	  @ResponseBody
	  public String saveProduct(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request) {
	    Result  result=new Result();
	    Gson gson=new Gson();
	    try{
	      String id=allRequestParams.get("id");
	      String buyPrice=allRequestParams.get("buyPrice");
	      String sellPrice=allRequestParams.get("sellPrice");
	      String desc=allRequestParams.get("description");
	      String searchKeywords=allRequestParams.get("searchKeywords");
	      String name=allRequestParams.get("name");
	      String imgsPath=allRequestParams.get("imgs");

	      Product product=productManageService.findProductById(Integer.valueOf(id));
	      if(product==null){
	        result.setResult("fail");
	        result.setMes("请选择保存的产品");
	        return gson.toJson(result);
	      }

	      product.setBuyPrice(ParamsValueUtil.getFloadValue(buyPrice));
	      product.setDescription(desc);
	      product.setName(name);
	      if(!StringUtil.isNullOrEmpty(searchKeywords)){
	        product.setSearchKeywords(StringUtil.replaceAllSpecialCharToWhitespace(searchKeywords));
	      }

	      product.setSellPrice(ParamsValueUtil.getFloadValue(sellPrice));
	      product.setUpdateTime(new  Date());

	      List<ProductImage> proImages=new ArrayList<ProductImage>();
	      String contextPath=request.getServletContext().getContextPath();
	      File realPath=new File(request.getServletContext().getRealPath("/"));

	      if(!StringUtil.isNullOrEmpty(imgsPath)){
	        String pathes[]=imgsPath.split(",");
	        for(String path:pathes){
	          ProductImage proImage=new ProductImage();
	          path=path.replaceFirst(contextPath, "");
	          File imgFile=new File(realPath,path);
	          proImage.setImage(FileUtil.readFileBytes(imgFile));
	          proImage.setFileName(imgFile.getName());
	          proImages.add(proImage);
	          imgFile.delete();
	        }
	      }
	      productManageService.updateProduct(product, proImages);
	      File imgFolder=new File(request.getServletContext().getRealPath("/")+"img/"+product.getId());
	      FileUtil.deleteFolderRecusive(imgFolder);
	      result.setResult("ok");
	    }catch(Exception e){
	      e.printStackTrace();
	      result.setResult("fail");
	    }

	    return gson.toJson(result);
	  }



	  class UploadImgResult extends Result{
	    String imgPath="";

	    public String getImgPath() {
	      return imgPath;
	    }

	    public void setImgPath(String imgPath) {
	      this.imgPath = imgPath;
	    }

	  }
}
