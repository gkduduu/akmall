package com.ak.android.akmall.fragment;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.MyWebviewActivity;
import com.ak.android.akmall.activity.ShopContentActivity;
import com.ak.android.akmall.activity.WebviewActivity_;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? : 공유하기
 */

public class SharePopup extends Dialog {

    WebView DIALOG_WEBVIEW;
    String prodNo;
    Context context;

    public static final String INTENT_PROTOCOL_START = "intent:";
    public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
    public static final String INTENT_PROTOCOL_END = ";end;";
    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

    public SharePopup(Context context, String prodNo) {
        super(context);
        this.prodNo = prodNo;
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
        RelativeLayout DIALOG_SHARE = (RelativeLayout) findViewById(R.id.DIALOG_SHARE);

        DIALOG_SHARE.getLayoutParams().height = (int) BaseUtils.convertDpToPixel(268, context);
        DIALOG_SHARE.requestLayout();

        //웹뷰에 각종 옵션세팅
        DIALOG_WEBVIEW.setInitialScale(100);
        DIALOG_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        DIALOG_WEBVIEW.getSettings().setUseWideViewPort(true);
        DIALOG_WEBVIEW.loadUrl(URLManager.getServerUrl() + Const.ITME_SHARE + "&goods_id=" + prodNo);
        DIALOG_WEBVIEW.setWebViewClient(new WebViewClientClass());
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.d(url);
            if (url.startsWith("akmall://")) {
                //URL DECODE
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                JHYLogger.D(decodeString);

                if (decodeString.startsWith("akmall://closePopup")) {
                    SharePopup.this.dismiss();
                }else if(decodeString.contains("akmall://clipboard")) {
                    //클립보드 복사
                    String link = Parser.parsingTString(decodeString.replace("akmall://clipboard?",""));
                    ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", link);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
                }else if(decodeString.startsWith("akmall://openWebview")) {
                    //새 웹뷰 실행
                    OpenWebViewResult result = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?",""));
                    String link = result.url;
                    context.startActivity(new Intent(context, WebviewActivity_.class).putExtra("url", link));
                }else if(decodeString.startsWith("akmall://sms")) {
                    //문자보내기
                    String txt = Parser.parsingTString(decodeString.replace("akmall://sms?",""));
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", txt); // 보낼 문자
                    sendIntent.putExtra("address", ""); // 받는사람 번호
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(sendIntent);
                }

                return true;
            } else if (url.startsWith(INTENT_PROTOCOL_START)) {
                //카카오 링크
                final int customUrlStartIndex = INTENT_PROTOCOL_START.length();
                final int customUrlEndIndex = url.indexOf(INTENT_PROTOCOL_INTENT);
                if (customUrlEndIndex < 0) {
                    return false;
                } else {
                    final String customUrl = url.substring(customUrlStartIndex, customUrlEndIndex);
                    try {
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(customUrl)));
                    } catch (ActivityNotFoundException e) {
                        final int packageStartIndex = customUrlEndIndex + INTENT_PROTOCOL_INTENT.length();
                        final int packageEndIndex = url.indexOf(INTENT_PROTOCOL_END);

                        final String packageName = url.substring(packageStartIndex, packageEndIndex < 0 ? url.length() : packageEndIndex);
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packageName.replace("package=",""))));
                    }
                    return true;
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
