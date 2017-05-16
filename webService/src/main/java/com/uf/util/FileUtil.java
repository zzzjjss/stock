package com.uf.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil {
	public static String getFileExtendName(String  fileName){
		int index=fileName.lastIndexOf(".");
		if(index==-1)
			return "";
		return fileName.substring(index);
	}
	public static  byte[] readFileBytes(File file){
	  if(file.exists()){
	    FileInputStream  input=null;
	    try{
	      ByteArrayOutputStream  byteOutput=new ByteArrayOutputStream();
          input=new FileInputStream(file);
          byte[] buffer = new byte[1024 * 10];
          int bytesRead;
          while ((bytesRead = input.read(buffer)) > -1) {
              byteOutput.write(buffer, 0, bytesRead);
          }
          return byteOutput.toByteArray();
	    }catch(Exception e){
	      e.printStackTrace();
	    }finally{
	      if(input!=null){
  	        try {
              input.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
	      }
	    }
	      
	  }
	  return null;
	}
	public static void writeBytesToFile(byte[] content,File file){
	    FileOutputStream  fileOut=null;
	    try{
	      if(!file.getParentFile().exists()){
	          file.getParentFile().mkdirs();
	        }
	      fileOut=new FileOutputStream(file);
	      fileOut.write(content);
	    }catch(Exception e){
	      e.printStackTrace();
	    }finally{
	      if(fileOut!=null){
	        try {
            fileOut.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
	      }
	    }
	}
	
	public static  void deleteFolderRecusive(File file){
      if(file.exists()){
          if(file.isFile()){
              file.delete();
              return ;
          }else{
              File files[]=file.listFiles();
              if(files!=null&&files.length>0){
                  for(File f:files){
                      deleteFolderRecusive(f);
                  }
              }
              file.delete();
          }
          
      }
  }
}
