package com.uf.store.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.uf.store.searcher.SearchEngine;
import com.uf.store.service.ProductManageService;
import com.uf.store.service.dao.ManagerDao;
import com.uf.store.service.dao.ProductDao;
import com.uf.store.service.dao.ProductImgDao;
import com.uf.store.service.dao.WordDao;
import com.uf.store.service.entity.Manager;
import com.uf.store.service.entity.Product;
import com.uf.store.service.entity.ProductImage;
import com.uf.store.service.entity.Word;
import com.uf.store.springbean.ProductSearchTool;
import com.uf.store.util.PageQueryResult;
import com.uf.store.util.StringUtil;
@Service("productManageService")
public class ProductManageServiceImpl implements ProductManageService{
  @Autowired
  private ProductDao  productDao;
  @Autowired
  private ProductImgDao  productImgDao;
  @Autowired
  private SearchEngine  sercherEngine;
  @Autowired
  private ManagerDao managerDao;
  @Autowired
  private WordDao wordDao;
  @Autowired
  private ProductSearchTool searchTool;
  public void addProduct(Product product,List<ProductImage>  imgs){
      productDao.insert(product);
      sercherEngine.addProductInfoToIndex(product);
      if(imgs!=null&&imgs.size()>0){
        for(ProductImage img:imgs){
          img.setProduct(product);
          productImgDao.insert(img);
        }
      }
      if(!Strings.isNullOrEmpty(product.getSearchKeywords())){
    	  List<String> words=Splitter.on(' ').trimResults().omitEmptyStrings().splitToList(product.getSearchKeywords());
			for(String word:words){
				Word w=new Word();
				w.setWord(word.trim());
				System.out.println("----"+word.trim());
				this.saveWord(w);
				searchTool.addWordToTree(word.trim());
			}
		}
  }
  public Manager findManagerByName(String userName){
	  List<Manager> cus=managerDao.findByHql("select c from  Manager c  where c.userName=?", userName);
	    if(cus!=null&&cus.size()>0){
	      return cus.get(0);
	    }else{
	      return null;
	    }
  }

  public void updateProduct(Product product,List<ProductImage>  imgs){
    productDao.update(product);
    productImgDao.executeUpdateHql("delete from ProductImage img where img.product.id=? ", product.getId());
    if(imgs!=null&&imgs.size()>0){
      for(ProductImage img:imgs){
        img.setProduct(product);
        productImgDao.insert(img);
      }
    }
    sercherEngine.updateProductIndex(product);
  }
  public PageQueryResult<Product> findProducsByKeyword(String keyword,int pageSize,int pageIndex){
     if(StringUtil.isNullOrEmpty(keyword)){
       return productDao.findPagedProductByHql("select p from Product p ",null, pageSize, pageIndex);
     }else{
       List<String> words=searchTool.parseWords(keyword);
       if(words==null||words.size()<=0)
    	   return null;
       keyword=Joiner.on(" ").join(words);
       System.out.println("after parse the keyword is ->"+keyword);
       List<Integer> ids=sercherEngine.searchProductIds(keyword);
       if(ids!=null&&ids.size()>0){
         ids=getPageIndexIds(pageIndex,pageSize,ids);
         Map<String, Object> idsParam=new HashMap<String,Object>();
         idsParam.put("ids", ids);
         return productDao.findPagedProductByHql("select p from Product p where p.id in (:ids) order by field(p.id,:ids) ", idsParam,pageSize, pageIndex);
       }else{
         return null;
       }

     }
  }
  private List<Integer> getPageIndexIds(int pageIndex,int pageSize,List<Integer> allIds){
    List<Integer> result=new  ArrayList<Integer>();
    if(allIds!=null&&allIds.size()>0){
      int start=pageSize*(pageIndex-1);
      int end=start+(pageSize-1);
      if(end>(allIds.size()-1)){
        end=(allIds.size()-1);
      }
      for(int i=start;i<=end;i++){
        result.add(allIds.get(i));
      }
    }
    return result;
  }
  public List<ProductImage> findProductImages(Integer productId){
    return productImgDao.findByHql("select pi from  ProductImage pi  where pi.product.id=?", productId);
  }
  public Product findProductById(Integer productId){
    return productDao.findById(Product.class, productId);
  }

  public void deleteProductById(Integer productId){
    productImgDao.executeUpdateHql("delete from ProductImage pi where pi.product.id=?", productId);
    productDao.executeUpdateHql("delete from Product p where p.id=?", productId);
    sercherEngine.deleteProductFromIndexById(productId);

  }
  public void saveWord(Word word){
    wordDao.saveOrUpdate(word);
  }
  public PageQueryResult<Word> findPagedWords(int pageSize,int pageIndex){
	  return wordDao.queryPageEntity(pageSize, pageIndex, "select w from Word w ", null);
  }
  public PageQueryResult<Product> findPagedProducts(int pageSize,int pageIndex){
	  return productDao.findPagedProductByHql("select p from Product p ",null, pageSize, pageIndex);
  }
}
