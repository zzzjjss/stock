package com.uf.stock.banker;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.util.SpringBeanFactory;

public class BankerSnifferTest {

	@Test
	public void test() {
		DataSyncService service = SpringBeanFactory.getBean(DataSyncService.class);
		BankerSniffer sniffer=new BankerSniffer(); 
		List<StockInfo> stocks=service.findStocksPeRatioBetween(-1f, 100000f);
		for(StockInfo stock:stocks){
			float index=sniffer.calculateOpenPositionIndex(stock.getCode());
			System.out.println(stock.getSymbol()+":"+index);
		}
	}

}
