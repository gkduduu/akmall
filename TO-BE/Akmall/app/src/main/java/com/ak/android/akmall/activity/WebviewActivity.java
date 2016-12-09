package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.fragment.WebviewFragment;
import com.ak.android.akmall.utils.JHYLogger;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

//외부링크 웹뷰
@EActivity(R.layout.activity_webview)
public class WebviewActivity extends Activity {

    @ViewById
    WebView WEB_WEBVIEW;
    @ViewById
    TextView WEB_TITLE;

    @Click(R.id.WEB_CLOSE)
    void clickClose() {
        finish();
    }

    @AfterViews
    void afterView() {
        //웹뷰에 각종 옵션세팅
        WEB_WEBVIEW.clearCache(true);
        WEB_WEBVIEW.bringToFront();
        WEB_WEBVIEW.setInitialScale(100);
        WEB_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEB_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEB_WEBVIEW.getSettings().setUseWideViewPort(true);
        WEB_WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEB_WEBVIEW.loadUrl(getIntent().getStringExtra("url"));
        WEB_WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEB_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String decodeString = "";
            try {
                decodeString = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                JHYLogger.e(e.getMessage());
            }
            JHYLogger.D(decodeString);
            if (decodeString.startsWith("akmall://getTitle")) {
                try {
                    String title = new JSONObject(decodeString.replace("akmall://getTitle?", "")).getString("t");
                    WEB_TITLE.setText(title);
                }catch (Exception e) {

                }
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript: var title = document.getElementsByTagName('title')[0].text; location.href = 'akmall://getTitle?' + encodeURIComponent(JSON.stringify({'t' : title}));");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (WEB_WEBVIEW.canGoBack()) {
                WEB_WEBVIEW.goBack();
                return true;
            }
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
