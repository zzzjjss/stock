package com.uf.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.uf.dao.CommonDao;
import com.uf.util.PageQueryResult;

public class CommonDaoImpl<T> implements CommonDao<T> {
	@Autowired
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public void insert(T obj) {
		hibernateTemplate.save(obj);
	}
	
	@Override
	public void delete(T entity) {
		hibernateTemplate.delete(entity);
	}
	@Override
	public void update(T obj){
		hibernateTemplate.update(obj);
	}
	@Override
	public T findById(Class<T> entityClass, Serializable id) {
		T entity = (T) hibernateTemplate.get(entityClass, id);
		return entity;
	}
	public List<T> findByHql(String hql,Object... paramValues){
		return (List<T>)hibernateTemplate.find(hql,paramValues);
	}
	public void saveOrUpdate(T obj){
		hibernateTemplate.saveOrUpdate(obj);
	}
	public int executeUpdateHql(final String hql,final Object... paramValues){
	  return hibernateTemplate.execute(new HibernateCallback<Integer>() {
          @Override
          public Integer doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
            Query query = session.createQuery(hql);
            for(int i=0;i<paramValues.length;i++){
              Object value=paramValues[i];
              if(value instanceof Integer){
                query.setInteger(i,(Integer)value);
              }else if(value instanceof Float){
                query.setFloat(i, (Float)value);
              }else{
                query.setParameter(i,value);
              }
            }
            return query.executeUpdate();
            
          }
      });
	}
	public PageQueryResult<T> queryPageEntity(final int pageSize,final int pageIndex, final String hql,final Map<String, Object> paramValue) {
      final String countHql = parseCountHql(hql);
      final PageQueryResult<T> result = new PageQueryResult<T>();
      hibernateTemplate.execute(new HibernateCallback<Object>() {
          @Override
          public Object doInHibernate(org.hibernate.Session session) throws HibernateException, SQLException {
            Query countQuery = session.createQuery(countHql);
            Query query = session.createQuery(hql);
            if (paramValue != null && paramValue.size() > 0) {
                for (String param : paramValue.keySet()) {
                    Object value=paramValue.get(param);
                    if(value instanceof Collection<?>){
                      query.setParameterList(param, (Collection<?>)value);
                      countQuery.setParameterList(param, (Collection<?>)value);
                    }else if(value instanceof  Object[]){
                      query.setParameterList(param, (Object[])value);
                      countQuery.setParameterList(param, (Object[])value);
                    }else{
                      query.setParameter(param, value);
                      countQuery.setParameter(param, value);
                    }
                    
                }
            }
            Long totalRow = (Long) countQuery.list().get(0);
            int totalPage = countTotalPage(totalRow.intValue(), pageSize);
            query.setMaxResults(pageSize);
            query.setFirstResult(pageSize * (pageIndex - 1));
            result.setPageData(query.list());
            result.setPageIndex(pageIndex);
            result.setPageSize(pageSize);
            result.setTotalPage(totalPage);
            result.setTotalRecord(totalRow.intValue());
            return null;
          }
      });
      return result;
  }

  private int countTotalPage(int totalRow, int pageSize) {
      int totalPage = totalRow / pageSize;
      int tmp = totalRow % pageSize;
      if (tmp > 0)
          totalPage++;
      return totalPage;
  }

  private String parseCountHql(String hql) {
      int index = hql.indexOf(" from ");
      String afterFrom = hql.substring(index);
      return "select count(*) " + afterFrom;
  }
	
}
