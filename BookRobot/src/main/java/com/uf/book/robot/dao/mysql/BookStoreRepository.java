package com.uf.book.robot.dao.mysql;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
@NoRepositoryBean
public interface BookStoreRepository extends CrudRepository<BookStoreInfo, Long> {

}
