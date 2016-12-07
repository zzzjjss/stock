package com.uf.stock.sniffer.alarm;

import java.util.List;

import com.uf.stock.sniffer.alarm.bean.StockUpDownPowerMsg;

public interface Alarm {
  public void alarm(List<StockUpDownPowerMsg> infos);
}
