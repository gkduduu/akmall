package com.ak.android.akmall.utils.http;

import android.support.annotation.Nullable;

/**
 * Created by gkduduu on 20161102
 */
public interface RequestFailureListener {
    void onDataControlFailed(@Nullable Object responseData, @Nullable Object error);
}