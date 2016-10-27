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
package com.ak.android.akmall.imagereview;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;

public class ImageReviewWriter {
	public static final String TAG = "ImageReviewWriter";
	public static final boolean DBG = false;

	private static final int MAX_RETRY = 3;
	private static final int SOCKET_OPERATION_TIMEOUT = 60 * 1000;
	public static final String FIELD_COOKIE = "Cookie";

	private static DefaultHttpClient mClient;

	static {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params,
				SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				params, schemeRegistry);

		mClient = new DefaultHttpClient(manager, params);
		mClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
				MAX_RETRY, true));
	}

	public ImageReviewWriter(Context context) {

	}

	public InputStream write(String destUri, ImageReviewSource imgRevieSource)
			throws IOException {
		if(DBG) Log.d(TAG, "start image review write.. " + destUri + ", source: " + imgRevieSource);

		HttpRequestBase request = createImageReviewRequest(destUri, imgRevieSource);
		request.addHeader(FIELD_COOKIE, getCookieFromWebview(destUri));
		HttpResponse response = mClient.execute(request);

		final int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode != 200) {
			Log.e(TAG, "failed wirte image review.. http status code is not 200");
			int value = response.getStatusLine().getStatusCode();
			throw new IOException("이미지 리뷰 등록에 실패하였습니다. status code: " +  String.valueOf(value) );
		}

		if(DBG)	Log.d(TAG, "success wirte iamge review" + statusCode);

		//if(DBG) Log.d(TAG, URLDecoder.decode(EntityUtils.toString(response.getEntity()),"UTF-8") ) ;
		return response.getEntity().getContent();
	}

	private HttpRequestBase createImageReviewRequest(String destUri, ImageReviewSource imgReviewSource) throws IOException {

		HttpEntityEnclosingRequestBase request = new HttpPost(destUri);

		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName(HTTP.UTF_8));

		entity.addPart("commentTitle", new StringBody(imgReviewSource.getTitle(),Charset.forName("UTF-8") ));
		entity.addPart("commentContents", new StringBody(imgReviewSource.getContent(),Charset.forName("UTF-8") ));

		entity.addPart("eval1", new StringBody(imgReviewSource.getDesignRating()));
		entity.addPart("eval2", new StringBody(imgReviewSource.getPriceRating()));
		entity.addPart("eval3", new StringBody(imgReviewSource.getQualityRating()));
		entity.addPart("eval4", new StringBody(imgReviewSource.getDeliveryRating()));
		if(imgReviewSource.getUploadFile() != null){
			FileBody fbody = new FileBody(imgReviewSource.getUploadFile(), "image/jpeg");

			entity.addPart("attach", fbody );
		}
		request.setEntity(entity);

		return request;
	}

	private String getCookieFromWebview(String url) {
		return CookieManager.getInstance().getCookie(url);
	}

}
