package com.uf.store.service.searcher;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.uf.store.dao.mysql.WordRepository;
import com.uf.store.dao.mysql.po.Word;
import com.uf.store.service.searcher.wordtree.WordTree;

@Component
public class WordTool {
	@Autowired
	private WordRepository  wordRepository;
	private WordTree wordTree;
	@PostConstruct
	public void init(){
		rebuid();
	}
	public void rebuid() {
		wordTree=new WordTree();
		int pageIndex=0,pageSize=500;
		PageRequest pageRequest=new PageRequest(pageIndex,pageSize);
		Page<Word> words=wordRepository.findAll(pageRequest);
		int totalPage=words.getTotalPages();
		addWordsToTree(words.getContent());
		for( pageIndex=pageIndex+1;pageIndex<totalPage;pageIndex++){
			pageRequest=new PageRequest(pageIndex,pageSize);
			words=wordRepository.findAll(pageRequest);
			addWordsToTree(words.getContent());
		}
	}
	public void addWordsToTree(List<Word> words){
		for(Word w:words){
			if(!Strings.isNullOrEmpty(w.getWord().trim())){
				wordTree.addWord(w.getWord().trim());
			}
		}
	}
	public void addWordToTree(String word){
		if(!Strings.isNullOrEmpty(word.trim())){
			wordTree.addWord(word.trim());
		}
	}

	public List<String> parseWords(String  input){
		return wordTree.parseWords(input);
	}
}
