package com.uf.stock.util;

import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.uf.stock.data.bean.ConfigInfo;
public class HttpUnit {
	public static  CloseableHttpClient createHttpClient(ConfigInfo config){
		HttpClientBuilder clientBuilder=HttpClients.custom().setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(120*1000)
				.build()).setMaxConnTotal(1000).setMaxConnPerRoute(800);
		if (config.isUseProxy()) {
			clientBuilder.setProxy(new HttpHost(config.getProxyAddress(), config.getProxyPort()));
		}
		return clientBuilder.build();
	}
	public static CloseableHttpClient createNotimeoutHttpClient(){
	  ConfigInfo config=SpringBeanFactory.getBean(ConfigInfo.class);
		HttpClientBuilder clientBuilder=HttpClients.custom();
		if (config.isUseProxy()) {
			clientBuilder.setProxy(new HttpHost(config.getProxyAddress(), config.getProxyPort()));
		}
		return clientBuilder.build();
	}
	
}
