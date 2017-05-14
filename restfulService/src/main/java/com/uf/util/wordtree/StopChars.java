package com.uf.util.wordtree;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.google.common.io.LineReader;

public class StopChars {
  private static StopChars  instance=new StopChars();
  private Set<Character> stopChars=new HashSet<Character>();
  private StopChars(){
    InputStream input=StopChars.class.getResourceAsStream("/stopChar.txt");
    LineReader reader=new LineReader(new InputStreamReader(input)); 
    String line=null;
    try{
      while((line=reader.readLine())!=null){
        stopChars.add(new  Character(line.charAt(0)));
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public static StopChars getInstance(){
    return instance;
  }
  
  public boolean isStopChar(char c){
    return stopChars.contains(new Character(c));
  }
  
}
