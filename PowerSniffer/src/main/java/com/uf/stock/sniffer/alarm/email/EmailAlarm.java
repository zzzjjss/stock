package com.uf.stock.sniffer.alarm.email;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.uf.stock.sniffer.alarm.Alarm;
import com.uf.stock.sniffer.alarm.bean.StockBuySellAlarmMsg;

public class EmailAlarm implements Alarm{
  
  public void alarm(List<StockBuySellAlarmMsg> infos) {
    //TODO if  infos  is as same as  pre-infos ,don't send email
    String content=StringUtils.join(infos, "\r\n");
    MailSender sender=new MailSender();
    sender.sendTextMail(content);
  }

}
