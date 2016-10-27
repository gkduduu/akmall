package com.ak.android.akmall.model;

public class PopularSearchKeyword implements SearchKeyword {

	public final int id;
	public final String query;
	public final int rank;
	public final int hit;
	public final String disp_class_cd;

	public PopularSearchKeyword(int id, String query, int rank, int hit, String disp_class_cd) {
		this.id = id;
		this.query = query;
		this.rank = rank;
		this.hit = hit;
		this.disp_class_cd = disp_class_cd;
	}

	@Override
	public String getKeyword() {
		return query;
	}
}
