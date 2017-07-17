package com.uf.store.service.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.uf.store.util.PageQueryResult;

public interface  CommonDao<T> {
	public void insert(T obj);
	public void delete(T obj);
	public void update(T obj);
	public void saveOrUpdate(T obj);
	public T findById(Class<T> entity,Serializable id);
	public List<T> findByHql(String hql,Object... paramValues);
	public int executeUpdateHql(String hql,Object... paramValues);
	public PageQueryResult<T> queryPageEntity(final int pageSize,final int pageIndex, final String hql,final Map<String, Object> paramValue);
}
