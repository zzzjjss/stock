package com.uf.store.service.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.store.service.dao.WordDao;
import com.uf.store.service.entity.Word;

@Component("wordDao")
public class WordDaoImpl extends CommonDaoImpl<Word> implements WordDao{

}
