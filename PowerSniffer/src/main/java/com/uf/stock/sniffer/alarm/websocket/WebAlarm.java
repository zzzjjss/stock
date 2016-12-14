package com.uf.stock.sniffer.alarm.websocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.uf.stock.sniffer.alarm.Alarm;
import com.uf.stock.sniffer.alarm.bean.StockBuySellAlarmMsg;

public class WebAlarm implements Alarm{
  public void alarm(List<StockBuySellAlarmMsg> infos) {
    Iterator<Session> sessions=WebSocketServer.connections.iterator();
    while(sessions.hasNext()){
      Session session=sessions.next();
      if(session.isOpen()){
        try {
          if(infos.size()>0){
            Gson gson=new Gson();
            session.getBasicRemote().sendText(gson.toJson(infos));
          }
        } catch (IOException e) {
          e.printStackTrace();
          sessions.remove();
          try {
            session.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    }
  }
}
