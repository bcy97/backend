package com.backend.vo;

public class UserInfo {
	public UserInfo() {

	}

	public UserInfo(String name, String pwd, String depaName, String id,String depaType) {
		this.name = name;
		this.pwd = pwd;
		this.depaName = depaName;
		this.id = id;
		this.depaType = depaType;
	}

	private String name;
	private String pwd;
	private String depaName;
	private String depaType;
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDepaName() {
		return depaName;
	}

	public void setDepaName(String depaName) {
		this.depaName = depaName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepaType() {
		return depaType;
	}

	public void setDepaType(String depaType) {
		this.depaType = depaType;
	}

}
