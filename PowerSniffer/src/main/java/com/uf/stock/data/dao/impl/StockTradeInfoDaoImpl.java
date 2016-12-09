package com.uf.stock.data.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
	return avg.floatValue();
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





}
