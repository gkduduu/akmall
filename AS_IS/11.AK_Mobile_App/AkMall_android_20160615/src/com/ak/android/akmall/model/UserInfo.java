package com.ak.android.akmall.model;

public class UserInfo {

	private String userid;
	private String name;
	private String email;

	public UserInfo(String userid, String name, String email) {
		this.userid = userid;
		this.name = name;
		this.email = email;
	}

	public UserInfo() {
		this("", "", "");
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAuth() {
		return userid != null && userid.length() > 0;
	}

	public void invalidate() {
		userid = "";
		name = "";
		email = "";
	}
}
