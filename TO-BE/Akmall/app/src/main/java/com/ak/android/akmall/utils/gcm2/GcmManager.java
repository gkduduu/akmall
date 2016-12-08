package com.ak.android.akmall.utils.gcm2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ak.android.akmall.activity.MainActivity;
import com.ak.android.akmall.utils.SharedPreferencesManager;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

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



    public static String getRegistrationId(Context context) {
        String registrationId = SharedPreferencesManager.getString(context,PROPERTY_REG_ID);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
//            return "";
//        }
        return registrationId;
    }


    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void storeRegistrationId(Context context, String regid) {
        SharedPreferencesManager.setString(context,PROPERTY_REG_ID,regid);
    }
}
