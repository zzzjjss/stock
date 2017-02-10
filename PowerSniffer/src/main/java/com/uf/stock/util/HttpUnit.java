package com.uf.stock.util;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.uf.stock.data.bean.ConfigInfo;
public class HttpUnit {
	public static  CloseableHttpClient createHttpClient(ConfigInfo config){
		HttpClientBuilder clientBuilder=HttpClients.custom().setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(120*1000)
				.build()).setMaxConnTotal(1000).setMaxConnPerRoute(800);
		if (config.isUseProxy()) {
		  Credentials credentials = new UsernamePasswordCredentials("username","password");
		  AuthScope authScope = new AuthScope(config.getProxyAddress(), config.getProxyPort());
		  CredentialsProvider credsProvider = new BasicCredentialsProvider();
		  credsProvider.setCredentials(authScope, credentials);
		  clientBuilder.setProxy(new HttpHost(config.getProxyAddress(), config.getProxyPort())).setDefaultCredentialsProvider(credsProvider);
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
