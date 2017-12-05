package com.uf.store.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uf.store.dao.mysql.po.ShopCarItem;

public interface ShopCarItemRepository extends JpaRepository<ShopCarItem,Long> {

}
