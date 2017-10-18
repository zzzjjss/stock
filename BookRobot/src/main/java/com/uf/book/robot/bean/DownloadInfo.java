package com.uf.book.robot.bean;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class DownloadInfo {
@PrimaryKey	
private String md5Str;
private String filePath;
private long generateTime;

public String getMd5Str() {
	return md5Str;
}
public void setMd5Str(String md5Str) {
	this.md5Str = md5Str;
}
public String getFilePath() {
	return filePath;
}
public void setFilePath(String filePath) {
	this.filePath = filePath;
}
public long getGenerateTime() {
	return generateTime;
}
public void setGenerateTime(long generateTime) {
	this.generateTime = generateTime;
}



}
