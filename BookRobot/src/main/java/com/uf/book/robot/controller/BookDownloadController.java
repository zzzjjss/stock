package com.uf.book.robot.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uf.book.robot.controller.bean.DownloadPathInfo;
import com.uf.book.robot.dao.mysql.BookInfoRepository;
import com.uf.book.robot.searcher.SearchEngine;
import com.uf.book.robot.util.DownloadUtil;

@RestController
@RequestMapping("book")
public class BookDownloadController {
	@Autowired
	private BookInfoRepository bookInfoRepo;
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadBook(String downloadPath, HttpServletResponse response) throws Exception {
		String localFilePath=DownloadUtil.findLocalFilePath(downloadPath);
		if (localFilePath==null) {
			return null;
		}
		File file = new File(localFilePath);
		if (!file.exists()) {
			return null;
		}
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders header = new HttpHeaders();
		String fileName=file.getName().replaceAll(" ", "_");
		System.out.println(fileName);
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(fileName,"utf-8"));
		header.setContentLength(file.length());
		return new ResponseEntity<InputStreamResource>(resource, header, HttpStatus.OK);
	}

	@RequestMapping(value = "searchBooks", method = RequestMethod.GET)
	public List<DownloadPathInfo> generateDownloadUrl(String searchWord) {
		System.out.println("searchWord:"+searchWord);
		List<DownloadPathInfo> result=new ArrayList<DownloadPathInfo>();	
		List<File> files=SearchEngine.search(searchWord);
		if (files!=null) {
			files.forEach(file->{
				DownloadPathInfo info = new DownloadPathInfo();
				String downloadPath=DownloadUtil.generateDownloadPath(file.getAbsolutePath());
				info.setDownloadPath(downloadPath);
				info.setFileName(file.getName());
				result.add(info);
			});
		}
		return result;
	}
}
