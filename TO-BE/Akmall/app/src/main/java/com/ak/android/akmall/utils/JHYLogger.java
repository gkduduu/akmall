package com.ak.android.akmall.utils;

import android.util.Log;

/**
 * Created by gkduduu on 2016-11-28.
 * 하영로거
 */

public class JHYLogger {
    public static void d(String msg) {
        if(null == msg) {
            Log.i("jhy","msg is Null!");
            return;
        }
        if (Feature.DEBUG_MODE) {
            Log.i("jhy", msg);
        }
    }

    //대문자일떄도
    public static void D(String msg) {
        if(null == msg) {
            Log.i("jhy","msg is Null!");
            return;
        }
        if (Feature.DEBUG_MODE) {
            Log.i("jhy", msg);
        }
    }

    public static void e(String msg) {
        Log.e("jhy", msg);
    }
}
