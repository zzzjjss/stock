package com.uf.store.service.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.store.service.dao.ManagerDao;
import com.uf.store.service.entity.Manager;
@Component("managerDao")
public class ManagerDaoImpl extends CommonDaoImpl<Manager> implements ManagerDao{

}
