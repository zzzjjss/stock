package com.uf.store.dao.mysql;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.uf.store.dao.mysql.po.Product;

public interface ProductRepository  extends PagingAndSortingRepository<Product, Long> {
	public void deleteById(Integer id);
	@Query("select p from Product p  where p.id in :ids ")
	public Page<Product> findByIdIn(@Param("ids")List<Integer> ids,Pageable pageable);
}
