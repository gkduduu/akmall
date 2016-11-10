package android.ak.com.akmall.utils.http;

import android.ak.com.akmall.utils.BaseUtils;
import android.ak.com.akmall.utils.Feature;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by gkduduu 20161102
 */
public class DataControlHttpExecutor extends BaseExecutor implements Runnable {
    private String _requestURL;
    private HttpURLConnection _urlConnection;
    private ArrayList<String> _params;

    public DataControlHttpExecutor() {

    }

    public DataControlHttpExecutor(String requestURL, RequestCompletionListener completionListener) {
        this._requestURL = requestURL;
        this._completionListener = completionListener;
    }

    public DataControlHttpExecutor(RequestOperationListener operationLisentener, RequestCompletionListener completionListener) {
        this._operationListener = operationLisentener;
        this._completionListener = completionListener;
    }

    public DataControlHttpExecutor(final float pendingTimer) {
        this._operationListener = new RequestOperationListener() {
            @Override
            public void onRequestOperation(Object responseData) {
                try {
                    Thread.sleep((long) (pendingTimer * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
//    @WorkerThread
    public void run() {
        this._operationListener.onRequestOperation(null);
    }

    //로그인
    public DataControlHttpExecutor requestLogin(@Nullable final Context context, RequestCompletionListener completionListener, RequestFailureListener failureListener) {
        final String requestUrl = new StringBuilder(URLManager.getLoginURL() + "?")
                .append("cust_cert_id=1457")
                .toString();

        Log.i("jhy",requestUrl);
        this._operationListener = new RequestOperationListener() {
            @Override
            public void onRequestOperation(Object responseData) {
                _responseData = sendRecvByHTTP(false, requestUrl, false, context);
            }
        };
        this._completionListener = completionListener;
        this._failureListener = failureListener;

        return this;
    }

    /*
    Internal Operations
     */
    private Object sendRecvByHTTP(boolean isHttps, String requestUrl, Context context) {
        return sendRecvByHTTP(isHttps, requestUrl, true, context);
    }

    private Object sendRecvByHTTP(boolean isHttps, String requestUrl, boolean isPost, Context context) {
        //네트워크 상태 체크
        try {
            if (!BaseUtils.networkCheck(context)) {
                return "networkDisconnect";
            }
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            if (isPost) {
                _params = URLManager.getQueryArrayListFromUrlString(requestUrl);
                requestUrl = URLManager.getUrlPathOnly(requestUrl);
            }

            URL SSLurl = new URL(requestUrl);
            trustAllHosts();

            if (isHttps) {
                _urlConnection = (HttpsURLConnection) SSLurl.openConnection();
                ((HttpsURLConnection) _urlConnection).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } else {
                _urlConnection = (HttpURLConnection) SSLurl.openConnection();
            }
            _urlConnection.setRequestMethod(isPost ? "POST" : "GET");
            _urlConnection.setConnectTimeout(30000);
            _urlConnection.setReadTimeout(30000);

            if (Feature.cookie == null) {

            } else {
                StringBuilder cookiesSB = new StringBuilder();
                for (String aKey : Feature.cookie.keySet()) {
                    Object aValue = Feature.cookie.get(aKey);
                    if ((aKey instanceof String) && (aValue instanceof String)) {
                        cookiesSB.append(aKey).append("=").append(aValue).append("; ");
                    }
                }
                _urlConnection.setRequestProperty("Cookie",String.valueOf(cookiesSB));
            }
            _urlConnection.setDoInput(true);
            _urlConnection.setDoOutput(true);

            // params Post 처리
            if (_params != null) {
                OutputStream os = _urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(URLManager.getUrlQueryStringFromContentValues(_params));
                writer.flush();
                writer.close();
                os.close();
            }
            _urlConnection.connect();
            if (_urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK || _urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                Map receivedHeaderFields = _urlConnection.getHeaderFields();
                List<String> cookieList = (List<String>) receivedHeaderFields.get("Set-Cookie");
                if (cookieList != null) {
                    for (String aCookie : cookieList) {
                        StringTokenizer cookieTokenizer = new StringTokenizer(aCookie, ";");
                        while (cookieTokenizer.hasMoreTokens()) {
                            String[] keyAndValue = cookieTokenizer.nextToken().split("=");
                            if (keyAndValue.length != 2) {
                                continue;
                            }

                            if (Feature.cookie == null) {
                                Feature.cookie = new ContentValues();
                            }

                            Feature.cookie.put(keyAndValue[0], keyAndValue[1]);
                            String cookieName = HttpCookie.parse(aCookie).get(0).getName();
                            String cookieValue = HttpCookie.parse(aCookie).get(0).getValue();

                            String cookieString = cookieName + "=" + cookieValue;
                            CookieSyncManager.createInstance(context);
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.setCookie(URLManager.getServerUrl(), cookieString);
                        }
                    }
                }

                InputStream is = _urlConnection.getInputStream();

                String isString = getStringFromInputStream(is);
                Log.i("jhy",isString.toString());
                is.close();
                _urlConnection.disconnect();
                return isString.toString();
            }
        } catch (UnknownHostException ue) {
            ue.printStackTrace();
            responseError();
        } catch (SocketTimeoutException e) {
            return "<!DOCTYPE html><";
        } catch (Exception e) {
            e.printStackTrace();
            responseError();
        }
        _urlConnection.disconnect();
        return "";
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub
            }
        }};

        // Install the all-trusting trust manager
        try {
            // SSLContext sc = SSLContext.getInstance("TLS");
            // sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(new SSLSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void responseError() {

    }
}
