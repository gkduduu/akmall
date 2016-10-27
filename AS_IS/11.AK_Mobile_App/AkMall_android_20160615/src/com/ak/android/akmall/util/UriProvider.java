package com.ak.android.akmall.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.Const;

public class UriProvider {

	public static final String SCHEME_AKMALL = "akmall";

	public static final int URI_NO_MATCH = -1;
	public static final int URI_MAIN = 0;
	public static final int URI_SEARCH = 1;
	public static final int URI_HISTORY = 2;
	public static final int URI_SHORTCUT = 3;
	public static final int URI_EVENT = 4;
	public static final int URI_MYAK = 5;
	public static final int URI_LOGIN = 6;
	
	private static UriProvider mInstance;
	private List<Identity> mIdentityList;
	private Map<Integer, Uri> mUriMap;

	private UriProvider() {

	}

	public synchronized static UriProvider getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new UriProvider();
			mInstance.init(context);
		}
		return mInstance;
	}

	private void init(Context context) {
		buildUriMap(context);
		buildIdentityList();
	}

	private void buildIdentityList() {
		Uri uri;
		mIdentityList = new ArrayList<Identity>();
		for (int uriCode : mUriMap.keySet()) {
			uri = mUriMap.get(uriCode);
			mIdentityList.add(new Identity(uri.getPath(), uri.getQueryParameter("act"), uriCode));
		}
	}

	@SuppressLint("UseSparseArrays")
    private void buildUriMap(Context context) {
		String baseUrl = Const.URL_BASE;
		String appParam = "isAkApp=Y";

		mUriMap = new HashMap<Integer, Uri>();
		mUriMap.put(URI_MAIN,
				Uri.parse(baseUrl + context.getString(R.string.uri_home) + "?" + appParam));
		
		mUriMap.put(URI_SEARCH,
				Uri.parse(baseUrl + context.getString(R.string.uri_category) + "?" + appParam));
		
		mUriMap.put(URI_HISTORY,
				Uri.parse(baseUrl + context.getString(R.string.uri_history) + "?" + appParam));
		
		mUriMap.put(URI_SHORTCUT,
				Uri.parse(baseUrl + context.getString(R.string.uri_shortcut) + "?" + appParam));
		mUriMap.put(URI_EVENT,
				Uri.parse(baseUrl + context.getString(R.string.uri_event) + "?" + appParam));
		//기존 실서버테스트용
		//mUriMap.put(URI_MYAK,
		//		Uri.parse(baseUrl + context.getString(R.string.uri_myak) + "&" + appParam));
		
		mUriMap.put(URI_MYAK,
				Uri.parse(baseUrl + context.getString(R.string.uri_myak) + "?" + appParam));
		
		mUriMap.put(URI_LOGIN,
				Uri.parse(baseUrl + context.getString(R.string.uri_login) + "?" + appParam));
		
		
	}

	public Uri getUri(int uriCode) {
		return mUriMap.get(uriCode);
	}

	public int getUriCode(Uri uri) {
		for (Identity id : mIdentityList) {
			if (id.equals(uri)) {
				return id.code;
			}
		}
		return URI_NO_MATCH;
	}

	class Identity {

		public final String path;
		public final String act;
		public final int code;

		Identity(String path, String act, int code) {
			this.path = path != null ? path : "";
			this.act = act != null ? act : "";
			this.code = code;
		}

		public boolean equals(Uri uri) {
			String act = uri.getQueryParameter("act");
			return this.path.equals(uri.getPath()) && this.act.equals(act != null ? act : "");
		}
	}
}
