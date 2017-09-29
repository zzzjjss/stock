package com.uf.book.robot.schedule;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uf.book.robot.dao.mysql.BookInfo;
import com.uf.book.robot.dao.mysql.BookInfoRepository;
import com.uf.book.robot.util.HttpUtil;

@Component
public class AutoSyncBookInfo {
	@Autowired
	private BookInfoRepository bookInfoRepo;
	
	@Scheduled(fixedDelay = 1000 * 3600 * 24)
	public void incrementSyncBookInfo() {
		int concurrent=10;
		Executor pool=Executors.newFixedThreadPool(concurrent);
		CloseableHttpClient httpClient=HttpUtil.createHttpClient(concurrent, null, null, null);
		String  startIndex="1";
		BookInfo bookInfo=bookInfoRepo.findTopByOrderByIdDesc();
		if (bookInfo!=null) {
			startIndex=bookInfo.getDoubanId();
		}
		int startId=Integer.parseInt(startIndex);
		int end=10000000;
		for(int i=startId;i<end;i++) {
			String doubanId=String.format("%07d", i);
			pool.execute(new Runnable() {
				@Override
				public void run() {
					BookInfo bookInfo=getBookInfoFromDouban(httpClient, doubanId);
					if (bookInfo!=null) {
						bookInfoRepo.save(bookInfo);
					}
				}
			});
		}
	}
	
	private BookInfo getBookInfoFromDouban(HttpClient httpClient,String doubanId) {
		BookInfo bookInfo=new BookInfo();
		String url="https://api.douban.com/v2/book/"+doubanId;
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse  response=httpClient.execute(get);
			if (response!=null&&response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
				System.out.println("url:"+url+"  response code:"+response.getStatusLine().getStatusCode());
				return null;
			}
			String bookInfoJson=EntityUtils.toString(response.getEntity());
			ObjectMapper objMap=new ObjectMapper();
			JsonNode node=objMap.readTree(bookInfoJson);
			String bookName=node.get("title").asText();
			JsonNode authorNode=node.get("author");
			String avgScore=node.get("rating").get("average").asText();
			if (authorNode.isArray()&&authorNode.get(0)!=null) {
				String author=authorNode.get(0).asText();
				bookInfo.setAuthor(author);
			}
			bookInfo.setAvgScore(Float.parseFloat(avgScore));
			bookInfo.setDoubanId(doubanId);
			bookInfo.setBookName(bookName);
			return bookInfo;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
