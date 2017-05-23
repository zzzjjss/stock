package com.uf.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uf.bean.Result;
import com.uf.entity.BuycarProductInfo;
import com.uf.entity.Customer;
import com.uf.entity.Product;
import com.uf.entity.ProductImage;
import com.uf.service.CustomerActionService;
import com.uf.service.ProductManageService;
import com.uf.util.FileUtil;
import com.uf.util.GsonExcludeStrategy;
import com.uf.util.PageQueryResult;
import com.uf.util.StringUtil;

@Controller
public class CustomerActionController {
  @Autowired
  private ProductManageService productManageService;
  @Autowired
  private CustomerActionService custermActionService;

  @RequestMapping("/index")
  public String indexPage(HttpServletRequest request,Model model) {
      Object obj=request.getSession().getAttribute("customer");
      if(obj!=null&&obj instanceof Customer){
        model.addAttribute("customer", (Customer)obj);
      }
      try {
        request.setCharacterEncoding("UTF-8");
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      String keyword=request.getParameter("keyword");
      model.addAttribute("keyword",keyword);
      return "customer/index";
  }
  @RequestMapping("/customer/login")
  @ResponseBody
  public String login(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request){
    Result result=new Result();
    String userName=allRequestParams.get("userName");
    String password=allRequestParams.get("password");
    Customer customer=custermActionService.findCustomerByUserName(userName);
    if(customer!=null&&password.equals(customer.getPassword())){
      request.getSession().setAttribute("customer", customer);
      result.setResult("ok");
    }else{
      result.setResult("fail");
      result.setMes("用户或密码错误");
    }
    Gson gson=new Gson();
    return gson.toJson(result);

  }
  @RequestMapping("/customer/productDetail")
  public String productDetail(@RequestParam Map<String,String> allRequestParams,Model model,HttpServletRequest request) {
      String id=allRequestParams.get("id");
      Product pro=productManageService.findProductById(Integer.parseInt(id));
      File imgFolder=new File(request.getServletContext().getRealPath("/")+"img");
      String contextPath=request.getServletContext().getContextPath();
      setImgPathToProduct(pro,imgFolder,contextPath);
      model.addAttribute("product", pro);
      return "customer/productDetail";
  }
  @RequestMapping("/customer/queryProducts")
  @ResponseBody
  public String queryProducts(@RequestParam Map<String,String> allRequestParams,HttpServletRequest request){
    String queryKeyword=allRequestParams.get("keyword");
    String pageIndex=allRequestParams.get("pageIndex");
    String contextPath=request.getServletContext().getContextPath();
    if(StringUtil.isNullOrEmpty(pageIndex)){
      pageIndex="1";
    }
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
    Gson gson=new GsonBuilder().setExclusionStrategies(new GsonExcludeStrategy()).create();
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
}
