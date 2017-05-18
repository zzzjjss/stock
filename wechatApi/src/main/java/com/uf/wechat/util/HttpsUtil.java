package com.uf.wechat.util;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpsUtil {
private static CloseableHttpClient httpClient;
static{
	HttpClientBuilder clientBuilder=HttpClients.custom().setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(600*1000)
			.build()).setMaxConnTotal(1000).setMaxConnPerRoute(800);
	httpClient=	clientBuilder.build();
}

	public static JsonObject httpGet_ResponseJson(String url) {
		try {
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(request);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
				Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
				if (header != null ) {
					ContentType type=ContentType.create(header.getValue());
					if (!type.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
						System.err.println("response contentType is not applicaion/json,is :"+type.toString());
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
public static String httpGet_ResponseString(String url){
	try {
		HttpGet request = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(request);
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
public static  JsonObject httpPostJson_ResponseJson(String url,JsonObject jsonContent){
	try {
		HttpPost request = new HttpPost(url);
		request.setEntity(new ByteArrayEntity(jsonContent.toString().getBytes(ContentType.APPLICATION_JSON.getCharset()), ContentType.APPLICATION_JSON));
		CloseableHttpResponse response = httpClient.execute(request);
		StatusLine statusLine = response.getStatusLine();
		if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
			Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
			if (header != null ) {
				ContentType type=ContentType.create(header.getValue());
				if (!type.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
					System.err.println("response contentType is not applicaion/json,is :"+type.toString());
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
public static String httpPostJson_ResponseString(String url,JsonObject jsonContent){
	try {
		HttpPost request = new HttpPost(url);
		request.setEntity(new ByteArrayEntity(jsonContent.toString().getBytes(ContentType.APPLICATION_JSON.getCharset()), ContentType.APPLICATION_JSON));
		CloseableHttpResponse response = httpClient.execute(request);
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

public static String httpPostXml_ResponseString(String url,String xml){
	try {
		HttpPost request = new HttpPost(url);
		request.setEntity(new ByteArrayEntity(xml.getBytes(ContentType.APPLICATION_XML.getCharset()), ContentType.APPLICATION_XML));
		CloseableHttpResponse response = httpClient.execute(request);
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
public static JsonObject httpPostFormUrlEncodedData_ResponseJson(String url,Map<String, String> formDatas){
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
		CloseableHttpResponse response = httpClient.execute(request);
		StatusLine statusLine = response.getStatusLine();
		if (statusLine != null && statusLine.getStatusCode() == HttpStatus.SC_OK) {
			Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
			if (header != null ) {
				ContentType type=ContentType.create(header.getValue());
				if (!type.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType())) {
					System.err.println("response contentType is not applicaion/json,is :"+type.toString());
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
			CloseableHttpResponse response = httpClient.execute(request);
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

