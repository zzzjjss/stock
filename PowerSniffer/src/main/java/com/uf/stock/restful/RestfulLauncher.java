package com.uf.stock.restful;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.uf.stock.sniffer.Monitor;
import com.uf.stock.util.SpringBeanFactory;

@ApplicationPath("rest")
public class RestfulLauncher extends ResourceConfig{
  public RestfulLauncher() {
    packages("com.uf.stock.restful.action");
    Monitor monitor=SpringBeanFactory.getBean(Monitor.class);
    monitor.startMonitor();
}
}
