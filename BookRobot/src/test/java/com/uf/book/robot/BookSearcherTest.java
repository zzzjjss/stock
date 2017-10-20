package com.uf.book.robot;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uf.book.robot.searcher.BookSearcher;
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class BookSearcherTest {
	@Autowired
	private BookSearcher searcher;  
	@Test
	public void testBookSearcher() {
		List<File> files=searcher.searchBooks("chef");
		files.forEach(file->{
			System.out.println(file.getName());
		});
	}

}
