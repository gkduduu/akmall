package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.SplashResult;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    @ViewById
    WebView SPLASH_WEBVIEW;

    @AfterViews
    void afterView() {
        requestSplash();
    }

    private void initView(SplashResult result) {
        SPLASH_WEBVIEW.setInitialScale(100);
        SPLASH_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        SPLASH_WEBVIEW.getSettings().setUseWideViewPort(true);
        SPLASH_WEBVIEW.setWebContentsDebuggingEnabled(true);
        SPLASH_WEBVIEW.loadUrl(result.link);
    }

    private void requestSplash() {
        //파워링크 가여좀
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestSplash(SplashActivity.this,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
                                initView(Parser.parsingSplash(responseData.toString()));
                            }
                        },
                        new RequestFailureListener() {
                            @Override
                            public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
                            }
                        }
                ));
        DataControlManager.getInstance().runScheduledCommandOnAsync();
    }

}
