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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.http.Cookies;

public class LoginManager {

    private static final String TAG = "LoginManager";

    public static boolean appAutoLogin(Context context) {
        boolean is = false;
        int auto = SharedUtil.getSharedInt(context, "auto", "AUTOLOGIN");
        String id = SharedUtil.getSharedString(context, "auto", "USERID");
        String pw = SharedUtil.getSharedString(context, "auto", "USERPW");

        if (auto == 1 && !id.equals("") && !pw.equals("")) {
            is = appLogin(context, id, pw);
        }
        return is;
    }

    public static Map appLoginState(Context context) {
        Map map = new HashMap();

        String[] kValue = {
                "LOGIN", "USERID", "SESSIONID"
        };
        for (int i = 0; i < kValue.length; i++) {
            String value = SharedUtil.getSharedString(context, "login", kValue[i]);
            // Log.d("LOGINSTATE", value);
            if (value.equals("") || value.equals(null)) {
                return null;
            }
            map.put(kValue[i], value);
        }
        return map;

    }

    public static boolean isLogin(Cookies cookies) {

        String userId = cookies.getValue("userid");
        String logined = cookies.getValue("logined");

        if ("true".equals(logined) && userId.length() > 0) {
            return true;
        }

        return false;
    }

    public static boolean isLogin(Context context) {

        CookieManager cookieMgr = CookieManager.getInstance();
        Cookies cookies = new Cookies(cookieMgr.getCookie(Const.URL_BASE));

        String userId = cookies.getValue("userid");
        String logined = cookies.getValue("logined");

        if ("true".equals(logined) && userId.length() > 0) {
            return true;
        }

        return false;
    }

    public static void appSetAutoLogin(Context context, int type) {
        if (type == 0) {
            SharedUtil.setSharedInt(context, "auto", "AUTOLOGIN", 1);
            SharedUtil.setSharedInt(context, "auto", "IDSAVE", 0);
            SharedUtil.setSharedString(context, "auto", "USERID",
                    SharedUtil.getSharedString(context, "login", "USERID"));
            SharedUtil.setSharedString(context, "auto", "USERPW",
                    SharedUtil.getSharedString(context, "login", "USERPW"));
        } else {
            if (type == 1) {
                SharedUtil.setSharedInt(context, "auto", "AUTOLOGIN", 0);
                // SharedUtil.setSharedInt(context, "auto", "IDSAVE", 1);
                SharedUtil.setSharedString(context, "auto", "USERID", "");
                SharedUtil.setSharedString(context, "auto", "USERPW", "");
            }
        }
    }

    public static int appGetAutoLogin(Context context) {
        Log.d("AUTO", "AUTO");
        if (SharedUtil.getSharedInt(context, "auto", "AUTOLOGIN") == 1) {
            return 1;
        } else if (SharedUtil.getSharedInt(context, "auto", "IDSAVE") == 1) {
            Log.d("SAVE", SharedUtil.getSharedString(context, "auto", "USERID"));
            return 2;
        } else {
            return 0;
        }
    }

    public static boolean updateDeviceUid(Context context) {
        boolean isUpdateDeviceUid = false;

        Boolean loginState = LoginManager.isLogin(context);
        Log.d(TAG, "loginState : " + loginState);
        if (loginState == true) {
            String token = SharedUtil.getSharedString(context, "C2DM", "token");
            String uId = SharedUtil.getSharedString(context, "login", "USERID");

            if (!token.equals("") && !token.equals(null)) {
                // isUpdateDeviceUid = (Boolean)
                // XMLController.updateDeviceUserid(context, token, uId);
                if (isUpdateDeviceUid) {
                    SharedUtil.setSharedString(context, "DEVICE", "OK", "0");
                    SharedUtil.setSharedString(context, "DEVICE", "ID", uId);
                }
            }
        }

        Log.d(TAG, isUpdateDeviceUid + "");
        return isUpdateDeviceUid;

    }

    public static boolean updateDeviceTokenUserid(Context context) {

        String sessionId = SharedUtil.getSharedString(context, "login", "SESSIONID");
        String token = SharedUtil.getSharedString(context, "C2DM", "token");

        String url = Const.URL_LIB + context.getString(R.string.act_update_device_token_userid, token);

        String strResult = PostHttpClient.appLogin(context, url);

        if (strResult == null) {
            return false;
        }

        try {

            JSONObject jsonResult = new JSONObject(strResult);
            String result = jsonResult.getString("result");

            if ("success".equals(result)) {
                return true;
            }

            return false;

        } catch (JSONException e) {
            Log.e(TAG, "fail device tokent userid..", e);
            return false;
        }
    }

    public static boolean updateLogoutDeviceUid(Context context) {
        boolean isUpdateDeviceUid = false;

        boolean loginState = LoginManager.isLogin(context);
        Log.d(TAG, "loginState : " + loginState);
        if (loginState == true) {
            String token = SharedUtil.getSharedString(context, "C2DM", "token");
            String uId = "";

            if (!token.equals("") && !token.equals(null)) {
                // isUpdateDeviceUid = (Boolean)
                // XMLController.updateDeviceUserid(context, token, uId);
                if (isUpdateDeviceUid) {
                    SharedUtil.setSharedString(context, "DEVICE", "OK", "");
                    SharedUtil.setSharedString(context, "DEVICE", "ID", uId);
                }
            }
        }

        Log.d(TAG, isUpdateDeviceUid + "");
        return isUpdateDeviceUid;

    }

    public static String appGetSavedID(Context context) {
        return SharedUtil.getSharedString(context, "auto", "USERID");
    }

    public static boolean appLogin(Context context, String id, String pw) {

        boolean is = false;
        // 로그인 URL
        String localurl = Const.URL_LIB + context.getString(R.string.act_login);

        // 포스트 파라메터들을 작성한다.

        localurl = localurl.replace("http://", "https://");
        localurl = localurl + "&userid=" + id + "&passwd=" + pw;
        String result = "";
        Log.d(TAG, localurl);
        try {
            // 포스트한다.
            result = PostHttpClient.appLoginHttps(context, localurl);

        } catch (Exception ex) {
            Log.e(TAG, "fail app login..", ex);
            return false;
        }

        try {
            // 포스트한 리스폰스 스트링을 이용해 Json 을 만들어 사용하는 방법
            Log.d(TAG, "login result: " + result);

            JSONObject json = new JSONObject(result);
            Boolean logined = json.getBoolean("logined");

            if (logined == true) {

                String uId = json.getString("userid").toString();
                String uNm = json.getString("username").toString();
                String sId = json.getString("jsessionid").toString();

                SharedUtil.setSharedString(context, "login", "LOGIN", "Success");
                SharedUtil.setSharedString(context, "login", "SESSIONID", sId);
                SharedUtil.setSharedString(context, "login", "USERID", uId);
                SharedUtil.setSharedString(context, "login", "USERPW", pw);

                is = true;
            }

        } catch (Exception e) {
            Log.e(TAG, "fail app login..", e);
        }

        return is;
    }

    public static boolean appSessionLogin(Context context, String sessionId) {
        boolean is = false;

        Map loginMap = null;
        try {
            // loginMap = XMLController.getSessionLogin(context,sessionId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            loginMap = null;
        }
        if (loginMap != null) {
            String result = (String) loginMap.get("GUBUN");
            String sId = (String) loginMap.get("JSESSIONID");
            String uId = (String) loginMap.get("USERID");
            if (result.equals("COMPLETE")) {
                result = "Success";
                SharedUtil.setSharedString(context, "login", "LOGIN", result);
                SharedUtil.setSharedString(context, "login", "SESSIONID", sId);
                SharedUtil.setSharedString(context, "login", "USERID", uId);
                Log.d("LOGIN123", result + " " + sId + " " + uId);
                is = true;
            }
        }

        return is;
    }

    public static boolean appLogout(Context context) {
        boolean is = false;
        String localurl = Const.URL_LIB + context.getString(R.string.act_logout);
        String sessionId = SharedUtil.getSharedString(context, "login", "SESSIONID");
        String result = "";

        Log.d("LOGINMANAGER", localurl);
        try {
            // 포스트한다.
            result = PostHttpClient.appLogout(context, localurl);

        } catch (Exception ex) {
            Log.d("log", "PostException", ex);
            ex.getMessage();
        }

        try {
            // 포스트한 리스폰스 스트링을 이용해 Json 을 만들어 사용하는 방법
            JSONObject json = new JSONObject(result);
            Boolean logout = json.getBoolean("logout");
            String status = json.getString("status");

            if (logout == true || "not logined".equals(status)) {
                // CookieManager.getInstance().removeSessionCookie();
                SharedUtil.clearAllSetting(context, "auto", 0);
                SharedUtil.clearAllSetting(context, "login", 0);
                is = true;
            } else {
                String msg = json.getString("message").toString();
                Log.d("log", "logout =" + json.getString("logout").toString());
                Log.d("log", "msg =" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.d("log","msg ="+ msg);
        }

        return is;

    }

    public static boolean appAutoLogout(Context context) {
        boolean is = false;
        String localurl = Const.URL_LIB + context.getString(R.string.act_logout);
        String sessionId = SharedUtil.getSharedString(context, "login", "SESSIONID");
        String result = "";

        Log.d("LOGINMANAGER", localurl);
        try {
            // 포스트한다.
            result = PostHttpClient.appLogout(context, localurl);
        } catch (Exception ex) {
            Log.d("log", "PostException", ex);
            ex.getMessage();
        }

        try {
            // 포스트한 리스폰스 스트링을 이용해 Json 을 만들어 사용하는 방법
            JSONObject json = new JSONObject(result);
            Boolean logout = json.getBoolean("logout");
            String status = json.getString("status");

            if (logout == true || "not logined".equals(status)) {
                // SharedUtil.clearAllSetting(context, "auto", 0);
                SharedUtil.clearAllSetting(context, "login", 0);
                is = true;
            } else {
                String msg = json.getString("message").toString();
                Log.d("log", "logout =" + json.getString("logout").toString());
                Log.d("log", "msg =" + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.d("log","msg ="+ msg);
        }

        return is;
    }
}
