package com.uf.book.robot.controller;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.uf.book.robot.controller.bean.DownloadUrlInfo;
import com.uf.book.robot.dao.BookInfo;
import com.uf.book.robot.dao.BookInfoRepository;

@RestController
@RequestMapping("book")
public class BookDownloadController {
	@Autowired
	private BookInfoRepository bookInfoRepo;
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadBook(String url) throws Exception {
		File file = new File("c:/jason/temp/worker-6700.log");
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName().replaceAll(" ", "_"));
		header.setContentLength(file.length());
		return ResponseEntity.ok().headers(header).body(resource);
	}

	@RequestMapping(value = "generateDownloadUrl", method = RequestMethod.GET)
	public DownloadUrlInfo generateDownloadUrl(String bookName) {
		System.out.println(bookName);
		BookInfo bookInfo=new BookInfo();
		bookInfo.setAuthor("jasonzhagn");
		bookInfo.setBookName("hello word");
		bookInfoRepo.save(bookInfo);
		DownloadUrlInfo info = new DownloadUrlInfo();
		info.setUrl("http://1111");
		info.setAvalibleMinute(4);
		return info;
	}
}
