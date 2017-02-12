package com.uf.stock.statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.uf.stock.analysis.LowPriceUpStockFilterChain;
import com.uf.stock.analysis.TargetDefinition;
import com.uf.stock.analysis.filter.KLineTFilter;
import com.uf.stock.analysis.filter.PriceFilter;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;
import com.uf.stock.util.StockUtil;

public class StatisticsTool {
	private DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);

	public void statisticBuyPointByKline(String stockSymbol) {
		{
			TargetDefinition targetDefin = new TargetDefinition(5, 2f);
			StockInfo stock = service.findStockInfoByStockSymbol(stockSymbol);
			if (stock == null) {
				throw new IllegalArgumentException("stockSymbol is invalid");
			}

			List<StockTradeInfo> tradeInfos = service.findAllTradeInfosOrderByDateAsc(stock.getCode());
			
			if (tradeInfos != null) {
				LowPriceUpStockFilterChain lowPriceChain = new LowPriceUpStockFilterChain();
				lowPriceChain.appendStockFilter(new PriceFilter(tradeInfos, 50f));
				LowPriceUpStockFilterChain klineChain = new LowPriceUpStockFilterChain();
				klineChain.appendStockFilter(new KLineTFilter());
				int i = 0;
				if (tradeInfos.size() > 100) {
					i = tradeInfos.size() - 100;
				}
				List<Float> passTrade=new ArrayList<Float>();
				List<Float> noPassTrade=new ArrayList<Float>();
				for (; i < tradeInfos.size(); i++) {
					StockTradeInfo stockTradeInfo = tradeInfos.get(i);
					boolean isPass = lowPriceChain.doFilter(stockTradeInfo);
					if (isPass) {
						int days=StockUtil.howmanyDaysToTargetUpPercent(tradeInfos,i,targetDefin.getUpPercent());
						klineChain.doFilter(stockTradeInfo);
						Collection<Float> values=klineChain.getFilterChainResult().values();
						if(values!=null&&values.size()>0){
							if(days<=targetDefin.getDays()){
								passTrade.add((Float)(values.toArray()[0]));
							}else{
								noPassTrade.add((Float)(values.toArray()[0]));
							}
						}
						System.out.println("after days:" + days + " up to targetUpPercent");
					}
				}
				Collections.sort(passTrade);
				Collections.sort(noPassTrade);
				System.out.println(passTrade);
				System.out.println(noPassTrade);
			}
		}
	}
	
}
