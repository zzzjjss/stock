package com.uf.stock.data.sync;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.stock.data.bean.ConfigInfo;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfoWithAnalysisResult;
import com.uf.stock.data.exception.DataSyncException;
import com.uf.stock.util.HttpUnit;

public class BaiduDataSynchronizer {
  private  ConfigInfo configInfo; 
  private CloseableHttpClient client ;
  public BaiduDataSynchronizer(ConfigInfo configInfo){
    this.configInfo=configInfo;
    client = HttpUnit.createHttpClient(configInfo);
  }

  public StockTradeInfoWithAnalysisResult syncCurrentStockTradeInfoWithAnalysisResult(String stockSymbol) throws DataSyncException {
    StockTradeInfoWithAnalysisResult result = null;
    String urlString = "http://gupiao.baidu.com/api/stocks/stockdaybar?from=pc&os_ver=1&cuid=xxx&vv=100&format=json&stock_code=" + stockSymbol + "&step=3&start=&count=10&fq_type=front&timestamp=" + System.currentTimeMillis();
    HttpGet getMethod = new HttpGet(urlString);
    CloseableHttpResponse response = null;
    try {
      response = client.execute(getMethod);
      int status = response.getStatusLine().getStatusCode();
      if (status == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, Charset.forName("gb2312"));
        List<StockTradeInfoWithAnalysisResult> results=jsonToStockTradeInfoWithAnalysisResult(stockSymbol,responseContent);
        if (results!=null&&results.size()>=0) {
          result=results.get(0);
        }
      } else {
        throw new DataSyncException("http response statu code is not 200,the statu code is:" + status);
      }
    } catch (Exception e) {
      System.out.println("error parse ->" + urlString);
      throw new DataSyncException("dataSyncFromBaiduException ", e);
    } finally {
      try {
        if (response != null)
          response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }
  private List<StockTradeInfoWithAnalysisResult> jsonToStockTradeInfoWithAnalysisResult(String stockSymbol,String jsonString){
    String stockCode=stockSymbol.substring(2);
    DateFormat format=new SimpleDateFormat("yyyyMMdd");
    List<StockTradeInfoWithAnalysisResult> result=new LinkedList<StockTradeInfoWithAnalysisResult>();
    if(StringUtils.isBlank(jsonString)||"{}".equals(StringUtils.reverseDelimited(jsonString,'\n'))){
      return result;
  }
  JsonParser parser=new JsonParser();
  JsonObject jsonObj=parser.parse(jsonString).getAsJsonObject();
  if(jsonObj.get("errorNo").getAsInt()==0){
    if(jsonObj.get("mashData")==null){
      return result;
    }
    JsonArray  tradeDatas=jsonObj.get("mashData").getAsJsonArray();
    if (tradeDatas==null||tradeDatas.size()==0) {
      return result;
    }
    for(int i=0;i<tradeDatas.size();i++){
      JsonObject   oneDayData=tradeDatas.get(i).getAsJsonObject();
      int tradeDateInt=oneDayData.get("date").getAsInt();
      Date tradeDate=null;
      try {
        tradeDate = format.parse(String.valueOf(tradeDateInt));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      JsonObject klineObject=oneDayData.get("kline").getAsJsonObject();
      JsonObject kdjObject=oneDayData.get("kdj").getAsJsonObject();
      JsonObject ma5Object=oneDayData.get("ma5").getAsJsonObject();
      JsonObject ma10Object=oneDayData.get("ma10").getAsJsonObject();
      JsonObject ma20Object=oneDayData.get("ma20").getAsJsonObject();
      JsonObject macdObject=oneDayData.get("macd").getAsJsonObject();
      JsonObject rsiObject=oneDayData.get("rsi").getAsJsonObject();
      StockTradeInfoWithAnalysisResult info=new StockTradeInfoWithAnalysisResult();
      StockInfo stock=new StockInfo();
      stock.setSymbol(stockSymbol);
      stock.setCode(Integer.parseInt(stockCode));
      info.setStock(stock);
      info.setStockSymbol(stockSymbol);
      info.setClosePrice(klineObject.get("close").getAsFloat());
      info.setDeaMACD(macdObject.get("dea").getAsFloat());
      info.setDiffMACD(macdObject.get("diff").getAsFloat());
      info.setdKDJ(kdjObject.get("d").getAsFloat());
      info.setHighestPrice(klineObject.get("high").getAsFloat());
      info.setjKDJ(kdjObject.get("j").getAsFloat());
      info.setkKDJ(kdjObject.get("k").getAsFloat());
      info.setLowestPrice(klineObject.get("low").getAsFloat());
      info.setMa10Price(ma10Object.get("avgPrice").getAsFloat());
      info.setMa10Volume(ma10Object.get("volume").getAsLong());
      info.setMa20Price(ma20Object.get("avgPrice").getAsFloat());
      info.setMa20Volume(ma20Object.get("volume").getAsLong());
      info.setMa5Price(ma5Object.get("avgPrice").getAsFloat());
      info.setMa5Volume(ma5Object.get("volume").getAsLong());
      info.setMacdMACD(macdObject.get("macd").getAsFloat());
      info.setOpenPrice(klineObject.get("open").getAsFloat());
      info.setPreClosePrice(klineObject.get("preClose").getAsFloat());
      info.setRsi1(rsiObject.get("rsi1").getAsFloat());
      info.setRsi2(rsiObject.get("rsi2").getAsFloat());
      info.setRsi3(rsiObject.get("rsi3").getAsFloat());
      info.setTradeAmount(klineObject.get("volume").getAsLong());
      info.setTradeDate(tradeDate);
      info.setTradeMoney(klineObject.get("amount").getAsLong());
      info.setUpDownPrice(info.getClosePrice()-info.getOpenPrice());
      info.setUpDownRate(klineObject.get("netChangeRatio").getAsFloat());
      result.add(info);
    }
  }else {
    System.out.println("errorNo is  not  0, try again!");
  }
    return result;
  }
  
  public List<StockTradeInfoWithAnalysisResult> syncStockDateTradeInfosWithAnalysisResult(String stockSymbol,Date existLatestDataDate) throws DataSyncException {
    List<StockTradeInfoWithAnalysisResult> result=new LinkedList<StockTradeInfoWithAnalysisResult>();
    String stockCode=stockSymbol.substring(2);
    DateFormat format=new SimpleDateFormat("yyyyMMdd");
    Date startDate=new Date();
    out: while(true){
      String start=format.format(startDate);
      int count=160;
      String urlString="http://gupiao.baidu.com/api/stocks/stockdaybar?from=pc&os_ver=1&cuid=xxx&vv=100&format=json&stock_code="+stockSymbol+"&step=3&start="+start+"&count="+count+"&fq_type=front&timestamp="+System.currentTimeMillis();
      HttpGet getMethod = new HttpGet(urlString);
      CloseableHttpResponse response = null;
      try {
        response=client.execute(getMethod);
        int status = response.getStatusLine().getStatusCode();
        if (status == HttpStatus.SC_OK) {
          HttpEntity entity = response.getEntity();
          String responseContent = EntityUtils.toString(entity, Charset.forName("gb2312"));
          System.out.println(responseContent);
          if(StringUtils.isBlank(responseContent)||"{}".equals(StringUtils.reverseDelimited(responseContent,'\n'))){
              return result;
          }
          JsonParser parser=new JsonParser();
          JsonObject jsonObj=parser.parse(responseContent).getAsJsonObject();
          if(jsonObj.get("errorNo").getAsInt()==0){
            if(jsonObj.get("mashData")==null){
              break;
            }
            JsonArray  tradeDatas=jsonObj.get("mashData").getAsJsonArray();
            if (tradeDatas==null||tradeDatas.size()==0) {
              break;
            }
            for(int i=0;i<tradeDatas.size();i++){
              JsonObject   oneDayData=tradeDatas.get(i).getAsJsonObject();
              int tradeDateInt=oneDayData.get("date").getAsInt();
              if (tradeDateInt<=20100100) {
                break out;
              }
              if(existLatestDataDate!=null){
                int existDataDateInt=Integer.parseInt(format.format(existLatestDataDate));
                if (tradeDateInt<=existDataDateInt) {
                  break out;
                }
              }
              String tradeDate=String.valueOf(tradeDateInt);
              startDate=format.parse(tradeDate);
              JsonObject klineObject=oneDayData.get("kline").getAsJsonObject();
              JsonObject kdjObject=oneDayData.get("kdj").getAsJsonObject();
              JsonObject ma5Object=oneDayData.get("ma5").getAsJsonObject();
              JsonObject ma10Object=oneDayData.get("ma10").getAsJsonObject();
              JsonObject ma20Object=oneDayData.get("ma20").getAsJsonObject();
              JsonObject macdObject=oneDayData.get("macd").getAsJsonObject();
              JsonObject rsiObject=oneDayData.get("rsi").getAsJsonObject();
              StockTradeInfoWithAnalysisResult info=new StockTradeInfoWithAnalysisResult();
              StockInfo stock=new StockInfo();
              stock.setSymbol(stockSymbol);
              stock.setCode(Integer.parseInt(stockCode));
              info.setStock(stock);
              info.setStockSymbol(stockSymbol);
              info.setClosePrice(klineObject.get("close").getAsFloat());
              info.setDeaMACD(macdObject.get("dea").getAsFloat());
              info.setDiffMACD(macdObject.get("diff").getAsFloat());
              info.setdKDJ(kdjObject.get("d").getAsFloat());
              info.setHighestPrice(klineObject.get("high").getAsFloat());
              info.setjKDJ(kdjObject.get("j").getAsFloat());
              info.setkKDJ(kdjObject.get("k").getAsFloat());
              info.setLowestPrice(klineObject.get("low").getAsFloat());
              info.setMa10Price(ma10Object.get("avgPrice").getAsFloat());
              info.setMa10Volume(ma10Object.get("volume").getAsLong());
              info.setMa20Price(ma20Object.get("avgPrice").getAsFloat());
              info.setMa20Volume(ma20Object.get("volume").getAsLong());
              info.setMa5Price(ma5Object.get("avgPrice").getAsFloat());
              info.setMa5Volume(ma5Object.get("volume").getAsLong());
              info.setMacdMACD(macdObject.get("macd").getAsFloat());
              info.setOpenPrice(klineObject.get("open").getAsFloat());
              info.setPreClosePrice(klineObject.get("preClose").getAsFloat());
              info.setRsi1(rsiObject.get("rsi1").getAsFloat());
              info.setRsi2(rsiObject.get("rsi2").getAsFloat());
              info.setRsi3(rsiObject.get("rsi3").getAsFloat());
              info.setTradeAmount(klineObject.get("volume").getAsLong());
              info.setTradeDate(startDate);
              info.setTradeMoney(klineObject.get("amount").getAsLong());
              info.setUpDownPrice(info.getClosePrice()-info.getOpenPrice());
              info.setUpDownRate(klineObject.get("netChangeRatio").getAsFloat());
              result.add(info);
            }
          }else {
            System.out.println("errorNo is  not  0, try again!");
          }
        }else{
          throw new DataSyncException("http response statu code is not 200,the statu code is:"+status);
        }
      } catch (Exception e) {
        System.out.println("error parse ->" + urlString);
        throw new DataSyncException("dataSyncFromBaiduException ",e);
      } finally {
        try {
          if(response!=null)
            response.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("total sync:"+result.size());
    return result;
  }
  
  
}
