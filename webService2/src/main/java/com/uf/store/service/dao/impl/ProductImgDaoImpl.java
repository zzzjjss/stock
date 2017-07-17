package com.uf.store.service.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.store.service.dao.ProductImgDao;
import com.uf.store.service.entity.ProductImage;
@Component("productImageDao")
public class ProductImgDaoImpl extends CommonDaoImpl<ProductImage> implements ProductImgDao{

}
