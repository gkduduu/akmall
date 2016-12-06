package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.blurbehind.BlurBehind;
import com.ak.android.akmall.utils.blurbehind.OnBlurCompleteListener;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.http.URLManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.cookie.Cookie;

import java.util.ArrayList;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();

        Cookie sessionCookie = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Click(R.id.test)
    void vlic() {
//        ACTIVITY_MAIN.openDrawer(MAIN_SLIDEMENU);
        startActivity(new Intent(this, WebviewActivity_.class));
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
        WEBVIEW.loadUrl(URLManager.getServerUrl() + "/main/Main.do");
        WEBVIEW.setWebViewClient(new WebViewClientClass());

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

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.D(url);
            if (url.contains("accounts.google.com")) {
                //g analytics 관련 url 스킵
                return true;
            }
            if (url.startsWith("akmall://")) {
                return true;
            } else if (url.contains("akplaza/DeptStore.do?")) {
                startActivity(new Intent(MainActivity.this, ShopContentActivity_.class).putExtra("url", url));
            } else if (url.contains(URLManager.getServerUrl())) {
                startActivityForResult(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", url.replace(URLManager.getServerUrl(), "")), Const.CLICK_GO_HOME_SO_CLOSE_REQUEST);
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
