package com.uf.store.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.uf.store.service.ProductManageService;
import com.uf.store.service.entity.Product;

public class Tool {
	public static void main(String[] args) {
		ApplicationContext  context=new ClassPathXmlApplicationContext("springMVC-servlet.xml");
		Set<String> word=new HashSet<String>();
		ProductManageService service=context.getBean(ProductManageService.class);
		for(int i=10;i<=36;i++){
			Product p=service.findProductById(i);
			if(p!=null){
				String keys=p.getSearchKeywords();
				String keyArray[]=keys.split(" ");
				for(String key:keyArray){
					word.add(key.trim());
				}
			}
		}
//		word.forEach(item->{
//			Word w=new Word();
//			w.setWord(item);
//			service.saveWord(w);
//		});
	}
}
