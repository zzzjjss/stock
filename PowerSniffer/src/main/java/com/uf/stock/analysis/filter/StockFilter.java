package com.uf.stock.analysis.filter;

import com.uf.stock.data.bean.StockTradeInfo;

public interface StockFilter {
  public FilterResult  doFilter(StockTradeInfo tradeInfo);
}
