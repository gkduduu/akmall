package com.ak.android.akmall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;

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

    Dialog mDialog;

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
                WEB_WEBVIEW.scrollTo(0, 0);
            }
        }, 10);
    }

    @Click(R.id.FLOATING_BACK)
    void clickBack() {
        if (WEB_WEBVIEW.canGoBack()) {
            WEB_WEBVIEW.goBack();
        } else {
            finish();
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
        Feature.closeAllActivity();
        Feature.currentMain.refreshWeb();
    }

    @Click(R.id.MENU_MYAK)
    void ClickMenuCMyak() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_MYAK));
    }

    @Click(R.id.MENU_BAG)
    void ClickMenuBag() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
    }

    String json = "";
    String cate = "";

    @AfterViews
    void afterView() {
        WEB_WEBVIEW.getSettings().setMediaPlaybackRequiresUserGesture(false);

        String extraUrl = getIntent().getStringExtra("url");
        String url = "";
        if (extraUrl.contains(URLManager.getServerUrl()) || extraUrl.contains("recopick.com")) {
            url = extraUrl;
        } else {
            url = URLManager.getServerUrl() + extraUrl;
        }
        //카테고리일때 파라미터로 쓸 sjon

        cate = BaseUtils.nvl(getIntent().getStringExtra("cate"));
        if (!cate.equals("")) {
            //카테고리일 경우 하단 레이아웃과 플로팅 버튼 gone
            MENU_LAYOUT.setVisibility(View.GONE);
            FLOATING_LAYOUT.setVisibility(View.GONE);
        }

        if (cate.equals("mid") || cate.equals("brand") || cate.equals("small") || cate.equals("small2") || cate.equals("filter")) {
            String holderId = getIntent().getStringExtra("json");
            json = (String) DataHolder.popDataHolder(holderId);
        } else {
            json = getIntent().getStringExtra("json");
        }

        //웹뷰에 각종 옵션세팅
        WEB_WEBVIEW.clearCache(true);
        WEB_WEBVIEW.setInitialScale(100);
//        WEB_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEB_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEB_WEBVIEW.setWebContentsDebuggingEnabled(true);
        WEB_WEBVIEW.getSettings().setUseWideViewPort(true);
        WEB_WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEB_WEBVIEW.loadUrl(url);
        WEB_WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEB_WEBVIEW.setWebChromeClient(new ChromeClient());
        WEB_WEBVIEW.requestFocus(View.FOCUS_DOWN | View.FOCUS_UP);
        WEB_WEBVIEW.getSettings().setLightTouchEnabled(true);
        WEB_WEBVIEW.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        WEB_WEBVIEW.getSettings().setSupportMultipleWindows(true);

        SLIDE_WEBVIEW.setInitialScale(100);
        SLIDE_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        SLIDE_WEBVIEW.getSettings().setUseWideViewPort(true);
        SLIDE_WEBVIEW.setWebContentsDebuggingEnabled(true);
        SLIDE_WEBVIEW.setWebViewClient(new WebViewClientClass());

        //슬라이드 메뉴
        MY_SLIDELAYOUT.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MY_SLIDELAYOUT.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        MY_SLIDELAYOUT.setFocusableInTouchMode(false);

        JHYLogger.d(json);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.VOICE_REQUEST) {
            if (resultCode == Const.VOICE_RESULT) {
                String voiceResult = BaseUtils.nvl(data.getStringExtra("result"));
                voiceResult = voiceResult.replace("+"," ");
                JHYLogger.D(voiceResult);
                try {
                    voiceResult = URLEncoder.encode(voiceResult, "UTF-8");
                    voiceResult = Base64.encodeToString(voiceResult.getBytes(), 0);
                } catch (Exception e) {
                    JHYLogger.e(e.getMessage());
                }
                WEB_WEBVIEW.loadUrl("javascript:voiceSearch('" + voiceResult + "')");
            }
        } else if (requestCode == Const.MORE_REQUEST) {
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

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {

            WebView.HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            JHYLogger.D(data);
            Context context = view.getContext();
            Intent browserIntent = new Intent(MyWebviewActivity.this,WebviewActivity_.class ).putExtra("url",data);
            context.startActivity(browserIntent);

            return true;
        }
    }

    private static final String URL_SHINHANCARD_APP_DOWN = "http://m.shinhancard.com/fw.jsp?c=a2";
    private static final String URL_ISP_MOBILE_APP_DOWN = "market://details?id=kvp.jjy.MispAndroid320";//p65458 20150727 isp mobile appp download url add


    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.d(url);
            if (url.contains("/main/Main.do")) {
                Feature.closeAllActivity();
                Feature.currentMain.refreshWebMoveTab(url);
            }
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
                } else if (decodeString.startsWith("akmall://openWebview")) {
                    //대카테고리페이지에서 카테고리 클릭하면 창 끄고  이전페이지 새 카테고리로 로드함
                    if (decodeString.contains("/display/ShopFront.do")) {
                        //대카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/CtgMClsf.do")) {
                        //중카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/CtgSClsf.do")) {
                        //소카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/BrandCtgMClsf.do")) {
                        //브랜드카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/BrandCtgSClsf.do")) {
                        //브랜드카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    }
                } else if (decodeString.startsWith("akmall://voiceSearch")) {
                    startActivityForResult(new Intent(MyWebviewActivity.this, VoiceActivity_.class), Const.VOICE_REQUEST);
                } else if (decodeString.startsWith("akmall://setup")) {
                    startActivity(new Intent(MyWebviewActivity.this, SettingActivity_.class));
                } else if (decodeString.startsWith("akmall://changeFilter")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://changeFilter?", ""));
                    goContentWebview(openReult.url);
                } else if (decodeString.startsWith("akmall://goBack")) {
                    if (WEB_WEBVIEW.canGoBack()) {
                        WEB_WEBVIEW.goBack();
                    } else {
                        finish();
                    }
//                } else if (decodeString.startsWith("akmall://showGNB")) {
//                    MENU_LAYOUT.setVisibility(View.VISIBLE);
//                } else if (decodeString.startsWith("akmall://hideGNB")) {
//                    MENU_LAYOUT.setVisibility(View.GONE);
//                } else if (decodeString.startsWith("akmall://showFloat")) {
//                    FLOATING_LAYOUT.setVisibility(View.VISIBLE);
//                } else if (decodeString.startsWith("akmall://hideFloat")) {
//                    FLOATING_LAYOUT.setVisibility(View.GONE);
//                } else if (decodeString.contains("logState")) {
//                    BaseUtils.updateWidget(MyWebviewActivity.this);
                } else if (decodeString.startsWith("akmall://clipboard")) {
                    //클립보드 복사
                    String link = Parser.parsingTString(decodeString.replace("akmall://clipboard?", ""));
                    ClipboardManager clipboardManager = (ClipboardManager) MyWebviewActivity.this.getSystemService(MyWebviewActivity.this.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", link);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MyWebviewActivity.this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (url.contains("/display/ShopFront.do")) {
                //대카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                goContentWebview(url);
            } else if (url.contains("/display/CtgMClsf.do") || url.contains("/display/CtgSClsf.do")) {
                //중카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                goContentWebview(url);
            } else if (url.contains("/display/BrandCtgMClsf.do") || url.contains("/display/BrandCtgSClsf.do")) {
                //브랜드카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                goContentWebview(url);
            }

            //결제모듈 by asis
            if (url.startsWith("intent") || url.contains("cpy") || url.contains("hanaansim") || url.contains("market://") || url.contains("com.ahnlab.v3mobileplus")) {
                boolean isStarted = true;
                isStarted = BaseUtils.startv3mobileActivity(MyWebviewActivity.this, url);
                return true;
            }
            //test
            if (url.startsWith("shinhancard-sr-ansimclick")) {
                boolean isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, url,
                        R.string.abc_action_bar_home_description);
                if (!isStarted) {
                    isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, URL_SHINHANCARD_APP_DOWN, R.string.abc_action_bar_home_description);
                }
                return true;
            }
            /******************************************
             * 결제 처리
             * vguard : 삼성 ,신한
             * v3mobile : 롯데카드
             * driodxantivirus : 현대카드
             * 갤탭경우 : 스키마를 전부 소문자로 인식 대문자 사용불가.
             *******************************************/
            if (url.startsWith("intent://mvaccine")) { //20150508 minseok 신한 사이드로 추
                boolean isStarted = BaseUtils.startv3mobileActivity(MyWebviewActivity.this, url);
                return true;
            } else if (url.startsWith("ispmobile")) {
                boolean isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, url,
                        R.string.abc_action_bar_home_description);

                if (!isStarted) {
                    isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, URL_ISP_MOBILE_APP_DOWN, R.string.abc_action_bar_home_description);
                }
            } else if (url.startsWith("vguard") // vguardstart://, vguard@#$@#://...
                    || url.contains("droidxantivirus")
                    //|| url.contains("v3mobile")
                    || url.startsWith("smshinhanansimclick")
                    || url.startsWith("smshinhancardusim")
                    // || url.contains("vbv")
                    // 현대카드. 2012.02.29일. From: 이정진(NewBiz마케팅팀, jbaram@hyundaicard.com)
                    || url.startsWith("smhyundaiansimclick")
                    || url.contains("ansimclick")
                    || url.startsWith("Lottesmartpay")
                    || url.startsWith("lottesmartpay")
                    || url.startsWith("lotteappcard")) {

                if (!url.startsWith("vguardend")) {
                    boolean isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, url,
                            R.string.abc_action_bar_home_description);

                    return true;
                } else {
                    return true;
                }
            } else if (url.startsWith("intent") || url.contains("cpy") || url.contains("hanaansim") || url.contains("market://") || url.contains("com.ahnlab.v3mobileplus") || url.contains("ahnlabv3mobileplus"))

            {
                boolean isStarted = true;
                isStarted = BaseUtils.startv3mobileActivity(MyWebviewActivity.this, url);
                return true;
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (null == mDialog) {
                mDialog = new Dialog(MyWebviewActivity.this, R.style.NewDialog);
                mDialog.addContentView(
                        new ProgressBar(MyWebviewActivity.this),
                        new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
                mDialog.show();
            }
        }

        boolean isFirst = true;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (null != mDialog && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            CookieSyncManager.getInstance().sync();
            if (isFirst) {
                if (cate.equals("big")) {
                    view.loadUrl("javascript:drawBigCtg('" + json + "')");
                } else if (cate.equals("mid")) {
                    view.loadUrl("javascript:drawMidcate('" + json + "')");
                } else if (cate.equals("brand")) {
                    view.loadUrl("javascript:drawRecomBrand('" + json + "')");
                } else if (cate.equals("small")) {
                    view.loadUrl("javascript:drawSamCate('" + json + "')");
                } else if (cate.equals("small2")) {
                    view.loadUrl("javascript:drawSam2Cate('" + json + "')");
                } else if (cate.equals("filter")) {
                    view.loadUrl("javascript:drawFilter('" + json + "')");
                }
            }
            isFirst = false;
        }

    }

    //    //상품 리스트 페이지로 이동
    private void goContentWebview(String link) {
        if (getIntent().getBooleanExtra("isReload", false)) {
            setResult(Const.CATEGORY_BIG_RESULT, new Intent().putExtra("url", URLManager.getServerUrl() + link));
        } else {
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
            if (WEB_WEBVIEW.canGoBack()) {
                WEB_WEBVIEW.goBack();
                return true;
            }
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
