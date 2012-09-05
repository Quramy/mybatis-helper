package com.example.mybatis.helper.ant;

import org.apache.tools.ant.types.ZipFileSet;

public class Mapperset extends ZipFileSet{
	
	public final static String TYPE_RESOURCE = "resource";
	public final static String TYPE_URL ="url";
	
	private String suffix = "";
	private String type = TYPE_RESOURCE;
	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
