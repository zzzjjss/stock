package com.uf.springbean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.uf.entity.Word;
import com.uf.service.ProductManageService;
import com.uf.util.PageQueryResult;
import com.uf.util.wordtree.WordTree;
@Component("searchTool")
public class ProductSearchTool {
	@Autowired
	private ProductManageService productManageService;
	private WordTree wordTree=new WordTree();
	@PostConstruct
	public void init(){
		int pageIndex=1;
		PageQueryResult<Word> pageResult=productManageService.findPagedWords(500, pageIndex);
		int totalPage=pageResult.getTotalPage();
		addWordsToTree(pageResult.getPageData());
		for( pageIndex=pageIndex+1;pageIndex<=totalPage;pageIndex++){
			pageResult=productManageService.findPagedWords(500, pageIndex);
			addWordsToTree(pageResult.getPageData());
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
