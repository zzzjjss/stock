package com.uf.stock.data.dao;

import java.io.Serializable;
import java.util.List;

public interface CommonDao<T> {
	public void insert(T obj);
	public void delete(T obj);
	public void update(T obj);
	public T findById(Class<T> entity,Serializable id);
	public List<T> findAll(Class<T> entity);
	public void saveOrUpdate(T obj);
	public List<T> findByHql(String hql, Object... paramValues);
	public Integer executeUpdateHql(final String hql, final Object... paramValues);
}
