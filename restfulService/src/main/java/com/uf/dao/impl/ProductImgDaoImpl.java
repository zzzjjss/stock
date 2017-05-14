package com.uf.dao.impl;

import org.springframework.stereotype.Component;

import com.uf.dao.ProductImgDao;
import com.uf.entity.ProductImage;
@Component("productImageDao")
public class ProductImgDaoImpl extends CommonDaoImpl<ProductImage> implements ProductImgDao{

}
