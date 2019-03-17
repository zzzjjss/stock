package com.uf.stock.data.sync;

import java.util.LinkedList;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.uf.stock.data.bean.ConfigInfo;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.util.HttpUnit;

public class QqDataSynchronizer {
	private CloseableHttpClient client;
	private ConfigInfo configInfo;

	public QqDataSynchronizer(ConfigInfo configInfo) {
		this.configInfo = configInfo;
		client = HttpUnit.createHttpClient(configInfo);
	}

	public List<StockInfo> syncAllStocksInfo() {
		List<StockInfo> all = new LinkedList<StockInfo>();
		HttpGet get = new HttpGet("http://stock.gtimg.cn/data/get_hs_xls.php?id=ranka&type=1&metric=chr");
		try {
			CloseableHttpResponse response = client.execute(get);
			Workbook workbook = new HSSFWorkbook(response.getEntity().getContent());
			Sheet sheet = workbook.getSheetAt(0);
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
			for (int rowNum = firstRowNum + 2; rowNum <= lastRowNum; rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				StockInfo stockInfo = new StockInfo();
				String symbol = row.getCell(0).getStringCellValue();
				String name = row.getCell(1).getStringCellValue();
				stockInfo.setSymbol(symbol);
				stockInfo.setName(name);
				stockInfo.setCode(Integer.parseInt(symbol.substring(2)));
				all.add(stockInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all;
	}
}
