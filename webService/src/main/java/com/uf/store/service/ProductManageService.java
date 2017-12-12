package com.uf.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.uf.store.dao.mysql.OrderItemRepository;
import com.uf.store.dao.mysql.ProductImageRepository;
import com.uf.store.dao.mysql.ProductRepository;
import com.uf.store.dao.mysql.po.OrderItem;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.dao.mysql.po.ProductImage;

@Transactional
@Service
public class ProductManageService{
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private ProductImageRepository productImageRepo;
	@Autowired
	private OrderItemRepository orderItemRepository;

	public void saveProduct(Product p,List<byte[]> imageData) throws Exception {
		productRepo.save(p);
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
			//TODO  search product in lucene  by keyword
			List<Integer> ids=new ArrayList<Integer>();
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

