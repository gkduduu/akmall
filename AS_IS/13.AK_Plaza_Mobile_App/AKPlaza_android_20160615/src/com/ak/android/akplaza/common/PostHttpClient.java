package com.ak.android.akplaza.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;

public class PostHttpClient {

	public static final String TAG = "PostHttpClient";

	public static final String FIELD_COOKIE = "Cookie";
	public static final String FIELD_SET_COOKIE = "Set-Cookie";

	public static String appLogin(Context context, String strUrl) {

		String result = null;

		InputStream input = null;
		URL url = null;

		try {
			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.addRequestProperty(FIELD_COOKIE, getCookieFromWebview(strUrl));
			http.setConnectTimeout(2000);

			input = http.getInputStream();
			result = convertStreamToString(input);

			syncCookieToCookieManager(strUrl, http);

		} catch (Exception ex) {

		}

		return result;
	}

/*	public static String appLogin(Context context, String strUrl, String sessionId) {

		String result = null;

		InputStream input = null;
		URL url = null;

		try {
			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("cookie", "JSESSIONID=" + sessionId);
			http.setConnectTimeout(2000);

			input = http.getInputStream();
			result = convertStreamToString(input);

			syncCookieToCookieManager(strUrl, http);
		} catch (Exception ex) {

		}

		return result;
	}
*/
	public static String appLoginHttps(Context context, String strUrl) {

		String result = null;

		InputStream input = null;
		URL url = null;

		try {
			trustAllHosts();
			url = new URL(strUrl);
			HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
			https.setRequestMethod("POST");
			https.addRequestProperty(FIELD_COOKIE, getCookieFromWebview(strUrl));
			https.setConnectTimeout(2000);
			https.setHostnameVerifier(DO_NOT_VERIFY);

			input = https.getInputStream();
			result = convertStreamToString(input);

			syncCookieToCookieManager(strUrl, https);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public static String appLogout(Context context, String strUrl) {
		Log.d(TAG, "appLogout");
		String result = null;
		InputStream input = null;
		URL url = null;

		try {
			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.addRequestProperty(FIELD_COOKIE, getCookieFromWebview(strUrl));
			http.setConnectTimeout(2000);

			input = http.getInputStream();
			result = convertStreamToString(input);

			syncCookieToCookieManager(strUrl, http);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	// CONVERT HTTP STREAM TO STRING //
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
    };

    private static String getCookieFromWebview(String url) {
		return CookieManager.getInstance().getCookie(url);
	}

    private static void syncCookieToCookieManager(String strUrl, HttpURLConnection http) {
		List<String> values = http.getHeaderFields().get(FIELD_SET_COOKIE);
		if(values != null) {
			CookieManager cookieMgr = CookieManager.getInstance();
			for(String value : values) {
				cookieMgr.setCookie(strUrl, value);
			}
		}
	}


	/*private static void syncCookieToCookieManager(Context context, String url, HttpURLConnection urlConn) {
		List<String> cookies = urlConn.getHeaderFields().get("Set-Cookie");
		if(cookies != null) {
			for(String cookie : cookies) {
				CookieManager.getInstance().setCookie(url, cookie);
				if(cookie.startsWith("JSESSIONID=")) {
					String[] jsid = cookie.split("=");
					if(jsid.length > 1) {
						SharedUtil.setSharedString(context, "login", "SESSIONID", jsid[1]);
					}
				}
			}
		}
		CookieSyncManager.getInstance().sync();
	}*/

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
              // Create a trust manager that does not validate certificate chains
              TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                      @Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                              return new java.security.cert.X509Certificate[] {};
                      }

                      @Override
					public void checkClientTrusted(X509Certificate[] chain,
                                      String authType) throws CertificateException {
                      }

                      @Override
					public void checkServerTrusted(X509Certificate[] chain,
                                      String authType) throws CertificateException {
                      }
              } };

              // Install the all-trusting trust manager
              try {
                      SSLContext sc = SSLContext.getInstance("TLS");
                      sc.init(null, trustAllCerts, new java.security.SecureRandom());
                      HttpsURLConnection
                                      .setDefaultSSLSocketFactory(sc.getSocketFactory());
              } catch (Exception e) {
                      e.printStackTrace();
              }
      }
    
    public static String HttpConnect(String strUrl) {
		String result = null;

		InputStream input = null;
		URL url = null;

		try {
			url = new URL(strUrl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setConnectTimeout(2000);

			input = http.getInputStream();
			result = convertStreamToString(input);

		} catch (Exception ex) {

		}

		return result;
	}
}
