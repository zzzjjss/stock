package com.uf.book.robot;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.uf.book.robot.searcher.BookSearcher;
import com.uf.book.robot.searcher.RegExpressBookSearcher;

public class BookSearcherTest {

	@Test
	public void test() {
		BookSearcher searcher=new RegExpressBookSearcher(new File("C:\\jason\\chromeDownload"));
		List<File> files=searcher.searchBooks("隐身人    ");
		files.forEach(file->{
			System.out.println(file.getName());
		});
	}

}
