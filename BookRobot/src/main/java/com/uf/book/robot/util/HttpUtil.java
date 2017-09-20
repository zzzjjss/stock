package com.uf.book.robot.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtil {
	public static CloseableHttpClient createHttpClient(Integer maxConcurrent,String proxyHost,String proxyPort,Map<AuthScope, Credentials> credentials){
		  SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(30 * 1000).build();
		  HttpClientBuilder builder = HttpClientBuilder.create().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
				  						.setDefaultSocketConfig(socketConfig).setMaxConnPerRoute(maxConcurrent==null||maxConcurrent.intValue()==0?10:maxConcurrent)
				  						.setMaxConnTotal(1000);
		  if (StringUtils.isNotBlank(proxyHost)) {
		    int port=80;
		    if (StringUtils.isNumeric(proxyPort.trim())) {
		      port=Integer.parseInt(proxyPort.trim());
		    }
		    HttpHost proxy = new HttpHost(proxyHost, port);
		    builder.setProxy(proxy);
		  }
		  if (credentials!=null&&credentials.size()>0) {
		    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		    for(Map.Entry<AuthScope, Credentials> cre:credentials.entrySet()){
		      credentialsProvider.setCredentials(cre.getKey(), cre.getValue());
		    }
		    builder.setDefaultCredentialsProvider(credentialsProvider);
		  }
		  return  builder.build();
		}
}
