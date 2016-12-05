package com.uf.stock.sniffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.uf.stock.data.bean.StockTradeInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.sniffer.alarm.Alarm;

public class Monitor extends Thread {
  private boolean isStarted = false;
  private List<Alarm> alarms=new ArrayList<Alarm>();
  @Autowired
  private DataSyncService dataSyncService;
  
  
  
  public void startMonitor() {
    isStarted = true;
    this.start();
  }

  @Override
  public void run() {
    while (isStarted) {
      try {
        Map<String, StockTradeInfo> current=dataSyncService.getCurrentStocksTradeInfo(Arrays.asList("sz000004"));
        Iterator<StockTradeInfo> currentInfos=current.values().iterator();
        for(Alarm alarm:alarms){
          alarm.alarm(Arrays.asList(String.valueOf(current.size())));
        }
        Thread.sleep(1000 * 3);
      } catch (InterruptedException e) {
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
