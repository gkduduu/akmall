
package com.ak.android.akmall.gcm;

import com.ak.android.akmall.BuildConfig;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.common.FinishCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class GcmManager {

    public static final String TAG = "GcmManager";
    public static final boolean DBG = BuildConfig.DEBUG & true;

    private static final String SHARED_PREFS_NAME = "gcm";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "app_version";
    private static final String PROPERTY_REGISTERED_SERVER = "registered_server";
    private static final String PROPERTY_REG_ID_PREV = "registration_id_prev";

    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME,
                Context.MODE_PRIVATE);
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

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            return 0;
        }
    }

    private static boolean registerServer(Context context) {
        // 서버에 저장
        boolean isSuccess = AkMallAPI.deviceRegister(context);
        // 결과 저장
        setRegisteredServer(context, isSuccess);
        if (isSuccess) {
            setPrevRegistrationId(context, "");
        }
        return isSuccess;
    }
}
