package com.uf.book.robot.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
@NoRepositoryBean
public interface BookInfoRepository extends JpaRepository<BookInfo, Long>{
	public BookInfo findTopByOrderByIdDesc();
}
