package com.uf.store.restful.action.manager.dto;

import com.uf.store.restful.dto.RestfulResponse;

public class UploadImageResponse extends RestfulResponse{
	private String imagePath;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
