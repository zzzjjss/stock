package com.uf.store.service.searcher.wordtree;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

public class WordTree {
  private CharacterNode rootNode=new CharacterNode();
  public void addWord(String word){
    CharacterNode node=rootNode;
    for(int i=0;i<word.length();i++){
      char c=word.charAt(i);
      CharacterNode childNode=lookforChildNode(node,c);
      if(childNode==null){
        CharacterNode charNode=new CharacterNode();
        Character charObj=new Character(c);
        charNode.setCharacter(charObj);
        charNode.setParentNode(node);
        if(i==(word.length()-1)){
          charNode.setWordEnd(true);
        }
        node.getChilderNodes().put(charObj, charNode);
        node=charNode;
      }else{
        if(i==(word.length()-1)){
          childNode.setWordEnd(true);
        }
        node=childNode;
      }
    }
  }
  public List<String> parseWords(String input){
    List<String>  results=new ArrayList<String>();
   if(!Strings.isNullOrEmpty(input)){
     for(int i=0;i<input.length();i++){
       String subInput=input.substring(i);
       //TODO May use  Threadpool  to optimize
       List<String> result=parseOnePathWords(subInput);
       results.addAll(result);
     }
   }
   return results;
  }
  
  public List<String> parseWords(InputStream input,String charsetName) throws UnsupportedEncodingException{
    StopWordReader reader=new StopWordReader(new InputStreamReader(input,charsetName));
    List<String>  results=new ArrayList<String>();
    String segment=null;
    try{
      while((segment=reader.readSegmentation())!=null){
        List<String> parsedWords=parseWords(segment);
        results.addAll(parsedWords);
      }
    }catch(IOException e){
      e.printStackTrace();
    }
    return results;
  }
  
  
  private List<String> parseOnePathWords(String input){
    List<String> result=new ArrayList<String>();
    if(input!=null&&!input.isEmpty()){
      char[] chars=input.toCharArray();
      String word="";
      Map<Character,CharacterNode> childs=rootNode.getChilderNodes();
      for(char c:chars){
        CharacterNode node=childs.get(new Character(c));
        if(node!=null){
          word=word+c;
          if(node.isWordEnd()){
            result.add(word);
            if(node.getChilderNodes().size()<=0){
              return result;
            }
          }
          childs=node.getChilderNodes();
        }else{
          return result;
        }
      }
    }
    return result;
  }
  
  public void printAllWord(){
    printWord(rootNode,"");
  }
  private  void printWord(CharacterNode  node,String preChars){
    Map<Character,CharacterNode> childs=node.getChilderNodes();
    if(node.isWordEnd()){
      System.out.println(preChars);
    }
    if(childs!=null&&childs.size()>0){
      for(CharacterNode child:childs.values()){
        printWord(child,preChars+child.getCharacter().charValue());
      }
    }
  }
  
  private CharacterNode lookforChildNode(CharacterNode node,char c){
    if(node==null)
      return null;
    Map<Character,CharacterNode> childerNodes=node.getChilderNodes();
    if(childerNodes==null||childerNodes.size()<=0){
      return null;
    }
    return childerNodes.get(new Character(c));
  }
  
  
}
