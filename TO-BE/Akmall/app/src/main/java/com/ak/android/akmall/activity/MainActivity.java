package com.ak.android.akmall.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.SharedPreferencesManager;
import com.ak.android.akmall.utils.UriProvider;
import com.ak.android.akmall.utils.blurbehind.BlurBehind;
import com.ak.android.akmall.utils.blurbehind.OnBlurCompleteListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.LogStateResult;
import com.ak.android.akmall.utils.json.result.MainPopupResult;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;
import com.ak.android.akmall.utils.json.result.SMSResult;

import net.daum.mf.speech.api.SpeechRecognizerManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    private static boolean popupCheck = true;
    public static boolean loginCheck = false;
    public static boolean autoLoginCheck = false;

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
        JHYLogger.d("refreshWebMoveTab >> "+url);
        WEBVIEW.loadUrl(url+"?isAkApp=Android");
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
            loginCheck = false;
            JHYLogger.d("자동로그인 아님");
            //자동로그인 아니면 쿠키 조짐
            CookieManager.getInstance().removeAllCookie();
        }
        BaseUtils.updateWidget(this);

        popupCheck = true;
    }

    @Click(R.id.FLOATING_MORE)
    void clickMore() {
//        BlurBehind.getInstance().execute(MainActivity.this, new OnBlurCompleteListener() {
//                    @Override
//                    public void onBlurComplete() {
//                        Intent intent = new Intent(MainActivity.this, MoreActivity_.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        startActivityForResult(intent, Const.MORE_REQUEST);
//                    }
//                }
//        );
//        slide_category.loadUrl(URLManager.getServerUrl() + Const.MENU_HISTORY);
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
        if (WEBVIEW.canGoBack()) {
            WEBVIEW.goBack();
        }
    }

    @Click(R.id.MENU_CATEGORY)
    void ClickMenuCate() {
        startActivityForResult(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_SHOPPINGALIM), Const.CATEGORY_BIG_REQUEST);
    }

    @Click(R.id.MENU_LIKEIT)
    void ClickMenuSearch() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_LIKEIT));
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
        String url = URLManager.getServerUrl() + "/main/Main.do?isAkApp=Android";

        Uri data = getIntent().getData();
        String lastURL = "";

        if (data != null) {
            if(!UriProvider.SCHEME_AKMALL.equals(data.getScheme())) {
                String tmpURL = data.toString();

                if (tmpURL.startsWith("mtracker")) {
                    lastURL = tmpURL.substring(tmpURL.indexOf("http"));
                } else {
                    lastURL = tmpURL;
                }
            }
        }

//        WEBVIEW.setInitialScale(300);
        WEBVIEW.setInitialScale(getScale());
        WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEBVIEW.getSettings().setUseWideViewPort(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WEBVIEW.setWebContentsDebuggingEnabled(true);
        }

        WEBVIEW.loadUrl(url);
        WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEBVIEW.setWebChromeClient(new ChromeClient());
        WEBVIEW.getSettings().setSupportMultipleWindows(true);

        WEBVIEW.getSettings().setBuiltInZoomControls(true);
        WEBVIEW.getSettings().setSupportZoom(true);

        if(Build.VERSION.SDK_INT >= 17) {
            WEBVIEW.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            WEBVIEW.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(WEBVIEW, true);
        }

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        WEBVIEW.getSettings().setTextZoom(100);

//        SLIDE_WEBVIEW.setInitialScale(300);
        SLIDE_WEBVIEW.setInitialScale(getScale());
        SLIDE_WEBVIEW.getSettings().setTextZoom(100);
        SLIDE_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        SLIDE_WEBVIEW.getSettings().setUseWideViewPort(true);

        SLIDE_WEBVIEW.getSettings().setBuiltInZoomControls(true);
        SLIDE_WEBVIEW.getSettings().setSupportZoom(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SLIDE_WEBVIEW.setWebContentsDebuggingEnabled(true);
        }
        SLIDE_WEBVIEW.setWebViewClient(new WebViewClientClass());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            SLIDE_WEBVIEW.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(SLIDE_WEBVIEW, true);
        }

        //슬라이드 메뉴
        ACTIVITY_MAIN.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        ACTIVITY_MAIN.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ACTIVITY_MAIN.setFocusableInTouchMode(false);

        if(!lastURL.equals("")) {
            startActivity(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", lastURL));
        }
    }

    private Context context = MainActivity.this;

    private int getScale()
    {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int PIC_WIDTH = WEBVIEW.getRight() - WEBVIEW.getLeft();
        Double val = new Double(PIC_WIDTH) / new Double(width);
        val = val * 100d;
        return val.intValue();
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            // _blank 처리
            WebView.HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            JHYLogger.D("Main _blank >> "+data);
            Context context = view.getContext();
            Intent browserIntent = new Intent(MainActivity.this, WebviewActivity_.class).putExtra("url", data);
            context.startActivity(browserIntent);

            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if(progress > 10) {
                WEBVIEW.setVerticalScrollBarEnabled(true);
                WEBVIEW.setHorizontalScrollBarEnabled(true);
            }
        }
    }



    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.D("MainActivity >> "+url);
            if (url.contains("accounts.google.com")) {
                //g analytics 관련 url 스킵
                return true;
            }

            if (url.contains("/main/Main.do")) {
                Feature.closeAllActivity();
                Feature.currentMain.refreshWebMoveTab(url);
                return true;
            }

            if (url.startsWith("akmall://")) {
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e("MainActivity >> "+e.getMessage());
                }
                if (decodeString.contains("logState")) {
                    BaseUtils.updateWidget(MainActivity.this);

                    LogStateResult logStateResult = Parser.parsingLogState(decodeString.replace("akmall://logState?", ""));

                    if(logStateResult.r.equals("Y"))
                        loginCheck = true;
                    else
                        loginCheck = false;

                } else if (decodeString.contains("sendMainPopup")) {
                    List<MainPopupResult> popupList = Parser.parsingMainPopupList(decodeString.replace("akmall://sendMainPopup?", ""));
                    for (int i = popupList.size(); i > 0; i--) {

                        Calendar ca = Calendar.getInstance();
                        String today = ca.get(Calendar.YEAR) + "" + ca.get(Calendar.MONTH) + "" + ca.get(Calendar.DATE);
                        String id = "mainpopup" + popupList.get(i - 1).mobile_popup_id;
                        JHYLogger.D("MainActivity >> "+SharedPreferencesManager.getString(MainActivity.this, id) + " *** " + today);

                        if(popupCheck) {
                            if (!SharedPreferencesManager.getString(MainActivity.this, id).equals(today)) {
                                startActivity(new Intent(MainActivity.this, PopupActivity_.class).putExtra("data", popupList.get(i - 1)));
                            }
                            popupCheck = false;
                        }
                    }
                } else if (decodeString.startsWith("akmall://openWebview")) {

                    if (decodeString.contains("/planshop/PlanShopView.do")        || decodeString.contains("/event/EventDetail.do")
                            || decodeString.contains("/display/BrandCtgMClsf.do") || decodeString.contains("/display/CtgSClsf.do")
                            || decodeString.contains("/special/ChanelMain.do")     || decodeString.contains("/special/Special.do")) {

                        String json = decodeString.replace("akmall://openWebview?", "");
                        OpenWebViewResult result = Parser.parsingOpenWebview(json);

                        JHYLogger.d("openWebview URL >> " + result.url);
                        String link = URLManager.getServerUrl() + result.url + "&isAkApp=Android";

                        startActivity(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", link));
                    }
                } else if(decodeString.startsWith("akmall://sms")) {
                    //문자보내기
                    SMSResult openReult = Parser.parsingSMS(decodeString.replace("akmall://sms?", ""));

                    String txt = openReult.t;
                    JHYLogger.d("sms body => " + txt);

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", txt); // 보낼 문자
                    sendIntent.putExtra("address", ""); // 받는사람 번호
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(sendIntent);

                } else if (decodeString.startsWith("akmall://clipboard")) {
                    //클립보드 복사
                    String link = Parser.parsingTString(decodeString.replace("akmall://clipboard?", ""));
                    ClipboardManager clipboardManager = (ClipboardManager) MainActivity.this.getSystemService(MainActivity.this.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", link);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MainActivity.this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
                }

                JHYLogger.D("MainActivity >> "+decodeString);
                return true;
//            } else if (url.contains("akplaza/DeptStore.do?")) {
//                startActivity(new Intent(MainActivity.this, ShopContentActivity_.class).putExtra("url", url));
            } else if (url.contains(URLManager.getServerUrl())) {
                startActivityForResult(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", url.replace(URLManager.getServerUrl(), "")), Const.CLICK_GO_HOME_SO_CLOSE_REQUEST);

            } else if (url.contains("recopick.com")) {
                startActivity(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", url));
                return true;

            } else if (url.startsWith("http")) {
                startActivity(new Intent(MainActivity.this, MyWebviewActivity_.class).putExtra("url", url));
                return true;

            } else if(url.startsWith("newtab")) {  //
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url.replace("newtab:", ""), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e("MainActivity >> "+e.getMessage());
                }

                startActivity(new Intent(MainActivity.this, WebviewActivity_.class).putExtra("url", decodeString));
                return true;

            } else if(url.startsWith("newplay")) {
                startActivity(new Intent(MainActivity.this, WebviewMovieActivity_.class).putExtra("url", url.replace("newplay:", "")));
                return true;

            } else if (url.startsWith("intent") ||
                    url.contains("cpy") ||
                    url.contains("hanaansim") ||
                    url.contains("market://") ||
                    url.contains("com.ahnlab.v3mobileplus") ||
                    url.contains("ahnlabv3mobileplus")) {

                boolean isStarted = true;
                isStarted = BaseUtils.startv3mobileActivity(MainActivity.this, url);
                return true;

            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            WEBVIEW.setVerticalScrollBarEnabled(false);
            WEBVIEW.setHorizontalScrollBarEnabled(false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieSyncManager.getInstance().sync();
        }

        @Override
        public void onReceivedError(final WebView view, int errorCode, String description, final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            switch (errorCode) {
                case ERROR_AUTHENTICATION:               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:                            // 잘못된 URL
                case ERROR_CONNECT:                           // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:     // SSL handshake 수행 실패
                case ERROR_FILE:                                   // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:                // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:            // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:                               // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:                // 너무 많은 리디렉션
                case ERROR_TIMEOUT:                          // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:            // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:                         // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:  // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:

                    if( !(failingUrl.startsWith(URLManager.getServerUrl()) || failingUrl.contains("/search/PowerLink.do")) ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setPositiveButton("앱종료", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.finishAffinity(MainActivity.this);
                            }
                        });

                        builder.setNegativeButton("재시도", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                view.loadUrl(failingUrl);
                            }
                        });
                        builder.setTitle("AK MALL");
                        builder.setMessage("네트워크에 접속할 수 없습니다. 네트워크 연결상태를 확인해 주세요.");
                        builder.show();
                    }

                    break;          // URI가 지원되지 않는 방식
            }
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

    String debug = "";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //볼륨 히든 옵션
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) debug += "a";
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            debug += "b";
            if (Feature.DEBUG_MODE && debug.equals("aabbaab")) {
                if (URLManager.SERVER_URL_DEBUG.equals("m2.akmall.com")) {
                    URLManager.SERVER_URL_DEBUG = "91.3.115.140";

                } else if(URLManager.SERVER_URL_DEBUG.equals("91.3.115.140")) {
                    URLManager.SERVER_URL_DEBUG = "m2.akmall.com";
                }
                Toast.makeText(this, "재접속시 다음 url로 접속됩니다= " + URLManager.getServerUrl(), Toast.LENGTH_SHORT).show();
                debug = "";
            } else if (Feature.DEBUG_MODE && debug.equals("aab")) {
                Toast.makeText(this, "현재 서버 url = " + URLManager.getServerUrl(), Toast.LENGTH_SHORT).show();
            }
        }

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (ACTIVITY_MAIN.isDrawerOpen(MAIN_SLIDEMENU)) {
                ACTIVITY_MAIN.closeDrawer(MAIN_SLIDEMENU);
                return true;

            } else {
                if(WEBVIEW.canGoBack()) {
                    WEBVIEW.goBack();

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setTitle("AK MALL");
                    alert.setMessage("앱을 종료하시겠습니까?");
                    alert.show();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
