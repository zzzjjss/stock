package com.uf.book.robot.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class HttpUtil {
	public static CloseableHttpClient createHttpClient(Integer maxConcurrent, String proxyHost, String proxyPort,
			Map<AuthScope, Credentials> credentials) {
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(30 * 1000).build();
		HttpClientBuilder builder = HttpClientBuilder.create()
				.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).setDefaultSocketConfig(socketConfig)
				.setMaxConnPerRoute(maxConcurrent == null || maxConcurrent.intValue() == 0 ? 10 : maxConcurrent)
				.setMaxConnTotal(1000);
		if (StringUtils.isNotBlank(proxyHost)) {
			int port = 80;
			if (StringUtils.isNumeric(proxyPort.trim())) {
				port = Integer.parseInt(proxyPort.trim());
			}
			HttpHost proxy = new HttpHost(proxyHost, port);
			builder.setProxy(proxy);
		}
		if (credentials != null && credentials.size() > 0) {
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			for (Map.Entry<AuthScope, Credentials> cre : credentials.entrySet()) {
				credentialsProvider.setCredentials(cre.getKey(), cre.getValue());
			}
			builder.setDefaultCredentialsProvider(credentialsProvider);
		}
		return builder.build();
	}

	
	
	private static CloseableHttpClient httpClient=null;
	
	private static synchronized CloseableHttpClient  getHttpClient() {
		if (httpClient==null) {
//	          NTCredentials credential = new NTCredentials("zhangja", "zhangja", "computerName", "ANALYTICS");
//			AuthScope authScope = new AuthScope("proxy-sfo", 80);
//			Map<AuthScope,Credentials>  cre=new HashMap<AuthScope, Credentials>();
//			cre.put(authScope, credential);
//			httpClient=createHttpClient(5, "proxy-sfo", "80", cre);
			httpClient=createHttpClient(5, null,null,null);
		}
		return httpClient;
	} 
	public static JsonObject httpGet_ResponseJson(String url) {
		try {
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
				if (header != null) {
					ContentType type = ContentType.create(header.getValue());
					if (!type.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
						System.err.println("response contentType is not applicaion/json,is :" + type.toString());
					}
				}
				JsonParser parser = new JsonParser();
				JsonElement ele = parser.parse(new InputStreamReader(response.getEntity().getContent()));
				return ele.getAsJsonObject();
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String httpGet_ResponseString(String url) {
		try {
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonObject httpPostJson_ResponseJson(String url, JsonObject jsonContent) {
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(
					new ByteArrayEntity(jsonContent.toString().getBytes(ContentType.APPLICATION_JSON.getCharset()),
							ContentType.APPLICATION_JSON));
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
				if (header != null) {
					ContentType type = ContentType.create(header.getValue());
					if (!type.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
						System.err.println("response contentType is not applicaion/json,is :" + type.toString());
					}
				}
				JsonParser parser = new JsonParser();
				JsonElement ele = parser.parse(new InputStreamReader(response.getEntity().getContent()));
				return ele.getAsJsonObject();
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String httpPostJson_ResponseString(String url, JsonObject jsonContent) {
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(
					new ByteArrayEntity(jsonContent.toString().getBytes(ContentType.APPLICATION_JSON.getCharset()),
							ContentType.APPLICATION_JSON));
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String httpPostXml_ResponseString(String url, String xml) {
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new ByteArrayEntity(xml.getBytes(ContentType.APPLICATION_XML.getCharset()),
					ContentType.APPLICATION_XML));
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonObject httpPostFormUrlEncodedData_ResponseJson(String url, Map<String, String> formDatas) {
		try {
			HttpPost request = new HttpPost(url);
			List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
			if (formDatas != null && formDatas.size() > 0) {
				for (Map.Entry<String, String> entry : formDatas.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					nameValues.add(pair);
				}
				request.setEntity(new UrlEncodedFormEntity(nameValues));
			}
			BasicHeader header1=new BasicHeader("Cookie", "_ga=GA1.2.2064831745.1505196643; _gid=GA1.2.724907585.1505963160; PHPSESSID=lmndtppajlon3jnpf0tr4equf1"); 
		   BasicHeader header2=new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
		   BasicHeader header3=new BasicHeader("X-Requested-With", "XMLHttpRequest"); 
			request.addHeader(header1);
			request.addHeader(header2);
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
				if (header != null) {
					ContentType type = ContentType.create(header.getValue());
					if (!type.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
						System.err.println("response contentType is not applicaion/json,is :" + type.toString());
					}
				}
				JsonParser parser = new JsonParser();
				JsonElement ele = parser.parse(new InputStreamReader(response.getEntity().getContent()));
				return ele.getAsJsonObject();
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String httpPostFormUrlEncodedData_ResponseString(String url, Map<String, String> formDatas) {
		try {
			HttpPost request = new HttpPost(url);
			List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
			if (formDatas != null && formDatas.size() > 0) {
				for (Map.Entry<String, String> entry : formDatas.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					nameValues.add(pair);
				}
				request.setEntity(new UrlEncodedFormEntity(nameValues));
			}
			CloseableHttpResponse response = getHttpClient().execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());
			}
			System.out.println("response http status :" + statusLine.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
