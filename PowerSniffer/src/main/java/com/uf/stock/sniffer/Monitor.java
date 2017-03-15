package com.uf.stock.sniffer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.sniffer.alarm.Alarm;
import com.uf.stock.sniffer.alarm.bean.AlarmMsgType;
import com.uf.stock.sniffer.alarm.bean.StockBuySellAlarmMsg;
import com.uf.stock.util.StockUtil;

public class Monitor extends Thread {
  private boolean isStarted = false;
  private List<Alarm> alarms = new ArrayList<Alarm>();
  @Autowired
  private DataSyncService dataSyncService;
  @Autowired
  private StockAnalysisService analyseService;


  public void startMonitor() {
    isStarted = true;
    this.start();
  }

  @Override
  public void run() {
    while (isStarted) {
      try {
        if (StockUtil.isOpenTime(new Date())) {
          List<StockInfo> stocks = dataSyncService.findStocksInMonitor();
          List<String> stockSymbols=new ArrayList<String>();
          Map<String,StockInfo> stockMap=new HashMap<String,StockInfo>();
          for(StockInfo stock:stocks){
            stockMap.put(stock.getSymbol(), stock);
            stockSymbols.add(stock.getSymbol());
          }
          if (stocks != null && stocks.size() > 0) {
            Map<String, StockTradeInfo> tradeInfoMap = dataSyncService.getCurrentStocksTradeInfo(stockSymbols);
            List<StockBuySellAlarmMsg> msgs = new ArrayList<StockBuySellAlarmMsg>();
            for (String stockSymbol : tradeInfoMap.keySet()) {
              StockTradeInfo  tradeInfo= tradeInfoMap.get(stockSymbol);
              String symbol=tradeInfo.getStockSymbol();
              StockInfo stock=stockMap.get(symbol);
              //System.out.println(stock.getName()+"-->"+tradeInfo.getClosePrice());
              if (stock.getAlarmBuCangPrice()!=null&&tradeInfo.getClosePrice()<=stock.getAlarmBuCangPrice()) {
                StockBuySellAlarmMsg msg = new StockBuySellAlarmMsg();
                msg.setStockName(stock.getName());
                msg.setStockSymbol(symbol);
                msg.setMsgType(AlarmMsgType.BUY_POINT_MSG);
                msgs.add(msg);
              }
              if(stock.getAlarmSellPrice()!=null&&tradeInfo.getClosePrice()>=stock.getAlarmSellPrice()){
                StockBuySellAlarmMsg msg = new StockBuySellAlarmMsg();
                msg.setStockName(stock.getName());
                msg.setStockSymbol(symbol);
                msg.setMsgType(AlarmMsgType.SELL_POINT_MSG);
                msgs.add(msg);
              }
            }
            for (Alarm alarm : alarms) {
              alarm.alarm(msgs);
            }
          }
          Thread.sleep(1000 * 5);
        }else{
          Thread.sleep(1000 * 120);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  public void stopMonitor() {
    isStarted = false;
  }

  public List<Alarm> getAlarms() {
    return alarms;
  }

  public void setAlarms(List<Alarm> alarms) {
    this.alarms = alarms;
  }


}
