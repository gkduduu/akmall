package com.ak.android.akmall.fragment;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.WebviewActivity_;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.CheckHeightResult;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? : 셀렉트 팝업
 */

public class SelectPopup extends Dialog {

    WebView DIALOG_WEBVIEW;
    RelativeLayout DIALOG_SHARE;
    String link;
    Context context;

    public static final String INTENT_PROTOCOL_START = "intent:";
    public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
    public static final String INTENT_PROTOCOL_END = ";end;";
    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

    public SelectPopup(Context context, String link) {
        super(context);
        this.link = link;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_share);

        DIALOG_WEBVIEW = (WebView) findViewById(R.id.DIALOG_WEBVIEW);
        DIALOG_SHARE = (RelativeLayout) findViewById(R.id.DIALOG_SHARE);

        //웹뷰에 각종 옵션세팅
        DIALOG_WEBVIEW.setInitialScale(100);
        DIALOG_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        DIALOG_WEBVIEW.getSettings().setUseWideViewPort(true);
        DIALOG_WEBVIEW.loadUrl(URLManager.getServerUrl() + link);
        DIALOG_WEBVIEW.setWebViewClient(new WebViewClientClass());
        DIALOG_WEBVIEW.requestFocus(View.FOCUS_DOWN);
        DIALOG_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.d("SelectPopup >> " + url);
            if (url.startsWith("akmall://")) {
                //URL DECODE
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                JHYLogger.D("SelectPopup >> "+decodeString);

                if (decodeString.startsWith("akmall://checkHeight")) {
                    CheckHeightResult result = Parser.parsingCheckHeight(decodeString.replace("akmall://checkHeight?", ""));
                    DIALOG_SHARE.getLayoutParams().height = (int) BaseUtils.convertDpToPixel(Float.parseFloat(BaseUtils.nvl(result.h, "0")), context);
                    DIALOG_SHARE.requestLayout();

                } else if(decodeString.startsWith("akmall://closePopup")) {
                    SelectPopup.this.cancel();
                }

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
}
