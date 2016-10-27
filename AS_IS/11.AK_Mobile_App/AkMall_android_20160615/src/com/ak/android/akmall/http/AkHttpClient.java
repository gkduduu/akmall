/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ak.android.akmall.http;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.Const;

public class AkHttpClient {

	public static final String TAG = "AkHttpClient";
	public static final boolean DBG = false;

	private static final int MAX_RETRY = 3;
	private static final int SOCKET_OPERATION_TIMEOUT = 60 * 1000;

	private static DefaultHttpClient mClient;

	private Context mContext;

	static {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params,
				SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				params, schemeRegistry);

		mClient = new DefaultHttpClient(manager, params);
		mClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
				MAX_RETRY, true));
	}

	public AkHttpClient(Context context) {
		mContext = context;
	}

	public String execute(HttpRequestBase request) throws IOException {

		syncCookieManagerToCookieStore();
		request.addHeader(AkMallFacade.HTTP_HEADER_APP_DEVICE, "Android");
		request.addHeader(AkMallFacade.HTTP_HEADER_APP_VERSION, AkMallFacade.getVersionName(mContext));

		HttpResponse response = mClient.execute(request);

		if(response.getStatusLine().getStatusCode() != 200) {
			Log.e(TAG, "failed post request  http status code is not 200");
			throw new IOException("서버로부터 에러 응답을 받았습니다. code: " + response.getStatusLine().getStatusCode());
		}

		syncCookieStoreToCookieManager();

		return EntityUtils.toString(response.getEntity(), "UTF-8");
	}

	private void syncCookieManagerToCookieStore() {

		String domain = Const.SERVER_ADDR;
		CookieManager cookieManager = CookieManager.getInstance();
		Cookies cookies = new Cookies(cookieManager.getCookie(domain));

		CookieStore store = mClient.getCookieStore();
		for(Cookie cookie : cookies.toList(domain)) {
			if(DBG) Log.d(TAG, "Cookie Manager to Store: " + cookie.getName() + "=" + cookie.getValue() + "; domain=" + domain);
			store.addCookie(cookie);
		}
	}

	private void syncCookieStoreToCookieManager() {

		List<Cookie> cookies = mClient.getCookieStore().getCookies();
		String cookieString;
		CookieManager cookieManager = CookieManager.getInstance();
		for(Cookie cookie : cookies) {
			cookieString = cookie.getName() + "=" + cookie.getValue();
			cookieManager.setCookie(cookie.getDomain(), cookieString);
			if(DBG) Log.d(TAG, "Cookie Store to Manager: " + cookieString);
		}

		CookieSyncManager.getInstance().sync();
	}
}
