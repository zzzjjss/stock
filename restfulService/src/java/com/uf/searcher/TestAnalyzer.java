package com.uf.searcher;

import java.io.StringReader;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

public class TestAnalyzer {
  private static String chinese = "余仁生香砂六君丸  功效  ：益气健脾，和胃。用于脾虚气滞，消化不良，嗳气食少，脘腹胀满，大便溏泄。。主治  ：脾胃气虚，痰阻气滞所致脘腹胀痛，消瘦倦怠，不思饮食，呕吐，偶发性腹泻，气逆，嗳气食少"+
                                  "主要成份 ：炙甘草，茯苓，白朮，砂仁，党参，木香，法半夏，陈皮。 包裝  ：每盒18包，每包7.5克。";  
  private Analyzer analyzer = null;  
    
  public void testStandarAnalyzer() throws Exception{  
      analyzer = new StandardAnalyzer();  
      testAnalyzer(analyzer, chinese);  
  }  
    
  public void testSimpleAnalyzer() throws Exception{  
      analyzer = new SimpleAnalyzer();  
      testAnalyzer(analyzer, chinese);  
  }  
    
  public void testCJKAnalyzer() throws Exception{  
      analyzer = new CJKAnalyzer();  
      testAnalyzer(analyzer, chinese);  
  }  
  public void testSmartAnalyzer()throws Exception{
   CharArraySet  set=new CharArraySet(Arrays.asList("丸"), true);
    SmartChineseAnalyzer  smartAnalyzer=new SmartChineseAnalyzer(set);
    
    testAnalyzer(smartAnalyzer, chinese);
  }
  public void testMMSegAnalyzer()throws Exception{
    //analyzer=new MMSegAnalyzer();
    //analyzer.setVersion(Version.LUCENE_43);
    testAnalyzer(analyzer, chinese);
  }
    
//  public void testIKAnalyzer() throws Exception{  
//      analyzer = new IKAnalyzer();  
//      testAnalyzer(analyzer, chinese);  
//  }  
  /** 
   * 使用指定的分词器对指定的文本进行分词，并打印出分出的词 
   *  
   * @param analyzer 
   * @param text 
   * @throws Exception 
   */  
  private void testAnalyzer(Analyzer analyzer, String text) throws Exception {  
      System.out.println("分词器：" + analyzer.getClass().getSimpleName());  
      TokenStream tokenStream = analyzer.tokenStream("content",  new StringReader(text));  
      tokenStream.addAttribute(CharTermAttribute.class);
      tokenStream.reset();
      while (tokenStream.incrementToken()) { 
        CharTermAttribute termAttribute = tokenStream .getAttribute(CharTermAttribute.class);  
        System.out.println(termAttribute.toString());  
      }  
      tokenStream.close();
    
  }  
  
  public static void main(String[] args) {
    TestAnalyzer test=new TestAnalyzer();
    try {
      test.testSmartAnalyzer();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
