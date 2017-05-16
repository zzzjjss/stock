package com.uf.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.dao.WordDao;
import com.uf.entity.Word;

@Component("wordDao")
public class WordDaoImpl extends CommonDaoImpl<Word> implements WordDao{

}
