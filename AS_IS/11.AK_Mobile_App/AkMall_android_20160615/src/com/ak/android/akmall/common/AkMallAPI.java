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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;

import com.ak.android.akmall.BuildConfig;
import com.ak.android.akmall.R;
import com.ak.android.akmall.gcm.GcmManager;
import com.ak.android.akmall.http.AkHttpClient;
import com.ak.android.akmall.http.Cookies;
import com.ak.android.akmall.imagereview.ImageReviewSource;
import com.ak.android.akmall.imagereview.ImageReviewWriter;
import com.ak.android.akmall.model.PopularSearchKeyword;
import com.ak.android.akmall.model.SuggestSearchKeyword;
import com.ak.android.akmall.model.UserInfo;
import com.ak.android.akmall.util.SharedUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class AkMallAPI {

    public static final String TAG = "AkMallAPI";
    public static final boolean DBG = BuildConfig.DEBUG & true;

    public static final String PARAM_GUBUN = "gubun";
    public static final String GUBUN_COMPLETE = "COMPLETE";
    public static final String GUBUN_ERROR = "ERRROR"; // kkkk errror????

    private AkMallAPI() {

    }

    public static boolean updateDeny(Context context, String eventId, String deny) {

        boolean isSuccess = false;
        String token = GcmManager.getRegistrationId(context);

        if (!TextUtils.isEmpty(token)) {
            String url = Const.URL_LIB + context.getString(R.string.alarmdenyupdate);
            url = url.replace("#eid#", eventId);
            url = url.replace("#state#", deny);
            url = url.replace("#token#", token);            
            
            XMLLoader loader = new XMLLoader(url);
            Document doc = loader.getDocument();

            try {
                List<Map<String, String>> list = document2List1(doc, "denyupdate");
                if(list != null){//p65458 20150727  null check
	                for (int i = 0; i < list.size(); i++) {
	                    Map<String, String> map = list.get(i);
	                    if (((String) map.get(PARAM_GUBUN)).equals("COMPLETE")) {
	                        isSuccess = true;
	                    }
	                }
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "fail update deny status.. ", e);
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    public static boolean updateAllDeny(Context context, String deny) {

        boolean isSuccess = false;
        String token = GcmManager.getRegistrationId(context);

        if (!TextUtils.isEmpty(token)) {
            String url = Const.URL_LIB
                    + context.getString(R.string.act_update_all_deny, deny, token);
            XMLLoader loader = new XMLLoader(url);
            Document doc = loader.getDocument();

            try {
                List<Map<String, String>> list = document2List1(doc, "denyupdate");
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    if (((String) map.get(PARAM_GUBUN)).equals("COMPLETE")) {
                        isSuccess = true;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "fail update all deny status.. ", e);
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    public static boolean getLogoutXml(Context context, String sessionId) {

        String url = Const.URL_LIB + context.getString(R.string.my_logout);

        XMLLoader loader = new XMLLoader(url, sessionId);
        Document doc = loader.getDocument();

        List<Map<String, String>> list = null;

        try {
            list = document2List1(doc, "xml");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        if (list == null) {
            return false;
        }

        for (Map<String, String> map : list) {
            String state = map.get("Login");
            if ("Logout_Success".equals(state)) {
                return true;
            }
        }

        return false;
    }

    public static String writeImageReview(Context context, String imageReview_data,
            String imageReview_title, int eval1_rating, int eval2_rating, int eval3_rating,
            int eval4_rating, String classCd, String goodCd, File imgFile) {

        String result = "";
        String strUrl = "";
        strUrl = Const.URL_LIB + "act=insertGoodsCommentDo&#CLASS#&#GOOD#";
        strUrl = strUrl.replace("#CLASS#", classCd);
        strUrl = strUrl.replace("#GOOD#", goodCd);

        ImageReviewWriter imgReivewWriter = new ImageReviewWriter(context);
        ImageReviewSource source = new ImageReviewSource();
        source.setTitle(imageReview_title);
        source.setContent(imageReview_data);
        source.setDesignRating(Integer.toString(eval1_rating));
        source.setPriceRating(Integer.toString(eval2_rating));
        source.setQualityRating(Integer.toString(eval3_rating));
        source.setDeliveryRating(Integer.toString(eval4_rating));
        if (imgFile != null) {
            source.setUploadFile(imgFile);
        }

        try {
            XMLLoader xmlLoader = new XMLLoader(imgReivewWriter.write(strUrl, source));

            Document doc = xmlLoader.getDocument();
            List<Map<String, String>> list = document2List1(doc, "result");
            Map<String, String> map = list.get(0);

            result = map.get("status").toString();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            result = "fail";
        }

        return result;
    }

    public static boolean saveNotiTime(Context context, String starthh, String endhh) {

        String token = GcmManager.getRegistrationId(context);
        String url = Const.URL_LIB
                + context.getString(R.string.alarmdenytimeupdate, starthh, endhh, token);

        XMLLoader loader = new XMLLoader(url);
        Document doc = loader.getDocument();

        try {
            List<Map<String, String>> list = document2List1(doc, "denyupdate");

            if (list == null || list.size() == 0) {
                return false;
            }

            Map<String, String> item = list.get(0);
            return GUBUN_COMPLETE.equals(item.get(PARAM_GUBUN));

        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static UserInfo getUserInfo(Context context) {

        String url = Const.URL_LIB + context.getString(R.string.my_userinfo);
        UserInfo userInfo = new UserInfo();

        XMLLoader loader = new XMLLoader(url);
        Document doc = loader.getDocument();

        if (doc == null) {
            return userInfo;
        }

        Map<String, String> userInfoMap = null;

        try {
            List<Map<String, String>> list = document2List1(doc, "userInfo");

            for (int i = 0; i < list.size(); i++) {
                userInfoMap = list.get(i);
            }

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "can't parsing userinfo..", e);
        }

        if (userInfoMap != null && GUBUN_COMPLETE.equals(userInfoMap.get("GUBUN"))) {
            userInfo.setUserid(userInfoMap.get("USERID"));
            userInfo.setName("NAME");
            userInfo.setEmail("EMAIL");
        }

        return userInfo;
    }

    public static Map<String, String> getLoginXml(String loginUrl)
            throws UnsupportedEncodingException {

        Map<String, String> map = new HashMap<String, String>();

        XMLLoader loader = new XMLLoader(loginUrl);
        Document doc = loader.getDocument();
        NodeList menu = doc.getElementsByTagName("xml");

        if (menu != null) {
            for (int i = 0; i < menu.getLength(); i++) {
                Node nodeList = menu.item(i);
                NodeList childs = nodeList.getChildNodes();

                for (int j = 0; j < childs.getLength(); j++) {
                    Node child = childs.item(j);
                    String nodeName = child.getNodeName();

                    if ("Login".equalsIgnoreCase(nodeName)) {
                        map.put("Login", URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
                    }

                    if ("SessionId".equalsIgnoreCase(nodeName)) {
                        map.put("SessionId",
                                URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
                    }

                    if ("UserId".equalsIgnoreCase(nodeName)) {
                        map.put("UserId", URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
                    }
                }
            }
        } else {
            map = null;
        }

        return map;
    }

    /**
     * gubun : 성공시 "COMPLETE", 실패시 "ERROR", 이미 등록된 경우 "DUPLICATE" c2dm 토큰은 삭제하도록
     * 하고 gcm 토큰을 서버에 저장 한다.
     * 
     * @param context
     * @return 성공시 true, 실패시 false.
     */
    public static boolean deviceRegister(Context context) {

        String gubun = null;
        String token = GcmManager.getRegistrationId(context);
        String oldToken = SharedUtil.getSharedString(context, "C2DM", "token");
        if (TextUtils.isEmpty(oldToken)) {
            oldToken = GcmManager.getPrevRegistrationId(context);
        }

        if (!TextUtils.isEmpty(token)) {

            String appid = Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            // on: 허용, off: 거절
            String deny_all = PreferenceFacade.isAllowPush(context) ? "no" : "yes";
            String registeredVersion = GcmManager.getRegisteredVersion(context);
            String url = Const.URL_LIB
                    + context.getString(R.string.devicereg, token, appid, deny_all, oldToken,
                            registeredVersion);
            
            System.out.println("!!!!! : " + url);
            
            if (DBG) {
                Log.d(TAG, "old token : " + oldToken);
                Log.d(TAG, "new token : " + token);
                Log.d(TAG, "version : " + registeredVersion);
                Log.d(TAG, "deny_all : " + deny_all);
            }

            XMLLoader loader = new XMLLoader(url);
            Document doc = loader.getDocument();

            if (doc == null) {
                return false;
            }

            try {
                List<Map<String, String>> list = document2List1(doc, "deviceregist");
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    gubun = (String) map.get(PARAM_GUBUN);
                }
            } catch (UnsupportedEncodingException e) {
                gubun = null;
            }
        }

        if ("COMPLETE".equals(gubun) || "DUPLICATE".equals(gubun)) {
            // c2dm 토큰 삭제, gcm으로 대체 됨.
            if (!TextUtils.isEmpty(oldToken)) {
                SharedUtil.clearAllSetting(context, "C2DM", Context.MODE_PRIVATE);
            }
            return true;
        } else {
            return false;
        }
    }

    public static List<Map<String, String>> getAlarmList(Context context) {
        List<Map<String, String>> list = null;

        String url = Const.URL_LIB + context.getString(R.string.notisetBox);
        //테스트 /* TO DO
        //String url = "http://m.akmall.com/app/lib.do?"+context.getString(R.string.notisetBox);
        
        XMLLoader loader = new XMLLoader(url);
        Document doc = loader.getDocument();
        try {
            list = document2List(doc, "alarm");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    public static Map<String, List<Map<String, String>>> getAlarmDenyList(Context context) {
        Map<String, List<Map<String, String>>> result = null;


        String url = Const.URL_LIB
                + context.getString(R.string.alarmdenylist);
        
        String token = GcmManager.getRegistrationId(context);
        
        if (!TextUtils.isEmpty(token)) {
            url = url.replace("#token#", token);
            Log.d(TAG, "XMLLoader token: " + url);
            XMLLoader loader = new XMLLoader(url);
            Document doc = loader.getDocument();

            if (DBG) {
                Log.d(TAG, "get alarm deny list: " + url);
            }

            try {
                if (doc != null) {
                    result = new HashMap<String, List<Map<String, String>>>();
                    result.put("DENY", document2List(doc, "deny"));
                    result.put("SPECIALDAY", document2List(doc, "specialday"));
                    result.put("DENYTIME", document2List(doc, "denytime"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                result = null;
            }
        }

        return result;
    }

    private static List<Map<String, String>> document2List(Document doc, String findValue)
            throws UnsupportedEncodingException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        NodeList menu = doc.getElementsByTagName(findValue);
        if (menu != null) {
            for (int j = 0; j < menu.getLength(); j++) {
                Node nodeList = menu.item(j);
                if (nodeList.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childs = nodeList.getChildNodes();

                    for (int k = 0; k < childs.getLength(); k++) {
                        Map<String, String> rowData = new HashMap<String, String>();
                        Node child = childs.item(k);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            NodeList items = child.getChildNodes();
                            for (int l = 0; l < items.getLength(); l++) {
                                Node item = items.item(l);
                                if (item.getNodeType() == Node.ELEMENT_NODE) {
                                    String nodeName = item.getNodeName();
                                    String value = XMLLoader.getNodeValue(item);
                                    rowData.put(nodeName, URLDecoder.decode(value, "UTF-8"));
                                }
                            }
                            list.add(rowData);
                        }
                    }
                }
            }
        } else {
            list = null;
        }
        return list;
    }

    private static List<Map<String, String>> document2List1(Document doc, String findValue)
            throws UnsupportedEncodingException {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if(doc != null){//p65458 20150727  null check
	        NodeList menu = doc.getElementsByTagName(findValue);
	        if (menu != null) {
	
	            for (int k = 0; k < menu.getLength(); k++) {
	                Map<String, String> rowData = new HashMap<String, String>();
	                Node child = menu.item(k);
	                if (child.getNodeType() == Node.ELEMENT_NODE) {
	                    NodeList items = child.getChildNodes();
	                    for (int l = 0; l < items.getLength(); l++) {
	                        Node item = items.item(l);
	                        if (item.getNodeType() == Node.ELEMENT_NODE) {
	                            String nodeName = item.getNodeName();
	                            String value = XMLLoader.getNodeValue(item);
	                            rowData.put(nodeName, URLDecoder.decode(value, "UTF-8"));
	                        }
	                    }
	                    list.add(rowData);
	                }
	            }
	        } else {
	            list = null;
	        }
        }else{
        	 list = null;
        }
        return list;
    }

    public static boolean isLogin(Context context) {
        String cookie = CookieManager.getInstance().getCookie(Const.URL_BASE);
        Cookies cookies = new Cookies(cookie);
        return "Y".equals(cookies.getValue("mobileLogin"));
    }

    public static boolean doAutoLogin(Context context) {

        if (!PreferenceFacade.isAutoLogin(context)) {
            return false;
        }

        String userId = SharedUtil.getSharedString(context, "auto", "USERID");
        String password = SharedUtil.getSharedString(context, "auto", "USERPW");

        if (userId.length() != 0 && password.length() != 0) {
            return doLogin(context, userId, password);
        }

        return false;
    }

    public static void doAutoLoginInBackground(final Context context, final FinishCallback callback) {

        if (!PreferenceFacade.isAutoLogin(context)) {
            if (callback != null) {
                callback.onFinish(false);
            }
            return;
        }

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                String userId = SharedUtil.getSharedString(context, "auto", "USERID");
                String password = SharedUtil.getSharedString(context, "auto", "USERPW");

                if (userId.length() != 0 && password.length() != 0) {
                    return doLogin(context, userId, password);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (callback != null) {
                    callback.onFinish(result);
                }
            }

        }.execute();
    }

    public static boolean doLogin(Context context, String id, String pw) {
        String loginUrl = Const.URL_LIB
                + context.getString(R.string.my_login, id, pw);

        Map<String, String> loginMap = null;
        try {
            loginMap = AkMallAPI.getLoginXml(loginUrl);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        if (loginMap != null) {
            String result = loginMap.get("Login");
            if ("Success".equals(result)) {
                AkMallFacade.getUserInfo(context).setUserid(loginMap.get("UserId"));
                return true;
            }
        }

        return false;
    }

    public static boolean doLogout(Context context) {
        boolean isSuccess = false;

        String sessionId = SharedUtil.getSharedString(context, "login", "SESSIONID");
        isSuccess = AkMallAPI.getLogoutXml(context, sessionId);

        if (isSuccess) {
            SharedUtil.clearAllSetting(context, "auto", 0);
            SharedUtil.clearAllSetting(context, "login", 0);
        }

        return isSuccess;
    }

    public static boolean updateDeviceUserId(Context context) {

    	 if (!GcmManager.isRegisteredServer(context)) {
            return false;
        }

        //String token = GcmManager.getRegistrationId(context);//p65458 20150907 app_version update 시 내부에 저장되어 있던 push id 대신 새로 발급 받아 버전 정보와 함께 업데이트 하도록 수정
    	//p65458 20150907 app_version update 시 내부에 저장되어 있던 push id 대신 새로 발급 받아 버전 정보와 함께 업데이트 하도록 수정
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

        String token = "";
		try {
			token = gcm.register(Const.GCM_SENDER_ID);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GcmManager.setRegistrationId(context, token);
		//p65458 20150907 app_version update 시 내부에 저장되어 있던 push id 대신 새로 발급 받아 버전 정보와 함께 업데이트 하도록 수정
        if (TextUtils.isEmpty(token)) {
            return false;
        }

        String registeredVersion = GcmManager.getRegisteredVersion(context);
        String url = Const.URL_LIB
                + context.getString(R.string.updateduserid, token, registeredVersion);
        
        if (DBG) {
            Log.d(TAG, "updateDeviceUserid : " + url);
        }

        XMLLoader loader = new XMLLoader(url);
        Document doc = loader.getDocument();

        List<Map<String, String>> list = null;
        try {
            list = document2List1(doc, "updateduser");
        } catch (UnsupportedEncodingException e) {
            // ignore error..
        }

        if (list == null) {
            return false;
        }

        String gubun;
        for (Map<String, String> map : list) {
            gubun = map.get(PARAM_GUBUN);
            if ("COMPLETE".equals(gubun)) {
                return true;
            }
        }

        return false;
    }

    // public static boolean updateLogoutDeviceUid(Context context) {
    // boolean isUpdateDeviceUid = false;
    //
    // boolean loginState = isLogin(context);
    //
    // if (loginState == true) {
    // String token = GcmManager.getRegistrationId(context);
    //
    // if (!TextUtils.isEmpty(token)) {
    // isUpdateDeviceUid = AkMallAPI.updateDeviceUserid(context, token);
    // if (isUpdateDeviceUid) {
    // SharedUtil.setSharedString(context, "DEVICE", "OK", "");
    // }
    // }
    // }
    //
    // return isUpdateDeviceUid;
    // }

    public static List<SuggestSearchKeyword> getSuggestSearchKeyword(Context context, String query) {

        String url = Const.URL_BASE
                + context.getString(R.string.uri_search_suggest, URLEncoder.encode(query));

        List<SuggestSearchKeyword> list = new ArrayList<SuggestSearchKeyword>();

        AkHttpClient client = new AkHttpClient(context);
        HttpGet request = new HttpGet(url);
        String resultStr = null;

        if (DBG)
            Log.d(TAG, "get suggest keyword: " + url);

        try {
            resultStr = client.execute(request);
            JSONArray result = new JSONArray(resultStr);
            JSONObject object;
            for (int i = 0, len = result.length(); i < len; i++) {
                object = result.getJSONObject(i);
                list.add(new SuggestSearchKeyword(URLDecoder.decode(object.getString("label")),
                        URLDecoder.decode(object.getString("value"))));
            }
        } catch (Exception e) {
            // ignore error
            Log.e(TAG, "fail get suggest search keyword..", e);
        }

        return list;
    }

    public static List<PopularSearchKeyword> getPopularSearchKeyword(Context context, int limit) {

        String url = Const.URL_BASE
                + context.getString(R.string.uri_search_popular, limit);

        List<PopularSearchKeyword> list = new ArrayList<PopularSearchKeyword>();

        AkHttpClient client = new AkHttpClient(context);
        HttpGet request = new HttpGet(url);
        String resultStr = null;

        if (DBG)
            Log.d(TAG, "get popular keyword url: " + url);

        try {
            resultStr = client.execute(request);
            JSONArray result = new JSONArray(resultStr);
            JSONObject o;
            for (int i = 0, len = result.length(); i < len; i++) {
                o = result.getJSONObject(i);
                list.add(new PopularSearchKeyword(o.getInt("ID"), URLDecoder.decode(o
                        .getString("QUERY")), o.getInt("RANK"), o.getInt("HIT"), o
                        .getString("DISP_CLASS_CD")));
            }

        } catch (Exception e) {
            // ignore error
            Log.e(TAG, "fail get popular search keyword..", e);
        }

        return list;
    }
    
    /**
     * 자동 로그인 상태 인지 확인한다.
     *
     * @return
     */
    public static boolean isAutoLogined() {
        String cookie = CookieManager.getInstance().getCookie(Const.URL_BASE);
        Cookies cookies = new Cookies(cookie);
        String autoLoginToken = cookies.getValue("loginToken");
        return !TextUtils.isEmpty(autoLoginToken);
    }
}
