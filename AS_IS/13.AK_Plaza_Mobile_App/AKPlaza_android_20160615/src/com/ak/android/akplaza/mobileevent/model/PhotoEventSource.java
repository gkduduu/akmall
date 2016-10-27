package com.ak.android.akplaza.mobileevent.model;

import android.net.Uri;

public class PhotoEventSource {

	private String title;
	private String content;
	private String contentImage;
	private String userid;
	private String username;
	private String regDate;
	private Uri contentImageUri;

	private String eventIndex;
	private String eventToken;
	private String entryIndexno;
	private String name = "";
	private String phone = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null) {
			name = "";
		}
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if(phone == null) {
			phone = "";
		}
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentImage() {
		return contentImage;
	}

	public void setContentImage(String contentImage) {
		this.contentImage = contentImage;
	}

	public String getEventIndex() {
		return eventIndex;
	}

	public void setEventIndex(String eventIndex) {
		this.eventIndex = eventIndex;
	}

	public String getEventToken() {
		return eventToken;
	}

	public void setEventToken(String eventToken) {
		this.eventToken = eventToken;
	}

	public String getEntryIndexno() {
		return entryIndexno;
	}

	public void setEntryIndexno(String entryIndexno) {
		this.entryIndexno = entryIndexno;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public Uri getContentImageUri() {
		return contentImageUri;
	}

	public void setContentImageUri(Uri contentImageUri) {
		this.contentImageUri = contentImageUri;
	}
}
