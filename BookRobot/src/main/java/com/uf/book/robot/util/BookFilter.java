package com.uf.book.robot.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class BookFilter {
public static void main(String[] args) {
	File file=new File("C:\\jason\\chromeDownload");
	File books[]=file.listFiles();
	for(File book:books) {
		String bookName=FilenameUtils.getBaseName(book.getName());
		if (bookName.endsWith("(1)")||bookName.endsWith("(2)")||bookName.endsWith("(3)")||bookName.endsWith("(4)")||
				bookName.endsWith("(5)")||bookName.endsWith("(6)")||bookName.endsWith("(7)")||bookName.endsWith("(8)")||bookName.endsWith("(9)")||bookName.endsWith("(10)")||bookName.endsWith("(11)")) {
			book.delete();
			System.out.println(bookName);
		}
	}
}
}
