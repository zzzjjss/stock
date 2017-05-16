package com.uf.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.dao.ManagerDao;
import com.uf.entity.Manager;
@Component("managerDao")
public class ManagerDaoImpl extends CommonDaoImpl<Manager> implements ManagerDao{

}
