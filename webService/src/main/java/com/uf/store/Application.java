package com.uf.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(value = {"com.uf.store"})
public class Application {
	 public static void main(String[] args) throws Exception {
	        SpringApplication.run(Application.class, args);
	    }
}