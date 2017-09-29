package com.uf.book.robot.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public class BookRobot {
	public static void main(String[] args) {
//		downloadBook("");
		String teString="{\"status\":\"cached\",\"log\":48,\"data\":\"%5B%5B%22https%3A%5C%2F%5C%2Fwww.doujiar.ml%5C%2Ffetch_book.php%3Fq%3DQTQ3WjZlalp4RjkwR0xPZDB3NElDajJyc1hjeWlhY1V6NDJsL0RGdCtDRT0%253D%22%2C%22%5Cu767d%5Cu591c%5Cu884c%20%5Bepub%5D%22%2C%22Size%3A%20479.6%20KB.%22%2C1000%2C0%2C2010%5D%2C%5B%22https%3A%5C%2F%5C%2Fwww.doujiar.ml%5C%2Ffetch_book.php%3Fq%3DaDcrTnRBeG5rUUpaNzgwYVNEbGcvRjVxWVM2NURPNjVoc3paSi9TTytpaz0%253D%22%2C%22%5Cu4e1c%5Cu91ce%5Cu572d%5Cu543e%5B%5Cu65e5%5D%20-%20%5Cu767d%5Cu591c%5Cu884c%20%5Bazw3%5D%22%2C%22Size%3A%20646.72%20KB.%22%2C1000%2C0%2C2010%5D%2C%5B%22https%3A%5C%2F%5C%2Fwww.doujiar.ml%5C%2Ffetch_book.php%3Fq%3DSjY3d2J0SUdBUlhvWXQ5M0JzaUZUeFlUQ2xwcWdPNUZQTllVY3FMR0lxQT0%253D%22%2C%221999%5Cu767d%5Cu591c%5Cu884c%28%5Cu53f0%5Cu7248%29%20-%20%5Cu4e1c%5Cu91ce%5Cu572d%5Cu543e%20%5Bmobi%5D%22%2C%22Size%3A%20861.97%20KB.%22%2C1000%2C0%2C2010%5D%5D\"}";
		URLDecoder decoder=new URLDecoder();
		try {
			System.out.println(decoder.decode(teString,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public static List<File> downloadBook(String bookName){
	Map<String, String> formData=new HashMap<String,String>();
	formData.put("q", "白夜行");
	JsonObject  jsonObject=HttpUtil.httpPostFormUrlEncodedData_ResponseJson("https://www.jiumodiary.com/wordsf_local.php",formData );
	if (jsonObject!=null) {
		String data=jsonObject.get("data").getAsString();
		URLDecoder decoder=new URLDecoder();
		try {
			System.out.println(decoder.decode(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return null;
	
}


}
