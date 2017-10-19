package com.uf.book.robot.searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class RegExpressBookSearcher implements BookSearcher {
	private String searchFolder[];

	public RegExpressBookSearcher(@Value("${searcher.folder}") String ...searchFolder) {
		this.searchFolder = searchFolder;
	}

	@Override
	public List<File> searchBooks(String bookName) {
		List<File> searchResult = new LinkedList<>();
		if (searchFolder == null || searchFolder.length == 0) {
			return searchResult;
		}
		String regExp=buildRegExpresstion(bookName);
		for (String folderStr : searchFolder) {
			File folder=new File(folderStr);
			if (folder.isFile()) {
				continue;
			}
			File files[] = folder.listFiles();
			if (files == null || files.length == 0) {
				continue;
			}
			for (File file : files) {
				if (file.isDirectory()) {
					continue;
				}
				String name = file.getName();
				if(name.matches(regExp)) {
					searchResult.add(file);
				}
			}
		}
		List<File> wordSearchRe=new ArrayList<File>();
		if (searchResult.size()>1) {
			String wordRegExp=buildWordRegExpresstion(bookName);
			searchResult.forEach(file->{
				if (file.getName().matches(wordRegExp)) {
					wordSearchRe.add(file);
				}
			});
		}
		return wordSearchRe.size()>0?wordSearchRe:searchResult;
	}

	private String buildRegExpresstion(String bookName) {
		StringBuilder result = new StringBuilder();
		if (bookName == null || bookName.trim().isEmpty()) {
			return null;
		}
		char[] chars = bookName.toCharArray();
		result.append(".*");
		for (char c : chars) {
			if (c==' ') {
				continue;
			}
			result.append(c + ".*");
		}
		return result.toString();
	}
	private String buildWordRegExpresstion(String bookName) {
		StringBuilder result = new StringBuilder();
		if (bookName == null || bookName.trim().isEmpty()) {
			return null;
		}
		String words[]=bookName.split(" ");
		result.append(".*");
		for (String word: words) {
			result.append(word + ".*");
		}
		return result.toString();
	}
}
