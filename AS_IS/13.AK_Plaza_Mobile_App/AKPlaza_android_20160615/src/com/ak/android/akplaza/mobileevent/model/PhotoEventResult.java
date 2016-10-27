package com.ak.android.akplaza.mobileevent.model;

public class PhotoEventResult {

	private String result;
	private String message;
	private PhotoEventSource entry;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PhotoEventSource getEntry() {
		return entry;
	}

	public void setEntry(PhotoEventSource entry) {
		this.entry = entry;
	}
}
