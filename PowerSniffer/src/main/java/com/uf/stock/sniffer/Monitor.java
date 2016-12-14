package com.uf.stock.sniffer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.StockInfo;
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
          Map<String,StockInfo> stockMap=new HashMap<String,StockInfo>();
          for(StockInfo stock:stocks){
            stockMap.put(stock.getSymbol(), stock);
          }
          if (stocks != null && stocks.size() > 0) {
            List<UpDownPower> powers = dataSyncService.calculateStocksCurrentPower(stocks);
            List<StockBuySellAlarmMsg> msgs = new ArrayList<StockBuySellAlarmMsg>();
            for (UpDownPower power : powers) {
              String symbol=power.getTradeInfo().getStockSymbol();
              if (power.isUpPower() && power.getUpdownPowerValue() >1.5&& power.getTradeInfo().getUpDownRate() > 1) {
                StockBuySellAlarmMsg msg = new StockBuySellAlarmMsg();
                msg.setStockName(power.getStockName());
                msg.setStockSymbol(symbol);
                msg.setMsgType(AlarmMsgType.BUY_POINT_MSG);
                msgs.add(msg);
              }
              StockInfo stock=stockMap.get(symbol);
              if(stock!=null&&stock.getAlarmSellPrice()!=null&&power.getTradeInfo().getClosePrice()>=stock.getAlarmSellPrice()){
                StockBuySellAlarmMsg msg = new StockBuySellAlarmMsg();
                msg.setStockName(power.getStockName());
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
