package com.uf.store.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uf.store.Application;
import com.uf.store.dao.mysql.po.Manager;
import com.uf.store.service.cache.CacheService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class CacheServiceTest {
	@Autowired
	private CacheService cache;  
	@Test
	public void testCache() {
		for(int i=0;i<100;i++) {
			Manager manager=new Manager();
			manager.setUserName("hello"+i);
			cache.putObject(String.valueOf(i),manager );
		}
		for(int i=0;i<100;i++) {
			Object manager=cache.getCachedObject(String.valueOf(i));
			assert(manager!=null&&manager instanceof Manager&&((Manager)manager).getUserName().equals("hello"+i));
		}
	}

}
