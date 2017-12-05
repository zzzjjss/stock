package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uf.store.dao.mysql.po.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
	public Manager findTopByUserName(String userName);
}
