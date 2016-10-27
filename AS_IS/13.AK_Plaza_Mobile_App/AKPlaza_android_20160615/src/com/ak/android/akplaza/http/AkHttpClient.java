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
package com.ak.android.akplaza.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
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

import com.ak.android.akplaza.BuildConfig;
import com.ak.android.akplaza.common.AkPlazaFacade;

public class AkHttpClient {

	public static final String TAG = "AkHttpClient";
	public static final boolean DBG = BuildConfig.DEBUG & false;

	private static final int MAX_RETRY = 3;
	private static final int SOCKET_OPERATION_TIMEOUT = 60 * 1000;

	private static DefaultHttpClient mClient;
	private static final CookieManager sCookieManager;

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
		sCookieManager = CookieManager.getInstance();
	}

	public AkHttpClient(Context context) {
		mContext = context;
	}

	public String execute(HttpRequestBase request) throws IOException {
		final String url = request.getURI().toString();

		setWebViewCookie(request, url);

		request.addHeader(AkPlazaFacade.HTTP_HEADER_APP_DEVICE, "Android");
		request.addHeader(AkPlazaFacade.HTTP_HEADER_APP_VERSION, AkPlazaFacade.getVersionName(mContext));

		HttpResponse response = mClient.execute(request);

		if(response.getStatusLine().getStatusCode() != 200) {
			Log.e(TAG, "failed post request  http status code is not 200");
			throw new IOException("서버로부터 에러 응답을 받았습니다. code: " + response.getStatusLine().getStatusCode());
		}

		syncCookieToWebView(url, response);

		return EntityUtils.toString(response.getEntity(), "UTF-8");
	}


	private void syncCookieToWebView(String url, HttpResponse response) {

		Header[] headers = response.getHeaders("Set-Cookie");
		for(Header header : headers) {
			sCookieManager.setCookie(url, header.getValue());
		}

		if(DBG) {
			for(Header header : headers) {
				Log.d(TAG, "sync cookie to webview : " + header.getValue());
			}
		}
	}

	private void setWebViewCookie(HttpRequestBase request, String url) {
		String cookie = sCookieManager.getCookie(url);
		request.setHeader("Cookie", cookie);
		if(DBG) {
		    Log.d(TAG, "set cookie : " + cookie);
		}
	}
}
