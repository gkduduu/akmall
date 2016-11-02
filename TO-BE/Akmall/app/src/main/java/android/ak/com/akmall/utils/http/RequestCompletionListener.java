package android.ak.com.akmall.utils.http;

import android.support.annotation.Nullable;

/**
 * Created by gkduduu on 2016. 11. 02..
 */
public interface RequestCompletionListener {
    void onDataControlCompleted(@Nullable Object responseData) throws Exception;
}