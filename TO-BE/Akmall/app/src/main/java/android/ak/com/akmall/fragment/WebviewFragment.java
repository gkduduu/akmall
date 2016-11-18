package android.ak.com.akmall.fragment;


import android.ak.com.akmall.R;
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
    void afterView(){
        //웹뷰에 각종 옵션세팅
        WEBVIEW.clearCache(true);
        WEBVIEW.bringToFront();
        WEBVIEW.setInitialScale(100);
        WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEBVIEW.getSettings().setUseWideViewPort(true);
        WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEBVIEW.loadUrl("http://akmall.com");
        WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
