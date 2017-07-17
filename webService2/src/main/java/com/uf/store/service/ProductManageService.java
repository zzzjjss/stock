package com.uf.store.service;

import java.util.List;

import com.uf.store.service.entity.Manager;
import com.uf.store.service.entity.Product;
import com.uf.store.service.entity.ProductImage;
import com.uf.store.service.entity.Word;
import com.uf.store.util.PageQueryResult;

public interface ProductManageService {
    public void addProduct(Product product,List<ProductImage>  imgs);
    public void updateProduct(Product product,List<ProductImage>  imgs);
    public PageQueryResult<Product> findProducsByKeyword(String keyword,int pageSize,int pageIndex);
    public List<ProductImage> findProductImages(Integer productId);
    public void deleteProductById(Integer productId);
    public Product findProductById(Integer productId);
    public Manager findManagerByName(String userName);
    public void saveWord(Word word);
    public PageQueryResult<Product> findPagedProducts(int pageSize,int pageIndex);
    public PageQueryResult<Word> findPagedWords(int pageSize,int pageIndex);
}
