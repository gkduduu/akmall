package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.fragment.WebviewFragment;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_webview)
public class WebviewActivity extends Activity {

    @ViewById
    WebView WEB_WEBVIEW;

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
        WEB_WEBVIEW.loadUrl("http://akmall.com");
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
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
