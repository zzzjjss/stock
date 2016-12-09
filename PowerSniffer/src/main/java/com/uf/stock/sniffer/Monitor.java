package com.uf.stock.sniffer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.uf.stock.bean.UpDownPower;
import com.uf.stock.data.bean.StockInfo;
import com.uf.stock.service.DataSyncService;
import com.uf.stock.service.StockAnalysisService;
import com.uf.stock.sniffer.alarm.Alarm;
import com.uf.stock.sniffer.alarm.bean.StockUpDownPowerMsg;
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
          System.out.println("monitor stocks size:" + stocks.size());
          if (stocks != null && stocks.size() > 0) {
            List<UpDownPower> powers = dataSyncService.calculateStocksCurrentPower(stocks);
            List<StockUpDownPowerMsg> msgs = new ArrayList<StockUpDownPowerMsg>();
            for (UpDownPower power : powers) {
              if (power.isUpPower() && power.getUpdownPowerValue() > 1 && power.getTradeInfo().getUpDownRate() > 1) {
                StockUpDownPowerMsg msg = new StockUpDownPowerMsg();
                msg.setIsUpPower(true);
                msg.setPower(power.getUpdownPowerValue());
                msg.setStockName(power.getStockName());
                msg.setStockSymbol(power.getTradeInfo().getStockSymbol());
                msgs.add(msg);
              }
              if (!power.isUpPower() && power.getUpdownPowerValue() > 1 && power.getTradeInfo().getUpDownRate() < -2) {
                StockUpDownPowerMsg msg = new StockUpDownPowerMsg();
                msg.setIsUpPower(false);
                msg.setPower(power.getUpdownPowerValue());
                msg.setStockName(power.getStockName());
                msg.setStockSymbol(power.getTradeInfo().getStockSymbol());
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
