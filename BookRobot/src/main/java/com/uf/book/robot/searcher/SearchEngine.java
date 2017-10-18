package com.uf.book.robot.searcher;

import java.io.File;
import java.util.List;

public class SearchEngine {
private static 	BookSearcher searcher=new RegExpressBookSearcher(new File("C:\\jason\\documents"));

public static List<File> search(String fileName) {
	return searcher.searchBooks(fileName);
}
}
