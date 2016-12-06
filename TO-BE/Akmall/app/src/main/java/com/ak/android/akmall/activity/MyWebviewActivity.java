package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.DataHolder;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.blurbehind.BlurBehind;
import com.ak.android.akmall.utils.blurbehind.OnBlurCompleteListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.BigCategoryResult;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

//akmall내의 웹뷰
@EActivity(R.layout.activity_mywebview)
public class MyWebviewActivity extends Activity {

    @ViewById
    WebView WEB_WEBVIEW;

    @ViewById
    DrawerLayout MY_SLIDELAYOUT;
    @ViewById
    WebView SLIDE_WEBVIEW;
    @ViewById
    LinearLayout MY_SLIDEMENU;

    @ViewById
    LinearLayout MENU_LAYOUT;
    @ViewById
    RelativeLayout FLOATING_LAYOUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Feature.addAcitivty(this);
        CookieSyncManager.createInstance(this);
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
        Feature.removeAcitivty(this);
    }

    @Click(R.id.WEB_CLOSE)
    void clickClose() {
        finish();
    }
    @Click(R.id.FLOATING_MORE)
    void clickMore() {
        BlurBehind.getInstance().execute(this, new OnBlurCompleteListener() {
                    @Override
                    public void onBlurComplete() {
                        Intent intent = new Intent(MyWebviewActivity.this, MoreActivity_.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent,Const.MORE_REQUEST);
                    }
                }
        );
    }
    @Click(R.id.FLOATING_BACK)
    void clickBack() {
        finish();
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
        Feature.closeAllActivity();
    }
    @Click(R.id.MENU_MYAK)
    void ClickMenuCMyak() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_MYAK));
    }
    @Click(R.id.MENU_BAG)
    void ClickMenuBag() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
    }

    String json ="";
    String cate = "";

    @AfterViews
    void afterView() {
        WEB_WEBVIEW.getSettings().setMediaPlaybackRequiresUserGesture(false);

        String extraUrl = getIntent().getStringExtra("url");
        String url= "";
        if(extraUrl.contains(URLManager.getServerUrl())) {
            url = extraUrl;
        }else {
            url = URLManager.getServerUrl() + extraUrl;
        }
        //카테고리일때 파라미터로 쓸 sjon

        cate = BaseUtils.nvl(getIntent().getStringExtra("cate"));
        if(!cate.equals("")) {
            //카테고리일 경우 하단 레이아웃과 플로팅 버튼 gone
            MENU_LAYOUT.setVisibility(View.GONE);
            FLOATING_LAYOUT.setVisibility(View.GONE);
        }

        if(cate.equals("mid") || cate.equals("brand") || cate.equals("small") || cate.equals("small2") || cate.equals("filter")) {
            String holderId = getIntent().getStringExtra("json");
            json = (String) DataHolder.popDataHolder(holderId);
        }else {
            json = getIntent().getStringExtra("json");
        }
        //웹뷰에 각종 옵션세팅
        WEB_WEBVIEW.clearCache(true);
        WEB_WEBVIEW.setInitialScale(100);
        WEB_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEB_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEB_WEBVIEW.setWebContentsDebuggingEnabled(true);
        WEB_WEBVIEW.getSettings().setUseWideViewPort(true);
        WEB_WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEB_WEBVIEW.loadUrl(url + "&isAkApp=Y");
        WEB_WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEB_WEBVIEW.setWebChromeClient(new ChromeClient());

        //슬라이드 메뉴
        MY_SLIDELAYOUT.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MY_SLIDELAYOUT.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        MY_SLIDELAYOUT.setFocusableInTouchMode(false);

        JHYLogger.d(json);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Const.VOICE_REQUEST) {
            if(resultCode == Const.VOICE_RESULT) {
                String voiceResult = BaseUtils.nvl(data.getStringExtra("result"));
                JHYLogger.D(voiceResult);
                try {
                    voiceResult = URLEncoder.encode(voiceResult, "UTF-8");
                    voiceResult = Base64.encodeToString(voiceResult.getBytes(), 0);
                }catch (Exception e) {
                    JHYLogger.e(e.getMessage());
                }
                WEB_WEBVIEW.loadUrl("javascript:voiceSearch('"+ voiceResult +"')");
            }
        }else if (requestCode == Const.MORE_REQUEST) {
            if (resultCode == Const.MORE_RESULT) {
                SLIDE_WEBVIEW.loadUrl(URLManager.getServerUrl() + Const.MENU_HISTORY);
                MY_SLIDELAYOUT.openDrawer(MY_SLIDEMENU);
            }
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
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
                    MyWebviewActivity.this.finish();
                }else if(decodeString.startsWith("akmall://openWebview")) {
                    //대카테고리페이지에서 카테고리 클릭하면 창 끄고  이전페이지 새 카테고리로 로드함
                    if(decodeString.contains("/display/ShopFront.do")) {
                        //대카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    }else if(decodeString.contains("/display/CtgMClsf.do")) {
                        //중카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    }else if(decodeString.contains("/display/BrandShopSClsf.do")) {
                        //브랜드샵
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    }else if(decodeString.contains("/display/CtgSClsf.do")) {
                        //소카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    }else if(decodeString.contains("/display/BrandCtgMClsf.do")) {
                        //브랜드카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    }
                }else if(decodeString.startsWith("akmall://voiceSearch")) {
                    startActivityForResult(new Intent(MyWebviewActivity.this,VoiceActivity_.class),Const.VOICE_REQUEST);
                }else if(decodeString.startsWith("akmall://setup")) {
                    startActivity(new Intent(MyWebviewActivity.this,SettingActivity_.class));
                }else if(decodeString.startsWith("akmall://changeFilter")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://changeFilter?", ""));
                    goContentWebview(openReult.url);
                }
                return true;
            }else if(url.contains("/display/ShopFront.do")) {
                //대카테고리
                url = url.replace(URLManager.getServerUrl(),"");
                goContentWebview(url);
            }else if(url.contains("/display/CtgMClsf.do")) {
                //중카테고리
                url = url.replace(URLManager.getServerUrl(),"");
                goContentWebview(url);
            }else if(url.contains("/display/BrandShopSClsf.do")) {
                //중카테고리
                url = url.replace(URLManager.getServerUrl(),"");
                goContentWebview(url);
            }else if(url.contains("/display/BrandCtgMClsf.do")) {
                //브랜드카테고리
                url = url.replace(URLManager.getServerUrl(),"");
                goContentWebview(url);
            }else {
                view.loadUrl(url);
            }
            return true;
        }

        boolean isFirst = true;
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieSyncManager.getInstance().sync();
            if(isFirst) {
                if (cate.equals("big")) {
                    view.loadUrl("javascript:drawBigCtg('" + json + "')");
                }else if(cate.equals("mid")) {
                    view.loadUrl("javascript:drawMidcate('" + json + "')");
                }else if(cate.equals("brand")) {
                    view.loadUrl("javascript:drawRecomBrand('" + json + "')");
                }else if(cate.equals("small")) {
                    view.loadUrl("javascript:drawSamCate('" + json + "')");
                }else if(cate.equals("small2")) {
                    view.loadUrl("javascript:drawSam2Cate('" + json + "')");
                } else if(cate.equals("filter")) {
                    view.loadUrl("javascript:drawFilter('" + json + "')");
                }
            }
            isFirst = false;
        }
    }

//    //상품 리스트 페이지로 이동
    private void goContentWebview(String link) {
        if( getIntent().getBooleanExtra("isReload",false)) {
            setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",URLManager.getServerUrl() + link));
        }else {
            startActivity(new Intent(MyWebviewActivity.this, ShopContentActivity_.class).putExtra("url", link));
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (MY_SLIDELAYOUT.isDrawerOpen(MY_SLIDEMENU)) {
                MY_SLIDELAYOUT.closeDrawer(MY_SLIDEMENU);
                return true;
            }
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
