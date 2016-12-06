package com.ak.android.akmall.fragment;


import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.URLManager;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_webview)
public class WebviewFragment extends Fragment {

    @ViewById
    WebView WEBVIEW;

    @AfterViews
    void afterView() {
        //웹뷰에 각종 옵션세팅
        WEBVIEW.clearCache(true);
        WEBVIEW.bringToFront();
        WEBVIEW.setInitialScale(100);
        WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEBVIEW.getSettings().setUseWideViewPort(true);
        WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEBVIEW.loadUrl(URLManager.getServerUrl() + "/main/include/main_section_homeM.jsp");
        WEBVIEW.setWebViewClient(new WebViewClientClass());
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.D(url);
            if (url.startsWith("akmall://")) {

            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
