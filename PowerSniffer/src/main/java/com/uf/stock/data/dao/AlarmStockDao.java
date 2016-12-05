package com.uf.stock.data.dao;

import com.uf.stock.data.bean.AlarmStock;

public interface AlarmStockDao extends CommonDao<AlarmStock>{
  public AlarmStock  findByStockCode(Integer stockCode);
}
