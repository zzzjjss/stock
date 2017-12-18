package com.uf.store.dao.mysql;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.uf.store.dao.mysql.po.Word;

public interface WordRepository extends PagingAndSortingRepository<Word, Long> {
	public Word findTopByWord(String word);
}
