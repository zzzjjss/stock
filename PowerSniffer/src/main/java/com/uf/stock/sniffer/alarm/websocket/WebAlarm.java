package com.uf.stock.sniffer.alarm.websocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;

import com.uf.stock.sniffer.alarm.Alarm;

public class WebAlarm implements Alarm{
  public void alarm(List<String> infos) {
    Iterator<Session> sessions=WebSocketServer.connections.iterator();
    while(sessions.hasNext()){
      Session session=sessions.next();
      if(session.isOpen()){
        try {
          session.getBasicRemote().sendText(StringUtils.join(infos, "\r\n"));
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
