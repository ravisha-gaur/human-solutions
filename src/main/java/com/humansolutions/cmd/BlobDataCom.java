package com.humansolutions.cmd;

import java.sql.Blob;

public class BlobDataCom {

	private int imageId;
	
	private Blob image;
	
	private int sessionId;
	
	private String username;
	
	private int imageSaved;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getImageSaved() {
		return imageSaved;
	}

	public void setImageSaved(int imageSaved) {
		this.imageSaved = imageSaved;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}
	
}
