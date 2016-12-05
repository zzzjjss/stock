package com.uf.stock.data.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.dao.StockInfoDao;


@Component("stockInfoDao")
public class StockInfoDaoImpl extends CommonRdbsDaoImpl<StockInfo> implements StockInfoDao {
  

  public StockInfo findStockBySymbol(String stockSymbol) {
    List<StockInfo> result=(List<StockInfo>)this.findByHql("from StockInfo s  where s.symbol=?", stockSymbol);
    if(result!=null&&result.size()>0){
      return result.get(0);
    }
    return null;
  }

  public Integer deleteAll() {
    return this.executeUpdateHql("delete from StockInfo");
  }
  public StockInfo findStockByStockCode(Integer stockCode){
    List<StockInfo> result=(List<StockInfo>)this.findByHql("from StockInfo s  where s.code=?", stockCode);
    if(result!=null&&result.size()>0){
      return result.get(0);
    }
    return null;
  }

}
