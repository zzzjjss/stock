package com.uf.stock.util;

public class StockUtil {
public static Integer parseStockSymbolToStockCode(String stockSymbol){
  return Integer.parseInt(stockSymbol.substring(2));
}
}
