package com.ak.android.akmall.utils.gcm2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.ak.android.akmall.BuildConfig;
import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.FinishCallback;
import com.ak.android.akmall.utils.PreferenceFacade;
import com.ak.android.akmall.utils.SharedPreferencesManager;
import com.ak.android.akmall.utils.XMLLoader;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? :
 */

public class GcmManager {

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    public static final String PROPERTY_REG_ID = "registration_id";

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "ICELANCER";

    public static final boolean DBG = BuildConfig.DEBUG & true;

    private static final String SHARED_PREFS_NAME = "gcm";
    private static final String PROPERTY_REGISTERED_SERVER = "registered_server";
    private static final String PROPERTY_REG_ID_PREV = "registration_id_prev";


    //
    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void registerInBackground(final Context context, final FinishCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return register(context);
            }

            @Override
            protected void onPostExecute(Boolean isSuccess) {
                if (callback != null) {
                    callback.onFinish(isSuccess);
                }
            }
        }.execute();
    }
    //


//    public static String getRegistrationId(Context context) {
//        String registrationId = SharedPreferencesManager.getString(context,PROPERTY_REG_ID);
//        if (registrationId.isEmpty()) {
//            Log.i(TAG, "Registration not found.");
//            return "";
//        }
//
////        int currentVersion = getAppVersion(context);
////        if (registeredVersion != currentVersion) {
////            Log.i(TAG, "App version changed.");
////            return "";
////        }
//        return registrationId;
//    }


//    public static int getAppVersion(Context context) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            return packageInfo.versionCode;
//
//        } catch (PackageManager.NameNotFoundException e) {
//            // should never happen
//            throw new RuntimeException("Could not get package name: " + e);
//        }
//    }
//    //                      setRegistrationId
//    public static void storeRegistrationId(Context context, String regid) {
//        SharedPreferencesManager.setString(context, PROPERTY_REG_ID, regid);
//    }





    /**
     * GCM에서 push token을 발급 받아 서버에 전송한다.
     * app version이 변경되면 토큰도 업데이트 한다.
     * 모든 과정일 1회 이상 정상 수행한 경우 모든 작업은 수행하지 않고 통과한다.
     *
     * @param context
     * @return 발급 및 서버에 등록 성공하면 true, 실패하면 false
     *         이미 정상 수행한 경우 (발급, 등록)도 true 반환.
     */
    public static boolean register(Context context) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String regId;
        boolean isSuccess = true;

        try {
            regId = getRegistrationId(context);

            // get registeration id
            if (TextUtils.isEmpty(regId)) {
                regId = gcm.register(Const.GCM_SENDER_ID);
                setRegistrationId(context, regId);
                setRegisteredServer(context, false);
            }

            // save registeration id to server
            if (!isRegisteredServer(context)) {
                isSuccess = registerServer(context);
            }

            return isSuccess;

        } catch (IOException ex) {
            Log.e(TAG, "failed register id..");
            isSuccess = false;
        }
        return isSuccess;
    }

    public static boolean isRegisteredServer(Context context) {
        SharedPreferences prefs = getGCMPreferences(context);
        return prefs.getBoolean(PROPERTY_REGISTERED_SERVER, false);
    }

    private static void setRegisteredServer(Context context, boolean registered) {
        SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PROPERTY_REGISTERED_SERVER, registered);
        editor.commit();
    }

    public static String getRegistrationId(Context context) {
        SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            Log.i(TAG, "Registration not found.");
            return registrationId;
        }

        // check if app was updated; if so, it must clear registration id to
        // avoid a race condition if GCM sends a message
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    //private static void setRegistrationId(Context context, String regId) {
    public static void setRegistrationId(Context context, String regId) {//P65458 private -> public
        final int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        if (DBG) {
            Log.d(TAG, "regId is " + regId);
        }
        SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        String prevRegId = prefs.getString(PROPERTY_REG_ID, "");
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        if (!prevRegId.equals(regId)) {
            editor.putString(PROPERTY_REG_ID_PREV, prevRegId);
        }
        editor.commit();
    }

    public static String getPrevRegistrationId(Context context) {
        SharedPreferences prefs = getGCMPreferences(context);
        return prefs.getString(PROPERTY_REG_ID_PREV, "");
    }

    private static void setPrevRegistrationId(Context context, String regId) {
        SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID_PREV, regId);
        editor.commit();
    }

    public static String getRegisteredVersion(Context context) {
        SharedPreferences prefs = getGCMPreferences(context);
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, 0);
        return String.valueOf(registeredVersion);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            Log.e(TAG, "PackageManager.NameNotFoundException !!");
            return 0;
        }
    }

    private static boolean registerServer(Context context) {
        // 서버에 저장
        boolean isSuccess = deviceRegister(context);
        // 결과 저장
        setRegisteredServer(context, isSuccess);
        if (isSuccess) {
            setPrevRegistrationId(context, "");
        }
        return isSuccess;
    }

    //SharedPreferencesManager

    public static boolean deviceRegister(Context context) {

        String gubun = null;
        String token = GcmManager.getRegistrationId(context);
        String oldToken = SharedPreferencesManager.getString(context, "token");
        if (TextUtils.isEmpty(oldToken)) {
            oldToken = GcmManager.getPrevRegistrationId(context);
        }

        if (!TextUtils.isEmpty(token)) {

            String appid = Settings.Secure.getString(context.getContentResolver(),
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
                Log.e(TAG, "UnsupportedEncodingException !!");
                gubun = null;
            }
        }

        return true;
    }

    public static final String PARAM_GUBUN = "gubun";

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
}
