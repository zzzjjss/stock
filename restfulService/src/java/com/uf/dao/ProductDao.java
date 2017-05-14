package com.uf.dao;

import java.util.Map;

import com.uf.entity.Product;
import com.uf.util.PageQueryResult;

public interface ProductDao extends CommonDao<Product>{
	public PageQueryResult<Product> findPagedProductByHql(String hql,Map<String, Object> paramValue,int pageSize,int pageIndex);
}
