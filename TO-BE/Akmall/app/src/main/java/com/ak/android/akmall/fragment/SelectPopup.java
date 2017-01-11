package com.ak.android.akmall.fragment;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.MainActivity;
import com.ak.android.akmall.activity.MainActivity_;
import com.ak.android.akmall.activity.MyWebviewActivity;
import com.ak.android.akmall.activity.MyWebviewActivity_;
import com.ak.android.akmall.activity.ShopContentActivity;
import com.ak.android.akmall.activity.WebviewActivity_;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.CheckHeightResult;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
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
    Dialog mDialog;

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

        DIALOG_SHARE.getLayoutParams().height = (int) BaseUtils.convertDpToPixel(268, context);
        DIALOG_SHARE.requestLayout();

        //웹뷰에 각종 옵션세팅
//        DIALOG_WEBVIEW.setInitialScale(100);
        DIALOG_WEBVIEW.getSettings().setTextZoom(100);
        DIALOG_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        DIALOG_WEBVIEW.getSettings().setUseWideViewPort(true);
        DIALOG_WEBVIEW.loadUrl(URLManager.getServerUrl() + link);
        DIALOG_WEBVIEW.setWebViewClient(new WebViewClientClass());
        DIALOG_WEBVIEW.setWebChromeClient(new ChromeClient());
        DIALOG_WEBVIEW.requestFocus(View.FOCUS_DOWN);
//        DIALOG_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_UP:
//                        if (!v.hasFocus()) {
//                            v.requestFocus();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(context)
                    .setTitle("알림")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.confirm();
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();

            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(context)
                    .setTitle("확인")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            }).setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    }).create().show();
            return true;
        }
//        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//            return super.onJsAlert(view, url, message, result);
//        }
//        @Override
//        public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {
//            return super.onJsConfirm(view, url, message, result);
//        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            WebView.HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            JHYLogger.D("Select _blank >> "+data);
//            Context context = view.getContext();
            Intent browserIntent = new Intent(context, WebviewActivity_.class).putExtra("url", data);
            context.startActivity(browserIntent);

            return true;
        }
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

                } else if (decodeString.startsWith("akmall://openWebview")) {
                    //외부 웹 링크 연결
                    String json = decodeString.replace("akmall://openWebview?", "");
                    OpenWebViewResult result = Parser.parsingOpenWebview(json);

                    String link = result.url;
                    context.startActivity(new Intent(context, MyWebviewActivity_.class).putExtra("url", link));
                }

            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (null == mDialog) {
                mDialog = new Dialog(context, R.style.NewDialog);
                mDialog.addContentView(
                        new ProgressBar(context),
                        new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
                mDialog.show();
            }
//            DIALOG_WEBVIEW.setVisibility(View.GONE);
//            DIALOG_SHARE.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }

//            DIALOG_WEBVIEW.setVisibility(View.VISIBLE);
//            DIALOG_SHARE.setVisibility(View.VISIBLE);
        }
    }
}
