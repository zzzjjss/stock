package com.uf.store.service.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.uf.store.service.dao.ProductDao;
import com.uf.store.service.entity.Product;
import com.uf.store.util.PageQueryResult;
import com.uf.store.util.StringUtil;
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
