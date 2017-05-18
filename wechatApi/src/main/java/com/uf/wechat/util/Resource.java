package com.uf.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Resource {
	private static 	Properties properties=new Properties();
	static{
		InputStream  inputStream=Resource.class.getResourceAsStream("/application.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static String getAttribute(String key){
		return properties.getProperty(key);
	}

}
