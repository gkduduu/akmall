package android.ak.com.akmall.utils;

import android.content.ContentValues;

/**
 * Created by gkdud on 2016-11-02.
 * 앱이 실행되는동안 저장되는 값들 singleton
 */

public class Feature {

    private static Feature instance = null;

    public static Feature getInstance() {
        if (null == instance) {
            instance = new Feature();
        }

        return instance;
    }

    public static ContentValues cookie;
}
