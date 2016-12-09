package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.SharedPreferencesManager;
import com.ak.android.akmall.utils.blurbehind.BlurBehind;
import com.ak.android.akmall.utils.blurbehind.OnBlurCompleteListener;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.MainPopupResult;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import net.daum.mf.speech.api.SpeechRecognizerManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    @ViewById
    DrawerLayout ACTIVITY_MAIN;
    @ViewById
    LinearLayout MAIN_SLIDEMENU;

    @ViewById
    WebView SLIDE_WEBVIEW;

    @ViewById
    WebView WEBVIEW;

    ArrayList<String> a = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(this);
        Feature.currentMain = this;
        BaseUtils.updateWidget(this);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
    }

    //웹뷰 리프레시
    public void refreshWeb() {
        WEBVIEW.reload();
    }

    //웹뷰 탭이동(상품리스트에서 탭 이동하는경우가 있음)
    public void refreshWebMoveTab(String url) {
        WEBVIEW.loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!BaseUtils.isAutoLogin()) {
            //자동로그인 아니면 쿠키 조짐
            CookieManager.getInstance().removeAllCookie();
        }
        BaseUtils.updateWidget(this);
    }


    @Click(R.id.FLOATING_MORE)
    void clickMore() {
        BlurBehind.getInstance().execute(MainActivity.this, new OnBlurCompleteListener() {
                    @Override
                    public void onBlurComplete() {
                        Intent intent = new Intent(MainActivity.this, MoreActivity_.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent, Const.MORE_REQUEST);
                    }
                }
        );
    }

    @Click(R.id.FLOATING_TOP)
    void clickTop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WEBVIEW.scrollTo(0, 0);
            }
        }, 10);
    }

    @Click(R.id.FLOATING_BACK)
    void clickBack() {
        if(WEBVIEW.canGoBack()) {
            WEBVIEW.goBack();
        }
    }

    @Click(R.id.MENU_CATEGORY)
    void ClickMenuCate() {
        startActivityForResult(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_CATEGORY), Const.CATEGORY_BIG_REQUEST);
    }

    @Click(R.id.MENU_SEARCH)
    void ClickMenuSearch() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_SEARCH));
    }

    @Click(R.id.MENU_HOME)
    void ClickMenuHome() {
        JHYLogger.D("메인");
        WEBVIEW.loadUrl(URLManager.getServerUrl() + "/main/Main.do?isAkApp=Android");
    }

    @Click(R.id.MENU_MYAK)
    void ClickMenuCMyak() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_MYAK));
    }

    @Click(R.id.MENU_BAG)
    void ClickMenuBag() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
    }

    @AfterViews
    void afterView() {
        WEBVIEW.setInitialScale(100);
        WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEBVIEW.getSettings().setUseWideViewPort(true);
        WEBVIEW.setWebContentsDebuggingEnabled(true);
        WEBVIEW.loadUrl(URLManager.getServerUrl() + "/main/Main.do?isAkApp=Android");
        WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEBVIEW.setWebChromeClient(new ChromeClient());
        WEBVIEW.getSettings().setSupportMultipleWindows(true);

        SLIDE_WEBVIEW.setInitialScale(100);
        SLIDE_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        SLIDE_WEBVIEW.getSettings().setUseWideViewPort(true);
        SLIDE_WEBVIEW.setWebContentsDebuggingEnabled(true);
        SLIDE_WEBVIEW.setWebViewClient(new WebViewClientClass());

        //슬라이드 메뉴
        ACTIVITY_MAIN.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ACTIVITY_MAIN.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ACTIVITY_MAIN.setFocusableInTouchMode(false);
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {

            WebView.HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            JHYLogger.D(data);
            Context context = view.getContext();
            Intent browserIntent = new Intent(MainActivity.this,WebviewActivity_.class ).putExtra("url",data);
            context.startActivity(browserIntent);

            return true;
        }
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.D(url);
            if (url.contains("accounts.google.com")) {
                //g analytics 관련 url 스킵
                return true;
            }
            if (url.startsWith("akmall://")) {
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                if (decodeString.contains("logState")) {
                    BaseUtils.updateWidget(MainActivity.this);
                } else if (decodeString.contains("sendMainPopup")) {
                    List<MainPopupResult> popupList = Parser.parsingMainPopupList(decodeString.replace("akmall://sendMainPopup?", ""));
                    for (int i = popupList.size(); i > 0; i--) {
                        Calendar ca = Calendar.getInstance();
                        String today = ca.get(Calendar.YEAR) + "" + ca.get(Calendar.MONTH) + "" + ca.get(Calendar.DATE);
                        String id = "mainpopup" + popupList.get(i - 1).mobile_popup_id;
                        JHYLogger.D(SharedPreferencesManager.getString(MainActivity.this, id) + " *** " + today);
                        if (!SharedPreferencesManager.getString(MainActivity.this, id).equals(today)) {
                            startActivity(new Intent(MainActivity.this, PopupActivity_.class).putExtra("data", popupList.get(i - 1)));
                        }
                    }
                }
                JHYLogger.D(decodeString);
                return true;
//            } else if (url.contains("akplaza/DeptStore.do?")) {
//                startActivity(new Intent(MainActivity.this, ShopContentActivity_.class).putExtra("url", url));
            } else if (url.contains(URLManager.getServerUrl())) {
                startActivityForResult(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url",url.replace(URLManager.getServerUrl(), "")), Const.CLICK_GO_HOME_SO_CLOSE_REQUEST);
            } else if (url.contains("recopick.com")) {
                startActivity(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", url));
                return true;
            } else if (url.startsWith("http")) {
                startActivity(new Intent(MainActivity.this, WebviewActivity_.class).putExtra("url", ""));

            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieSyncManager.getInstance().sync();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //상품리스트에서 메인의 어떤 탭으로 이동하는 경우가 있어 아래와 같이 처리함
        if (requestCode == Const.CLICK_GO_HOME_SO_CLOSE_REQUEST) {
            if (resultCode == Const.CLICK_GO_HOME_SO_CLOSE_RESULT) {
                String mainUrl = data.getStringExtra(Const.REQUEST_URL);
                if (!data.getStringExtra(Const.REQUEST_URL).equals("")) {
                    WEBVIEW.loadUrl(mainUrl);
                }
            }
            //+버튼 _> 히스트리버튼 클릭
        } else if (requestCode == Const.MORE_REQUEST) {
            if (resultCode == Const.MORE_RESULT) {
                SLIDE_WEBVIEW.loadUrl(URLManager.getServerUrl() + Const.MENU_HISTORY);
                ACTIVITY_MAIN.openDrawer(MAIN_SLIDEMENU);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (ACTIVITY_MAIN.isDrawerOpen(MAIN_SLIDEMENU)) {
                ACTIVITY_MAIN.closeDrawer(MAIN_SLIDEMENU);
                return true;
            }
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
