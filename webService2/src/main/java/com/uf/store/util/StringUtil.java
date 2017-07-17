package com.uf.store.util;

public class StringUtil {
	public static boolean isNullOrEmpty(String value){
		return value==null||value.trim().equals("");
	}
	
	public static String[]  splitStringByEmptySpace(String str){
	  return  str.trim().split("\\s+");
	}
	public static String replaceAllSpecialCharToWhitespace(String str){
	  if(!isNullOrEmpty(str)){
	    return str.replaceAll("[,;.。?<>，；!@#$%^&*()-+]+", " ");
	  }
	  return null;
	}
	
	public static void main(String[] args) {
	  //String a[]=splitStringByEmptySpace("dfd    ddddd ");
	  
	  System.out.println(replaceAllSpecialCharToWhitespace("kfii,fkd.idfdih..发的开发，"));
	  
    }
}
