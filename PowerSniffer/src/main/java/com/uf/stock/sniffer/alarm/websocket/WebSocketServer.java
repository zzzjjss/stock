package com.uf.stock.sniffer.alarm.websocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/alarm")
public class WebSocketServer{
  
  public static final Set<Session> connections =new CopyOnWriteArraySet<Session>();
  public WebSocketServer() {
    System.out.println("new");
  }


  @OnOpen
  public void start(Session session) {
      connections.add(session);
      System.out.println("new websocket session");
  }


  @OnClose
  public void end() {
      connections.remove(this);
      System.out.println("close websocket session");
  }


  @OnMessage
  public void incoming(String message) {
      System.out.println("websocket receive message:"+message);
  }

  @OnError
  public void onError(Throwable t) throws Throwable {
      System.out.println("websocket exception");
  }
}
