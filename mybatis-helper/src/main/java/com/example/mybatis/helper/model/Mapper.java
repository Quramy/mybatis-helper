package com.example.mybatis.helper.model;

import java.io.File;

public class Mapper {
	private String resource;
	private String url;
	private String clazz;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public Mapper() {

	}

	public Mapper(String resource) {
		this.resource = resource;
	}

	public Mapper(File file) {
		this.url = "file://" + file.getAbsolutePath();
	}

	public Mapper(Class<?> clazz) {
		this.clazz = clazz.getName();
	}
}
