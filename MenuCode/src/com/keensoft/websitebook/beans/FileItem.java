package com.keensoft.websitebook.beans;

public class FileItem {
	private String name;
	private String path;
	private boolean downLoad;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isDownLoad() {
		return downLoad;
	}

	public void setDownLoad(boolean downLoad) {
		this.downLoad = downLoad;
	}

}
