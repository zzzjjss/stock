package com.uf.store.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanFactory {
  private static ApplicationContext context;
  static{
      context=new ClassPathXmlApplicationContext("applicationContext.xml");
  }
  public static <T> T getBean(Class<T> bean){
    return context.getBean(bean);
  }
}
