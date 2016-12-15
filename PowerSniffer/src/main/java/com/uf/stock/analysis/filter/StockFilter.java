package com.uf.stock.analysis.filter;

import java.util.Date;

import com.uf.stock.data.bean.StockInfo;

public interface StockFilter {
  public Boolean doFilter(StockInfo stock,Date date);
}
