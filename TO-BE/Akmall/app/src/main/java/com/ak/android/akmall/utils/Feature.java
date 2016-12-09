package com.ak.android.akmall.utils;

import android.app.Activity;
import android.content.ContentValues;

import com.ak.android.akmall.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkdud on 2016-11-02.
 * 앱이 실행되는동안 저장되는 값들 singleton
 */

public class Feature {

    private static Feature instance = null;

    //디버그 여부
    public static Boolean DEBUG_MODE = true;

    //로그인 상태
    public static boolean isLogin = false;
    //자동로그인 설정여부
    public static boolean isAutoLogin = false;

    public static Feature getInstance() {
        if (null == instance) {
            instance = new Feature();
        }

        return instance;
    }

    private static List<Activity> activityManageList = new ArrayList<>();

    public static void addAcitivty(Activity activity) {
        activityManageList.add(activity);
    }

    public static void removeAcitivty(Activity activity) {
        activityManageList.remove(activity);
    }

    public static void closeAllActivity() {
        for (int i = 0; i < activityManageList.size(); i++) {
            try {
                activityManageList.get(i).finish();
            } catch (Exception e) {
                JHYLogger.e(e.getMessage());
            }
        }
    }

    public static MainActivity currentMain;

    public static ContentValues cookie;

}
