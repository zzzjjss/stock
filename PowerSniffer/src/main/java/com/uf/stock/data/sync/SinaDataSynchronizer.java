package com.uf.stock.data.sync;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
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

public class SinaDataSynchronizer {
  private CloseableHttpClient client ;
  private  ConfigInfo configInfo; 
  public SinaDataSynchronizer(ConfigInfo configInfo){
    this.configInfo=configInfo;
    client=HttpUnit.createHttpClient(configInfo);
  }
  public List<StockInfo> syncAllStocksInfo() {
    List<StockInfo> all = new LinkedList<StockInfo>();
    int pageIndex = 1, totalPage = Integer.MAX_VALUE;
    String url = "http://screener.finance.sina.com.cn/znxg/data/json.php/SSCore.doView";
    while (pageIndex <= totalPage) {
      try {
        HttpPost postMethod = new HttpPost(url);
        List<NameValuePair> formData = new ArrayList<NameValuePair>();
        formData.add(new BasicNameValuePair("page", String.valueOf(pageIndex)));
        formData.add(new BasicNameValuePair("num", "60"));
        formData.add(new BasicNameValuePair("sort", ""));
        formData.add(new BasicNameValuePair("asc", "0"));
        formData.add(new BasicNameValuePair("field0", "stocktype"));
        formData.add(new BasicNameValuePair("field1", "sinahy"));
        formData.add(new BasicNameValuePair("field2", "diyu"));
        formData.add(new BasicNameValuePair("value0", "*"));
        formData.add(new BasicNameValuePair("value1", "*"));
        formData.add(new BasicNameValuePair("value2", "*"));
        formData.add(new BasicNameValuePair("field3", "dtsyl"));
        formData.add(new BasicNameValuePair("max3", "82902.98"));
        formData.add(new BasicNameValuePair("min3", "0"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formData);
        postMethod.setEntity(formEntity);
        CloseableHttpResponse response = client.execute(postMethod);
        String responseContent = EntityUtils.toString(response.getEntity());
        if (responseContent != null && responseContent.length() > 1) {
          String jsonStr = responseContent.substring(1, responseContent.length() - 1);
          System.out.println(jsonStr);
          JsonParser parser = new JsonParser();
          JsonElement root = parser.parse(jsonStr);
          JsonArray items = root.getAsJsonObject().get("items").getAsJsonArray();
          Iterator<JsonElement> iterator = items.iterator();
          while (iterator.hasNext()) {
            JsonObject stockData = iterator.next().getAsJsonObject();
            StockInfo stockInfo = new StockInfo();
            stockInfo.setName(stockData.get("name").getAsString());
            stockInfo.setSymbol(stockData.get("symbol").getAsString());
            try {
              stockInfo.setCode(Integer.parseInt(stockData.get("symbol").getAsString().substring(2)));
            } catch (NumberFormatException exception) {
              exception.printStackTrace();
            }
            //stockInfo.setTotalAAmount((stockData.get("ltag").getAsFloat()) * 100);
            stockInfo.setPeRatio(stockData.get("dtsyl").getAsFloat());
            all.add(stockInfo);
          }
          totalPage = root.getAsJsonObject().get("page_total").getAsInt();
          pageIndex++;
          Thread.sleep(1000);
        } else {
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return all;
  }


  public Map<String, StockTradeInfo> syncStocksCurrentTradeInfo(List<String> stockSymbol) {
    Map<String, StockTradeInfo> result = new HashMap<String, StockTradeInfo>();
    if(stockSymbol==null||stockSymbol.size()==0)
    	return result;
    List<String> urlParam = new ArrayList<String>();
    for (String symbol : stockSymbol) {
      urlParam.add("s_" + symbol);
    }
    long time = System.currentTimeMillis();
    String url = "http://hq.sinajs.cn/?rn=" + time + "&list=" + Joiner.on(",").join(urlParam);
    HttpGet getMethod = new HttpGet(url);
    CloseableHttpResponse responese = null;
    try {
      responese = client.execute(getMethod);
      int status = responese.getStatusLine().getStatusCode();
      if (status == HttpStatus.SC_OK) {
        HttpEntity entity = responese.getEntity();
        String response = EntityUtils.toString(entity, Charset.forName("gb2312"));
        // System.out.println(response);
        List<String> stocksPrice = Splitter.on(";").splitToList(response);
        if (stocksPrice != null) {
          for (String stockInfo : stocksPrice) {
            if (!Strings.isNullOrEmpty(stockInfo) && stockInfo.contains("=")) {
              String keyValue[] = stockInfo.split("=");
              String symbol = keyValue[0].substring(keyValue[0].length() - 8);
              StockTradeInfo currentInfo = new StockTradeInfo();
              currentInfo.setStockSymbol(symbol);
              String infos[] = keyValue[1].replace("\"", "").split(",");
              if (infos != null && infos.length > 5) {
                if (infos[1] != null) {
                  currentInfo.setClosePrice(Float.parseFloat(infos[1]));
                }
                if (infos[2] != null) {
                  currentInfo.setUpDownPrice(Float.parseFloat(infos[2]));
                }
                if (infos[3] != null) {
                  currentInfo.setUpDownRate(Float.parseFloat(infos[3]));
                }

                if (infos[4] != null) {
                  currentInfo.setTradeAmount(Long.parseLong(infos[4]));
                }
                if (infos[5] != null) {
                  currentInfo.setTradeMoney(Long.parseLong(infos[5]));
                }
              }
              result.put(symbol, currentInfo);
            }
          }
        }
      } else {
        System.out.println("http response  status code is:" + status + " -->" + url);
      }

    } catch (Exception e) {
      System.out.println("error parse ->" + url);
      e.printStackTrace();
    } finally {
      try {
    	  if(responese!=null){
    		  responese.close();
    	  }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }
}
