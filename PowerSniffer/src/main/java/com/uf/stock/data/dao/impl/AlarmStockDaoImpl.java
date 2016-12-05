package com.uf.stock.data.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.uf.stock.data.bean.AlarmStock;
import com.uf.stock.data.dao.AlarmStockDao;

@Component("alarmStockDao")
public class AlarmStockDaoImpl extends CommonRdbsDaoImpl<AlarmStock> implements AlarmStockDao{
  public AlarmStock  findByStockCode(Integer stockCode){
    HibernateTemplate temp=this.getHibernateTemplate();
    temp.setMaxResults(1);
    List<AlarmStock> result=(List<AlarmStock>)temp.find("from AlarmStock s  where s.stockCode=? ", stockCode);
    if(result!=null&&result.size()>0){
        return result.get(0);
    }
    return null;
  }
}
