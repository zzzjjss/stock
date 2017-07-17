package com.uf.store.service.dao;

import java.util.Map;

import com.uf.store.service.entity.Product;
import com.uf.store.util.PageQueryResult;

public interface ProductDao extends CommonDao<Product>{
	public PageQueryResult<Product> findPagedProductByHql(String hql,Map<String, Object> paramValue,int pageSize,int pageIndex);
}
