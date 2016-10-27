package com.ak.android.akmall.model;

public class SuggestSearchKeyword implements SearchKeyword {

	public String label;
	public String value;

	public SuggestSearchKeyword(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String getKeyword() {
		return label;
	}
}
