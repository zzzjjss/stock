package com.uf.springbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.uf.entity.Product;
import com.uf.entity.Word;
import com.uf.service.ProductManageService;

public class ProductEventBus {
	@Autowired
	private ProductManageService productManageService;
	@Autowired
	private ProductSearchTool searchTool;
	
	private EventBus eventBus=new EventBus("productAdd");
	public ProductEventBus(){
		eventBus.register(new  ProductAddListener());
	}
	
	public void publicAddProduct(Product product){
		eventBus.post(product);
	}
	
class ProductAddListener{
	@Subscribe
	public void listener(Product product){
		if(!Strings.isNullOrEmpty(product.getSearchKeywords())){
			String words[]=product.getSearchKeywords().split(" ");
			for(String word:words){
				Word w=new Word();
				w.setWord(word.trim());
				productManageService.saveWord(w);
				searchTool.addWordToTree(word.trim());
				System.out.println("listener work..");
			}
		}
		
	}
}
	
}
