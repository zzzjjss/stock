package com.uf.stock.data.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.dao.StockTradeInfoDao;

@Component("stockTradeInfoDao")
public class StockTradeInfoDaoImpl extends CommonRdbsDaoImpl<StockTradeInfo> implements StockTradeInfoDao {

  @Override
  public StockTradeInfo findLatestDateStockTradeInfo(String stockSymbol) {
      HibernateTemplate temp=this.getHibernateTemplate();
      temp.setMaxResults(1);
      List<StockTradeInfo> result=(List<StockTradeInfo>)temp.find("from StockTradeInfo s  where s.stockSymbol=? order by s.tradeDate desc ", stockSymbol);
      if(result!=null&&result.size()>0){
          return result.get(0);
      }
      return null;
  }

@Override
public Float calculateAveragePriceBeforeDate(int limit, Date date,Integer stockCode) {
	HibernateTemplate temp=this.getHibernateTemplate();
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select avg(close_price) from (select * from stock_trade_info where  stock_code="+stockCode+" and trade_date <='"+format.format(date)+"' order by trade_date desc  limit "+limit+") as a;");
	Double avg=(Double)query.uniqueResult();
	if(avg!=null){
	  return avg.floatValue();
	}else{
	  return 0f;
	}
}

@Override
public Float calculateAverageTurnoverRateBeforeDate(int limit, Date date,Integer stockCode) {
    HibernateTemplate temp=this.getHibernateTemplate();
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select avg(turnover_rate) from (select * from stock_trade_info where  stock_code="+stockCode+" and trade_date <='"+format.format(date)+"' order by trade_date desc  limit "+limit+") as a;");
    Double avg=(Double)query.uniqueResult();
    if(avg!=null){
      return avg.floatValue();
    }else{
      return 0f;
    }
}
@Override
public List<StockTradeInfo> findLatestDaysStockTradeInfos(String stockSymbol, int days) {
  HibernateTemplate temp=this.getHibernateTemplate();
  temp.setMaxResults(days);
  return (List<StockTradeInfo>)temp.find("from StockTradeInfo s  where s.stockSymbol=? order by s.tradeDate desc ", stockSymbol);
}

@Override
public Float calculateDaysHighestPriceBeforeDate(int limit, Date date, Integer stockCode) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select max(highest_price) from (select * from stock_trade_info where  stock_code="+stockCode+" and trade_date <='"+format.format(date)+"' order by trade_date desc  limit "+limit+") as a;");
  return (Float)query.uniqueResult();
}

@Override
public Float calculateDaysLowestPriceBeforeDate(int limit, Date date, Integer stockCode) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select min(lowest_price) from (select * from stock_trade_info where  stock_code="+stockCode+" and trade_date <='"+format.format(date)+"' order by trade_date desc  limit "+limit+") as a;");
  return (Float)query.uniqueResult();
}

@Override
public int exrightBeforeDate(Integer stockCode, Date date,Float exPercent) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("update stock_trade_info set open_price=open_price*"+exPercent+",close_price=close_price*"+exPercent+",highest_price=highest_price*"+exPercent+",lowest_price=lowest_price*"+exPercent+"  where stock_code="+stockCode+" and trade_date <'"+format.format(date)+"'");
  return query.executeUpdate();
}

@Override
public float calculateAvgPriceBeforeDate(Integer stockCode, Date date) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select avg(close_price) from stock_trade_info where  stock_code="+stockCode+" and trade_date <='"+format.format(date)+"'");
  Double avg=(Double)query.uniqueResult();
  if(avg!=null){
    return avg.floatValue();
  }else{
    return 0f;
  }  
}

@Override
public float calculateLowestPrice(Integer stockCode) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select min(lowest_price) from stock_trade_info where  stock_code="+stockCode);
  Float lowest=(Float)query.uniqueResult();
  if(lowest!=null){
    return lowest.floatValue();
  }else{
    return 0f;
  }
}

@Override
public float calculateLowestPriceBeforeDate(Integer stockCode, Date date) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select min(lowest_price) from stock_trade_info where  stock_code="+stockCode+" and trade_date <='"+format.format(date)+"'");
  Float lowest=(Float)query.uniqueResult();
  if(lowest!=null){
    return lowest.floatValue();
  }else{
    return 0f;
  }
}

@Override
public float calculateAvgTurnoverRate(Integer stockCode) {
  HibernateTemplate temp=this.getHibernateTemplate();
  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select avg(turnover_rate) from stock_trade_info where  stock_code="+stockCode);
  Double avg=(Double)query.uniqueResult();
  if(avg!=null){
    return avg.floatValue();
  }else{
    return 0f;
  }  
}

@Override
public List<StockTradeInfo> findTradeInfosBeforeDate(Integer stockCode, Date date, int limitDays) {
  HibernateTemplate temp=this.getHibernateTemplate();
  String hql="from StockTradeInfo t where t.stock.code=? and t.tradeDate<=? order by t.tradeDate asc";
  long count=countHqlQuery(hql, stockCode,date);
  Query query=temp.getSessionFactory().getCurrentSession().createQuery(hql);
  query.setMaxResults(limitDays);
  int firstResult=0;
  if (count>limitDays) {
    firstResult=(int)(count-limitDays);
  }
  query.setFirstResult(firstResult);
  query.setParameter(0, stockCode);
  query.setParameter(1, date);
  return (List<StockTradeInfo>)query.list();
}

@Override
public Date getLatestDate() {
	HibernateTemplate temp=this.getHibernateTemplate();
	  SQLQuery query=temp.getSessionFactory().getCurrentSession().createSQLQuery("select max(trade_date) from stock_trade_info ");
	  Date date=(Date)query.uniqueResult();
	  return date;
}

@Override
public StockTradeInfo findLatestDateStockTradeInfo(Integer stockCode) {
	HibernateTemplate temp=this.getHibernateTemplate();
    temp.setMaxResults(1);
    List<StockTradeInfo> result=(List<StockTradeInfo>)temp.find("from StockTradeInfo s  where s.stock.code=? order by s.tradeDate desc ", stockCode);
    temp.setMaxResults(-1);
    if(result!=null&&result.size()>0){
        return result.get(0);
    }
    return null;
}

public long countHql(String hql,Object... paramValue){
  return countHqlQuery(hql,paramValue);
}



}
