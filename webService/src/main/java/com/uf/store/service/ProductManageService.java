package com.uf.store.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.uf.store.dao.mysql.OrderItemRepository;
import com.uf.store.dao.mysql.ProductImageRepository;
import com.uf.store.dao.mysql.ProductRepository;
import com.uf.store.dao.mysql.WordRepository;
import com.uf.store.dao.mysql.po.OrderItem;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ProductImage;
import com.uf.store.dao.mysql.po.Word;
import com.uf.store.service.searcher.SearchEngine;
import com.uf.store.service.searcher.WordTool;

@Transactional
@Service
public class ProductManageService{
	private Logger logger=LoggerFactory.getLogger(ProductManageService.class);
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private ProductImageRepository productImageRepo;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private WordRepository wordRepository;
	@Autowired
	private WordTool wordTool;
	@Autowired
	private SearchEngine searchEngine;	
	public void saveProduct(Product p,List<byte[]> imageData) throws Exception {
		if(!Strings.isNullOrEmpty(p.getSearchKeywords())){
	    	  List<String> words=Splitter.on(' ').trimResults().omitEmptyStrings().splitToList(p.getSearchKeywords());
				for(String word:words){
					if(wordRepository.findTopByWord(word)==null) {
						Word w=new Word();
						w.setWord(word.trim());
						wordRepository.save(w);
						wordTool.addWordToTree(word.trim());
					}
				}
		}
		boolean isInsert=false;
		if(p.getId()==null) {
			isInsert=true;
		}
		productRepo.save(p);
		if(isInsert) {
			searchEngine.addProductInfoToIndex(p);
		}else {
			searchEngine.updateProductIndex(p);
		}
		productImageRepo.deleteByProductId(p.getId());
		imageData.forEach(data->{
			ProductImage image=new ProductImage();
			image.setImageContent(data);;
			image.setProduct(p);
			productImageRepo.save(image);
		});
	}
	public void deleteProduct(Long id) {
		productImageRepo.deleteByProductId(id);
		productRepo.delete(id);
	}
	public Page<Product> getPagedProducts(int pageIndex,int pageSize,String keyword){
		PageRequest pageRequest=new PageRequest(pageIndex,pageSize);
		if (Strings.isNullOrEmpty(keyword)) {
			return productRepo.findAll(pageRequest);
		}else {
			List<String> words = wordTool.parseWords(keyword);
			if (words == null || words.size() <= 0)
				return null;
			keyword = Joiner.on(" ").join(words);
			logger.info("after parse the keyword is ->" + keyword);
			List<Long> ids = searchEngine.searchProductIds(keyword);
			return productRepo.findByIdIn(ids, pageRequest);
		}
	}
	public Product getProductById(long productId) {
		return productRepo.findOne(productId);
	}
	public List<OrderItem> listProductOrderItems(long productId,int pageIndex,int pageSize){
		PageRequest pageRequest=new PageRequest(pageIndex,pageSize);
		return orderItemRepository.findPagedByProductId(productId, pageRequest);
	}
}

