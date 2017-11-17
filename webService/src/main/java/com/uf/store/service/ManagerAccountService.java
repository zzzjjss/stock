package com.uf.store.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uf.store.dao.mysql.ManagerRepository;
import com.uf.store.dao.mysql.po.Manager;
import com.uf.store.service.cache.CacheService;

@Transactional
@Service
public class ManagerAccountService {
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private CacheService cacheService;
	public String loginGenerateToken(String userName,String password) {
		Manager manager=managerRepository.findTopByUserName(userName);
		if (manager==null||!manager.getPassword().equals(password)) {
			return null;
		}
		String token=UUID.randomUUID().toString();
		cacheService.putObject(token,manager);
		return token;
	}
}
