package com.ak.android.akmall.utils.http;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by gkduduu on 2016. 11. 02..
 */
public class URLManager {
    private static URLManager instance;

    /* URL */
    public final static String SERVER_URL_DEBUG = "91.3.115.140";
    public final static int SERVER_PORT_DEBUG = 10443;

    public final static String SERVER_URL_REAL = "";
    public final static int SERVER_PORT_REAL = 80;


    public static String getServerUrl() {
        return "http://" + SERVER_URL_DEBUG;
    }

    public static URLManager getInstance() {
        if (null == instance) {
            instance = new URLManager();
        }
        return instance;
    }

    //로그인
    public static String getLoginURL() {
        //http://91.3.115.135/login/CheckCustCertNoAjax.do
//        "cust_cert_id" = 1457
        return getServerUrl() + "/login/CheckCustCertNoAjax.do";
    }

    //로그아웃
    public static String getLogoutURL() {
        return getServerUrl() + "/login/Logout.do?native=Y";
    }

    //스플레시
    public static String getSplash() {
        return getServerUrl() + "/app/lib.do?act=appIntro&returnType=json";
    }

    //설정내 유저정보
    public static String getMain() {
        return getServerUrl() + "/app/userInfo.do";
    }

    //푸시등록
    public static String getGCMRegist() {
        return getServerUrl() + "/app/lib.do";
    }

    //상품 베스트
    public static String getGoodsBest() {
        ///display/selectGoodsByBestAjax.do?ctgId=10&pageIdx=1&native=Y
        return getServerUrl() + "/display/selectGoodsByBestAjax.do?ctgId=10&pageIdx=1&native=Y";
    }

    static public String getUrlPathOnly(String urlString) {
        if (urlString.contains("?") == false) {
            return urlString;
        }

        String result;
        result = urlString.substring(0, urlString.indexOf("?"));

        return result;
    }

    @NonNull
    static public String getUrlPathOnly(String urlString, @NonNull String removableScheme) {
        String result;

        if (urlString.contains("?") == false) {
            result = urlString;
        }
        else {
            result = urlString.substring(0, urlString.indexOf("?"));
        }

        if (result.contains(removableScheme)) {
            result = result.replace(removableScheme, "");
        }

        return result;
    }

    static public ContentValues getQueryContentValuesFromUrlString(String urlString) {
        if (urlString.contains("?") == false) {
            return null;
        }

        String queryString = urlString.substring(urlString.indexOf("?") + 1);
        StringTokenizer params = new StringTokenizer(queryString, "&");
        ContentValues resultValues = new ContentValues(params.countTokens());
        while (params.hasMoreTokens()) {
            String[] tokens = params.nextToken().split("=");
            resultValues.put(tokens[0], tokens.length == 1 ? "" : tokens[1]);
        }

        return resultValues;
    }

    static public ArrayList<String> getQueryArrayListFromUrlString(String urlString) {
        if (urlString.contains("?") == false) {
            return null;
        }

        String queryString = urlString.substring(urlString.indexOf("?") + 1);
        StringTokenizer params = new StringTokenizer(queryString, "&");
        ArrayList<String> resultValues = new ArrayList<>(params.countTokens());
        while (params.hasMoreTokens()) {
            resultValues.add(params.nextToken());
        }

        return resultValues;
    }

    static public String getUrlQueryStringFromContentValues(@Nullable ArrayList<String> params) throws UnsupportedEncodingException {
        if (params == null) {
            return "";
        }

        String result;
        ArrayList<String> queryStringsArray = new ArrayList<>(0);


        for (String param : params) {
            StringTokenizer paramTokenizer = new StringTokenizer(param, "=");
            String key = paramTokenizer.nextToken();
            String value = "";
            if (paramTokenizer.countTokens() == 1) {
                //value = URLEncoder.encode(paramTokenizer.nextToken(), "utf-8");
                value = paramTokenizer.nextToken();
            }
            if (value.isEmpty()) {
                continue;
            }

            queryStringsArray.add(key + "=" + value);
        }


        result = TextUtils.join("&", queryStringsArray);

        return result;
    }

    static public String getUrlQueryStringFromContentValues(@Nullable ContentValues params) throws UnsupportedEncodingException {
        if (params == null) {
            return "";
        }

        String result;
        ArrayList<String> queryStringsArray = new ArrayList<>(0);

        for (String key : params.keySet()) {
            //queryStringsArray.add(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key).toString(), "UTF-8"));
            queryStringsArray.add(key + "=" + params.get(key).toString());
        }

        result = TextUtils.join("&", queryStringsArray);
        return result;
    }

}
