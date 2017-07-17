package com.uf.store.webservice;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("webservice")
public class WebServicelLauncher extends ResourceConfig{
  public WebServicelLauncher() {
    packages("com.uf.store.webservice.action");
    register(MultiPartFeature.class);
}
}
