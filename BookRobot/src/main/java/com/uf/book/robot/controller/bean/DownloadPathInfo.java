package com.uf.book.robot.controller.bean;

public class DownloadPathInfo {
	private String fileName;
	private String downloadPath;
	private long remainTime;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	public long getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}
}
