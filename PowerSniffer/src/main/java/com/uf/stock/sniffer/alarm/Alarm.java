package com.uf.stock.sniffer.alarm;

import java.util.List;

import com.uf.stock.sniffer.alarm.bean.StockBuySellAlarmMsg;

public interface Alarm {
  public void alarm(List<StockBuySellAlarmMsg> infos);
}
