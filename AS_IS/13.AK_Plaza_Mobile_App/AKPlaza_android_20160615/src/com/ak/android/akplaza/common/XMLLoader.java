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
package com.ak.android.akplaza.common;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;

public class XMLLoader {

	public static final String TAG = "XMLLoader";

	public static final String FIELD_COOKIE = "Cookie";
	public static final String FIELD_SET_COOKIE = "Set-Cookie";

	private static String keys[] = null;
	private static String number[] = null;
	private static String pricee[] = null;
	private Document doc = null;

	private Context mContext;

	public static String[] getKeys() {
		return keys;
	}

	public static void setKeys(String[] keys) {
		XMLLoader.keys = keys;
	}

	public static String[] getNumber() {
		return number;
	}

	public static void setNumber(String[] number) {
		XMLLoader.number = number;
	}

	public static String[] getPricee() {
		return pricee;
	}

	public static void setPricee(String[] pricee) {
		XMLLoader.pricee = pricee;
	}

	public XMLLoader() {

	}

	public XMLLoader(Context context, String strUrl, String sessionId) {
		mContext = context;
		doc = getDocument(strUrl, sessionId);
	}

	public XMLLoader(Context context, String strUrl) {
		mContext = context;
		doc = getDocument(strUrl);
	}

	public XMLLoader(Context context, InputStream input) {
		mContext = context;
		doc = getDocument(input);
	}

	public Document getDocument() {
		return doc;
	}

	private Document getDocument(InputStream input) {
		Document result = null;
		try {
			DocumentBuilder buider = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			result = buider.parse(input);
		} catch (Exception ex) {
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	private Document getDocument(String strUrl, String sessionId) {
		Document result = null;
		InputStream input = null;
		try {
			input = getURLInput(strUrl, sessionId);
			result = getDocument(input);
		} catch (Exception ex) {
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	private Document getDocument(String strUrl) {
		Document result = null;
		InputStream input = null;
		try {
			input = getURLInput(strUrl);
			result = getDocument(input);
		} catch (Exception ex) {
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	public InputStream getURLInput(String strUrl) throws Exception {
		InputStream input = null;
		URL url = null;

		try {
			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setConnectTimeout(2000);
			http.setRequestProperty(FIELD_COOKIE, getCookieFromWebview(strUrl));
			http.addRequestProperty(AkPlazaFacade.HTTP_HEADER_APP_DEVICE, "Android");
			http.addRequestProperty(AkPlazaFacade.HTTP_HEADER_APP_VERSION, AkPlazaFacade.getVersionName(mContext));

			input = http.getInputStream();

			syncCookieToCookieManager(strUrl, http);

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}

		return input;
	}

	public InputStream getURLInput(String strUrl, String sessionId) throws Exception {
		InputStream input = null;
		URL url = null;

		try {

			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setConnectTimeout(2000);
			http.setRequestMethod("POST");
			http.setRequestProperty(FIELD_COOKIE, getCookieFromWebview(strUrl));
			http.addRequestProperty(AkPlazaFacade.HTTP_HEADER_APP_DEVICE, "Android");
			http.addRequestProperty(AkPlazaFacade.HTTP_HEADER_APP_VERSION, AkPlazaFacade.getVersionName(mContext));

			input = http.getInputStream();

			syncCookieToCookieManager(strUrl, http);

		} catch (Exception ex) {

		}
		return input;
	}

	public static String getNodeValue(Node node) {
		String result = "";
		if (node == null)
			return result;
		Node child = node.getFirstChild();
		if (child == null)
			return result;
		result = child.getNodeValue();
		return result;
	}

	public static String getNodeAttrValue(Node node, String attrName) {
		String result = "";
		if (node == null)
			return "";
		NamedNodeMap nodeMap = node.getAttributes();
		if (nodeMap != null) {
			Node attr = nodeMap.getNamedItem(attrName);
			result = attr.getNodeValue();
		}
		return result;
	}

	public static NodeList getChileNodeList(NodeList parent) {
		for (int i = 0; i < parent.getLength(); i++) {
			Node child = parent.item(i);
			if (child.hasChildNodes()) {

			} else {

			}
		}
		return null;
	}

	private String getCookieFromWebview(String url) {
		return CookieManager.getInstance().getCookie(url);
	}

	private void syncCookieToCookieManager(String strUrl, HttpURLConnection http) {
		List<String> values = http.getHeaderFields().get(FIELD_SET_COOKIE);
		if(values != null) {
			CookieManager cookieMgr = CookieManager.getInstance();
			for(String value : values) {
				cookieMgr.setCookie(strUrl, value);
			}
		}
	}

}
