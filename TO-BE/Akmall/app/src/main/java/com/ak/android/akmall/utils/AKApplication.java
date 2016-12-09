package com.ak.android.akmall.utils;

import android.app.Application;
import android.webkit.CookieManager;

import com.bumptech.glide.Glide;

/**
 * Created by 하영 on 2016-12-07.
 * gkduduu@naver.com
 * what is? :
 */

public class AKApplication extends Application {
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

}
