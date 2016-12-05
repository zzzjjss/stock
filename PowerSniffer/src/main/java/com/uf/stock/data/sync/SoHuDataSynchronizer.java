package com.uf.stock.data.sync;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uf.stock.data.bean.ConfigInfo;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.data.exception.DataSyncException;
import com.uf.stock.util.HttpUnit;

public class SoHuDataSynchronizer {
  private  ConfigInfo configInfo; 
  private CloseableHttpClient client ;
  public SoHuDataSynchronizer(ConfigInfo configInfo){
    this.configInfo=configInfo;
    client = HttpUnit.createHttpClient(configInfo);
  }
  public List<StockTradeInfo> syncStockDateTradeInfos(String stockSymbol,Date start, Date end) throws DataSyncException {
    List<StockTradeInfo> result=new ArrayList<StockTradeInfo>();
    String stockCode=stockSymbol.substring(2);
    DateFormat format=new SimpleDateFormat("yyyyMMdd");
    
    String url ="http://q.stock.sohu.com/hisHq?code=cn_"+stockCode+"&start="+format.format(start)+"&end="+format.format(end);
    HttpGet getMethod = new HttpGet(url);
    CloseableHttpResponse response = null;
    try {
      response=client.execute(getMethod);
      int status = response.getStatusLine().getStatusCode();
      if (status == HttpStatus.SC_OK) {
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, Charset.forName("gb2312"));
        if(StringUtils.isBlank(responseContent)||"{}".equals(StringUtils.reverseDelimited(responseContent,'\n'))){
        	return result;
        }
        JsonParser parser=new JsonParser();
        JsonObject jsonObj=parser.parse(responseContent.subSequence(1, responseContent.length()-2).toString()).getAsJsonObject();
        DateFormat  format2=new SimpleDateFormat("yyyy-MM-dd"); 
        if(jsonObj.get("status").getAsInt()==0){
          JsonArray  tradeDatas=jsonObj.get("hq").getAsJsonArray();
          for(int i=0;i<tradeDatas.size();i++){
            JsonArray   oneDayData=tradeDatas.get(i).getAsJsonArray();
            StockTradeInfo info=new StockTradeInfo();
            StockInfo stock=new StockInfo();
            stock.setSymbol(stockSymbol);
            stock.setCode(Integer.parseInt(stockCode));
            info.setStock(stock);
            info.setStockSymbol(stockSymbol);
            for(int j=0;j<oneDayData.size();j++){
              String data=oneDayData.get(j).getAsString();
              switch(j){
                case 0:info.setTradeDate(format2.parse(data));break;
                case 1:info.setOpenPrice(Float.parseFloat(data));break;
                case 2:info.setClosePrice(Float.parseFloat(data));break;
                case 3:info.setUpDownPrice(Float.parseFloat(data));break;
                case 4:info.setUpDownRate(Float.parseFloat(data.substring(0, data.length()-1)));break;
                case 5:info.setLowestPrice(Float.parseFloat(data));break;
                case 6:info.setHighestPrice(Float.parseFloat(data));break;
                case 7:info.setTradeAmount(Long.parseLong(data));break;
                case 8:Float money=(Float.parseFloat(data))*10000;info.setTradeMoney(money.longValue());break;
                case 9:if(!StringUtils.isBlank(data)&&!StringUtils.isBlank(data.substring(0, data.length()-1))){info.setTurnoverRate(Float.parseFloat(data.substring(0, data.length()-1)));}break;
              }
            }
            result.add(info);
          }
          
        }
      }else{
        throw new DataSyncException("http response statu code is not 200,the statu code is:"+status);
      }
    } catch (Exception e) {
      System.out.println("error parse ->" + url);
      throw new DataSyncException("dataSyncException ",e);
    } finally {
      try {
        if(response!=null)
          response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }
}
