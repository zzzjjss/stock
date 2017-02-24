package com.uf.stock.data.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.hibernate.Query;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;
import com.uf.stock.data.dao.StockTradeInfoWithAnalysisResultDao;
@Component("stockTradeInfoWithResultDao")
public class StockTradeInfoWithAnalysisResultDaoImpl extends CommonRdbsDaoImpl<StockTradeInfoWithAnalysisResult> implements StockTradeInfoWithAnalysisResultDao{

  @Override
  public StockTradeInfoWithAnalysisResult findLatestDateStockTradeInfo(String stockSymbol) {
    HibernateTemplate temp=this.getHibernateTemplate();
    temp.setMaxResults(1);
    List<StockTradeInfoWithAnalysisResult> result=(List<StockTradeInfoWithAnalysisResult>)temp.find("from StockTradeInfoWithAnalysisResult s  where s.stock.symbol=? order by s.tradeDate desc ", stockSymbol);
    temp.setMaxResults(-1);
    if(result!=null&&result.size()>0){
        return result.get(0);
    }
    return null;
  }

  @Override
  public void batchInsertInfos(List<StockTradeInfoWithAnalysisResult> infors) {
    if (infors!=null&&infors.size()>0) {
      for (StockTradeInfoWithAnalysisResult stockTradeInfoWithAnalysisResult : infors) {
        this.getHibernateTemplate().save(stockTradeInfoWithAnalysisResult);
      }
    }
  }

  @Override
  public List<StockTradeInfoWithAnalysisResult> findAllInforDateAsc(String stockSymbol) {
    return (List<StockTradeInfoWithAnalysisResult>)this.findByHql("from StockTradeInfoWithAnalysisResult t where t.stock.symbol=?  order by t.tradeDate asc", stockSymbol);
  }

  @Override
  public List<StockTradeInfoWithAnalysisResult> findLimitInfosBeforeDateDateAsc(String stockSymbol, Date date, int limit) {
    String hql="from StockTradeInfoWithAnalysisResult t where t.stock.symbol=?  and t.tradeDate<? order by t.tradeDate asc";
    long count=this.countHqlQuery(hql, stockSymbol,date);
    Query query=this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
    if (count>limit) {
      query.setFirstResult((int)(count-limit));
    }
    query.setMaxResults(limit);
    return query.list();
  }

}
