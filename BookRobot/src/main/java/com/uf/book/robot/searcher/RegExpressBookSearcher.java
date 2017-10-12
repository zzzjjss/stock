package com.uf.book.robot.searcher;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RegExpressBookSearcher implements BookSearcher {
	private File searchFolder[];

	public RegExpressBookSearcher(File ...searchFolder) {
		this.searchFolder = searchFolder;

	}

	@Override
	public List<File> searchBooks(String bookName) {
		List<File> searchResult = new LinkedList<>();
		if (searchFolder == null || searchFolder.length == 0) {
			return searchResult;
		}
		String regExp=buildRegExpresstion(bookName);
		for (File folder : searchFolder) {
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
		if (searchResult.size()>1) {
			String wordRegExp=buildWordRegExpresstion(bookName);
			Iterator<File> files=searchResult.iterator();
			while(files.hasNext()) {
				File file=files.next();
				if (!file.getName().matches(wordRegExp)) {
					files.remove();
					searchResult.remove(file);
				}
			}
		}
		return searchResult;
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
