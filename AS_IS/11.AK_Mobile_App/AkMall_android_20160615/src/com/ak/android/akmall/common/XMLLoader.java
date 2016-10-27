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
package com.ak.android.akmall.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.gcm.GcmManager;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

public class XMLLoader {

	public static final String TAG = "XMLLoader";

	public static final String FIELD_COOKIE = "Cookie";
	public static final String FIELD_SET_COOKIE = "Set-Cookie";

	private static String keys[] = null;
	private static String number[] = null;
	private static String pricee[] = null;
	private static String disp_class_cdd[] = null;
	private static String disp_class_nmm[] = null;
	private static String disp_class_idd[] = null;

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

	public static String[] getDisp_class_cdd() {
		return disp_class_cdd;
	}

	public static void setDisp_class_cdd(String[] disp_class_cdd) {
		XMLLoader.disp_class_cdd = disp_class_cdd;
	}

	public static String[] getDisp_class_nmm() {
		return disp_class_nmm;
	}

	public static void setDisp_class_nmm(String[] disp_class_nmm) {
		XMLLoader.disp_class_nmm = disp_class_nmm;
	}

	public static String[] getDisp_class_idd() {
		return disp_class_idd;
	}

	public static void setDisp_class_idd(String[] disp_class_idd) {
		XMLLoader.disp_class_idd = disp_class_idd;
	}

	private Document doc = null;

	public XMLLoader() {
	}

	public XMLLoader(String strUrl, String sessionId) {
		doc = getDocument(strUrl, sessionId);
	}

	public XMLLoader(String strUrl) {
		doc = getDocument(strUrl);
	}

	public XMLLoader(InputStream input) {
		doc = getDocument(input);
	}

	public Document getDocument() {
		return doc;
	}

	private Document getDocument(InputStream input) {
		Document result = null;
		try {
			GetDocumentTask documentTask = new GetDocumentTask();
			result = documentTask.execute(input).get();		//p65458 android.os.NetworkOnMainThreadException 예외가 발생했을 경우  20150729 add -> 4.4.2 버전에서 알림설정 화면 진입시 이전 결과갑을 네트워크에서 전달받지 못

			
			//DocumentBuilder buider = DocumentBuilderFactory.newInstance().newDocumentBuilder();;
			//result = buider.parse(input);
		} catch (Exception ex) {
			System.out.println("######"+ex.toString());
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}
	//p65458 android.os.NetworkOnMainThreadException 예외가 발생했을 경우 20150729 add -> 4.4.2 버전에서 알림설정 화면 진입시 이전 결과갑을 네트워크에서 전달받지 못
	//ex : http://ironheel.tistory.com/48
	   private class GetDocumentTask extends AsyncTask<InputStream, Void, Document> {

		   Document result = null;
	        @Override
	        protected Document doInBackground(InputStream... input) {
				DocumentBuilder buider = null;
				try {
					buider = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				try {
					result = buider.parse(input[0]);
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return result;
	        }

	        @Override
	        protected void onPostExecute(Document result) {
	        	//
	        }


	    }
	
		//p65458 android.os.NetworkOnMainThreadException 예외가 발생했을 경우  20150729 add -> 4.4.2 버전에서 알림설정 화면 진입시 이전 결과갑을 네트워크에서 전달받지 못

		//ex : http://ironheel.tistory.com/48

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
		// RetriveXmlTask 클래스를 생성하고 필요한 인수를 전달하여 실행시킨다.
		RetriveXmlTask xmlTask = new RetriveXmlTask();
		input = xmlTask.execute(strUrl).get();//p65458 android.os.NetworkOnMainThreadException 예외가 발생했을 경우 20150722 add
		
//		while(xmlTask.getStatus() != AsyncTask.Status.FINISHED){
//			
//		}
		//input = xmlTask.get();
		/*
		try {
			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setConnectTimeout(2000);
			http.setRequestProperty(FIELD_COOKIE, getCookieFromWebview(strUrl));

			input = http.getInputStream();

			syncCookieToCookieManager(strUrl, http);

		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
		 */
		
		return input;
	}
	
	//p65458 android.os.NetworkOnMainThreadException 예외가 발생했을 경우 20150722 add
	//ex : http://ironheel.tistory.com/48
	// RetriveXmlTask 클래스를 생성하고 필요한 인수를 전달하여 실행시킨다.
	 
	 // AsyncTask<Params,Progress,Result>
	 private class RetriveXmlTask extends AsyncTask<String, Void, InputStream>{
		 InputStream returnInput = null;
	      @Override
	      protected InputStream doInBackground(String... urls) {
	           /*
	          HttpResponse response = null;
	          HttpClient client = new DefaultHttpClient();
	          HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
	          HttpGet httpGet = new HttpGet(urls[0]);
	          try {
	              response = client.execute(httpGet);
	          }
	          catch(ClientProtocolException e){
	              e.printStackTrace();
	          }
	          catch(IOException e) {
	              e.printStackTrace();
	          }
	           
	           */

	   		InputStream input = null;
	   		URL url = null;
	  		try {
				url = new URL(urls[0]);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				http.setConnectTimeout(2000);
				http.setRequestProperty(FIELD_COOKIE, getCookieFromWebview(urls[0]));

				input = http.getInputStream();

				syncCookieToCookieManager(urls[0], http);

			} catch (Exception ex) {
				Log.e(TAG, ex.getMessage(), ex);
			}
	          return input;
	      }
	 
	      @Override
	      protected void onPostExecute(InputStream result) {
	          super.onPostExecute(result);     
	      }

	  }
	 
	//p65458 android.os.NetworkOnMainThreadException 예외가 발생했을 경우 20150722 add
	//ex : http://ironheel.tistory.com/48

	private void syncCookieToCookieManager(String strUrl, HttpURLConnection http) {
		List<String> values = http.getHeaderFields().get(FIELD_SET_COOKIE);
		if(values != null) {
			CookieManager cookieMgr = CookieManager.getInstance();
			for(String value : values) {
				cookieMgr.setCookie(strUrl, value);
			}
		}
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
}
