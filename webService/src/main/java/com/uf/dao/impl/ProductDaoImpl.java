package com.uf.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.uf.dao.ProductDao;
import com.uf.entity.Product;
import com.uf.util.PageQueryResult;
import com.uf.util.StringUtil;
@Component("productDao")
public class ProductDaoImpl extends CommonDaoImpl<Product> implements ProductDao{
  public PageQueryResult<Product> findPagedProductByHql(String hql,Map<String, Object> paramValue,int pageSize,int pageIndex){
    if(!StringUtil.isNullOrEmpty(hql)){
      return queryPageEntity(pageSize, pageIndex, hql.toString(), paramValue);
    }else{
      return null;
    }
  }
}
