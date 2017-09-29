package com.uf.book.robot.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long>{
	public BookInfo findTopByOrderByIdDesc();
}
