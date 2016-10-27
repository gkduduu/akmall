
package com.ak.android.akplaza.widget;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;

import kr.co.kcp.util.PackageState;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.CacheManager;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ak.android.akplaza.BuildConfig;
import com.ak.android.akplaza.R;
import com.ak.android.akplaza.alarm.NotiBoxActivity;
import com.ak.android.akplaza.common.AKPlazaApplication;
import com.ak.android.akplaza.common.ActivityTaskManager;
import com.ak.android.akplaza.common.AkPlazaAPI;
import com.ak.android.akplaza.common.AkPlazaFacade;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.LoginManager;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.WebViewActivity;
import com.ak.android.akplaza.http.Cookies;
import com.ak.android.akplaza.login.LoginMainActivity;
import com.ak.android.akplaza.mobileevent.MobileEventFacade;

public class AkPlazaWebView extends WebView {

    public static final String TAG = "AkPlazaWebView";
    public static final boolean DBG = BuildConfig.DEBUG & true;

    private static final String SCHEME_HTTP = "http:";
    private static final String SCHEME_HTTPS = "https:";

    private Context mContext;
    private WebViewListener mWebViewListener;
    private static MobileEventFacade mMobileEventFacade;
    private boolean mEnabledZoom = false;
    private static boolean mIsNotUpdatDeviceTokenUserid = true;
    private static AkPlazaAPI mAPI;
    private CookieManager mCookieMgr;
    
    
    public static final String   ACTIVITY_RESULT         = "ActivityResult";
    public static final int      PROGRESS_STAT_NOT_START = 1;
    public static final int      PROGRESS_STAT_IN        = 2;
    public static final int      PROGRESS_DONE           = 3;
    public static       int      m_nStat                 = PROGRESS_STAT_NOT_START;
    private final       Handler  handler                 = new Handler();
    public static Activity activity = null;


	public AkPlazaWebView(Context context) {
        this(context, null);
    }
	

    public AkPlazaWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public AkPlazaWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setup();
    }

    public void setWebViewListener(WebViewListener listener) {
        mWebViewListener = listener;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setup() {

        mCookieMgr = CookieManager.getInstance();
        mMobileEventFacade = new MobileEventFacade(mContext);
        mAPI = new AkPlazaAPI(mContext);

        WebSettings setting = getSettings();
        setting.setSavePassword(false);
        setting.setAppCacheEnabled(true);
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setGeolocationEnabled(true);
        setting.setSaveFormData(false);
        setting.setLoadsImagesAutomatically(true);
        setting.setBuiltInZoomControls(false);
        setting.setSupportZoom(false);
        setting.setSavePassword(false);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);
        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        setting.setSupportMultipleWindows(false);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);

        addExtraHeader(setting, AkPlazaFacade.getAppInfoHttpHeaderString(mContext));
        checkAndDisableDiskCache();
        setVerticalScrollbarOverlay(true);

        setWebChromeClient(mDefaultWebChromeClient);
        setWebViewClient(mDefaultWebViewClient);
        addJavascriptInterface(new KCPPayBridge()        , "KCPPayApp"     );
        addJavascriptInterface(new KCPPayPinInfoBridge() , "KCPPayPinInfo" ); // 페이핀 기능 추가
        addJavascriptInterface(new KCPPayPinReturn()     , "KCPPayPinRet"  ); // 페이핀 기능 추가
    }

    /**
     * trick.. add extra http header
     * 
     * @param setting
     * @param extraHeaderString HTTP 규격에 맞는 헤더들..
     */
    private void addExtraHeader(WebSettings setting, String extraHeaderString) {
        setting.setUserAgentString(setting.getUserAgentString() + "\n" + extraHeaderString);
    }

    private void checkAndDisableDiskCache() {

        // IO 성능이 떨어지는 갤럭시 S, 갤럭시 K, 갤럭시 U 모델에서 디스크 캐시 끔.
        // SHW-M110S, SHW-M130L, SHW-M130K
        if (Build.MODEL.equals("SHW-M110S") || Build.MODEL.startsWith("SHW-M130")) {
            try {
                Method m = CacheManager.class.getDeclaredMethod("setCacheDisabled", boolean.class);
                m.setAccessible(true);
                m.invoke(null, true);
                Log.i(TAG, "disabled disk cache..");
            } catch (Throwable e) {
                Log.w(TAG, "failed disable disk cache..", e);
            }
        }
    }

    private WebChromeClient mDefaultWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                final android.webkit.JsResult result) {

            new AlertDialog.Builder(mContext).setIcon(R.drawable.logo_small)
                    .setTitle(R.string.alert).setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setCancelable(false).show();

            return true;
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
            callback.invoke(origin, true, false);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mWebViewListener != null) {
                mWebViewListener.onProgressChanged(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    private WebViewClient mDefaultWebViewClient = new WebViewClient() {

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, false);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            loadUrl("file:///android_asset/networkerr.html");
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (DBG) {
                Log.d(TAG, "onPageStarted: " + url);
            }

            if (mWebViewListener != null) {
                mWebViewListener.onPageStarted(url);
            }

            fixBuiltInZoomControls(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	
            if (DBG) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
            }
            

            if (url == null) {
                return false;
            }
            
            if (url.indexOf("about:blank") > -1) {
                return false;
            }

            // tel: 전화걸기..
            if (url.startsWith("tel")) {
                AkPlazaFacade.showCallDialog(mContext, url);
                return true;
            }

            if (url.indexOf("act=appQRCode") > -1) {
                AkPlazaFacade.startQrCodeScanActivityForResult(mContext);
                return true;
            }

            if (url.indexOf("act=appleReceiptReg") > -1) {
                AkPlazaFacade.startBarCodeScanActivityForResult(mContext);
                return true;
            }

            // 로그인/알림설정
            if (url.indexOf("act=applogin") > -1) {
                Intent intent = new Intent(mContext, LoginMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent);

                return true;

            }

            // 알림보관함
            if (url.indexOf("act=appAlimList") > -1) {
                Intent intent = new Intent(mContext, NotiBoxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent);

                return true;
            }

            // 구글어플 띄우기
            if (url.indexOf("branch/branch.do?act=location&bc=") > -1
                    && url.indexOf("isAppMap=Y") > -1) {
                startExternalMapApp(url);
                return true;
            }

            // 기타 다른 앱으로 처리 넘김..
            if (url.startsWith("http://market.android.com") || url.startsWith("market:")
                    || url.endsWith(".apk") || "http://www.akplaza.com/?in_mobile=Y".equals(url)) {
                AkPlazaFacade.startExternalActivity(mContext, url);
                return true;
            }
            

            // 회원가입
            if (url.contains(Const.URL_MEMBERS)) {
                int versionCode = AkPlazaFacade.getVersionCode(mContext);
                url += mContext.getString(R.string.param_signup, versionCode);
                AkPlazaFacade.startExternalActivity(mContext, url);
                return true;
            }
            
            //페이스북
            if (url.startsWith("fb://")) {
            	Intent startLink = activity.getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
            	if(startLink == null) {
            		return url_scheme_intent( null, "market://details?id=com.facebook.katana" );
            	}
            	return AkPlazaFacade.startExternalActivity(mContext, url);
            }
            
            //카카오스토리
            if (url.startsWith("storylink://")) {
            	Intent startLink = activity.getPackageManager().getLaunchIntentForPackage("com.kakao.story");
            	if(startLink == null) {
            		return url_scheme_intent( null, "market://details?id=com.kakao.story" );
            	}
            	return AkPlazaFacade.startExternalActivity(mContext, url);
            }
            
            //인스타그램
            if (url.startsWith("instagram://")) {
            	Intent startLink = activity.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
            	if(startLink == null) {
            		return url_scheme_intent( null, "market://details?id=com.instagram.android" );
            	}
            	return AkPlazaFacade.startExternalActivity(mContext, url);
            }
            
            // 유투브 URL 포함시 유튜브 동영상으로 재생 //20151207 박보람
            if (url.indexOf("youtube.com") > -1 || url.indexOf("youtu.be") > -1) {
            	Intent startLink = activity.getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            	if(startLink == null) {
            		AkPlazaFacade.startExternalActivity(mContext, url);
            		return true;
            	}
                //AkPlazaFacade.startExternalActivity(mContext, url);
            	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
            	i.setPackage("com.google.android.youtube");
            	mContext.startActivity(i);
                return true;
            }
            
            
            Uri uri = Uri.parse(url);
         // akplaza://app/mevent/?act=....
            if (mMobileEventFacade.handleMobileEvent(uri)) {
                return true;
            }
            
            if (url != null && !url.equals("about:blank"))
            {
                if( url.startsWith("http://") || url.startsWith("https://"))
                {
                    if (url.contains("http://market.android.com")            ||
                        url.contains("http://m.ahnlab.com/kr/site/download") ||
                        url.endsWith(".apk")                                   )
                    {
                        return url_scheme_intent( view, url );
                    }
//                    else
//                    {
//                        view.loadUrl( url );
//                        return false;
//                    }
                }
                else
                {
                    return url_scheme_intent( view, url );
                }
            }

            

            

            // 항상 마지막에 확인 할 것,
            // 처리 할 수 없는 스키마는 웹브라우저 처럼 항상 외부로 처리 넘김.
            if (url.startsWith(SCHEME_HTTP) || url.startsWith(SCHEME_HTTPS)) {
                return false;
            } else {
                return AkPlazaFacade.startExternalActivity(mContext, url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mWebViewListener != null) {
                mWebViewListener.onPageFinished(url);
            }

            Cookies cookies = getCookies(url);

            setLoginStatus(cookies);
            signinDevice(cookies);
            updateDeviceTokenUserid(cookies);

        }
    }; // WebViewClient

    private void fixBuiltInZoomControls(String url) {

        if (mEnabledZoom) {
            getSettings().setSupportZoom(false);
            getSettings().setBuiltInZoomControls(false);
            mEnabledZoom = false;
        }

        // 확대보기
        if (url.contains("ebook.do?act=viewLarge")) {
            getSettings().setSupportZoom(true);
            getSettings().setBuiltInZoomControls(true);
            mEnabledZoom = true;
        }
    }

    private void setLoginStatus(Cookies cookies) {
        Log.d(TAG, "setLoginStatus");
        String sessionId = cookies.getValue("JSESSIONID");
        String userId = cookies.getValue("userid");
        String logined = cookies.getValue("logined");

        if (logined.equals("true")) {
            SharedUtil.setSharedString(mContext, "login", "LOGIN", "Success");
        } else {
            SharedUtil.setSharedString(mContext, "login", "LOGIN", "");
            mIsNotUpdatDeviceTokenUserid = true;
        }

        SharedUtil.setSharedString(mContext, "login", "USERID", userId);
        SharedUtil.setSharedString(mContext, "login", "SESSIONID", sessionId);
        Log.d(TAG, "setLoginStatus finished..");
    }

    private void signinDevice(Cookies cookies) {
        String sdvc = cookies.getValue("sdvc");
        if (!"true".equals(sdvc)) {
            mAPI.signinDevice();
        }
    }

    private Cookies getCookies(String url) {
        // CookieSyncManager.getInstance().sync(); //@FIX 속도 저하 문제 있음.
        String cookie = mCookieMgr.getCookie(url);
        Cookies cookies = new Cookies(cookie);
        return cookies;
    }

    private void updateDeviceTokenUserid(Cookies cookies) {
        if (mIsNotUpdatDeviceTokenUserid && LoginManager.isLogin(cookies)) {
            boolean result = LoginManager.updateDeviceTokenUserid(mContext);
            if (result) {
                mIsNotUpdatDeviceTokenUserid = false;
            }
        }
    }

    private void startExternalMapApp(String url) {
        String address = "";

        if (url.indexOf("bc=01") > -1) {
            address = "서울시 구로구 구로5동 573+AK플라자(구로점)";
        } else if (url.indexOf("bc=02") > -1) {
            address = "수원시 팔달구 매산로 1가 18+AK플라자(수원점)";
        } else if (url.indexOf("bc=03") > -1) {
            address = "경기도 성남시 분당구 서현동 263+AK플라자(분당점)";
        } else if (url.indexOf("bc=04") > -1) {
            address = "경기도 평택시 평택동 185-568+AK플라자(평택점)";
        } else if (url.indexOf("bc=05") > -1) {
            address = "강원도 원주시 단계동 1123+(AK플라자 원주점)";
        }

        final String mapUrl = "http://maps.google.com/maps?q=" + address;

        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setIcon(R.drawable.logo_small).create();
        ab.setTitle(" ").create();
        ab.setMessage("맵으로 이동 하시겠습니까?").create();
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AkPlazaFacade.startExternalActivity(mContext, mapUrl);
            }
        }).create();
        ab.setNegativeButton("아니오", null).create();
        ab.show();
    }

    public interface WebViewListener {
        public void onProgressChanged(int progress);

        public void onPageStarted(String url);

        public void onPageFinished(String url);
    }
    
    
    private boolean url_scheme_intent( WebView view, String url )
    {
    	
    	WebViewActivity.mIsReloadable = false;

        //chrome 버젼 방식 : 2014.01 추가
        if ( url.startsWith( "intent" ) )
        {
            //ILK 용
            if( url.contains( "com.lotte.lottesmartpay" ) )
            {
                try{
                    mContext.startActivity( Intent.parseUri(url, Intent.URI_INTENT_SCHEME) );
                } catch ( URISyntaxException        e ) {
                    return false;
                } catch ( ActivityNotFoundException e ) {
                    return false;
                }
            }
            //ILK 용
            else if ( url.contains( "com.ahnlab.v3mobileplus" ) )
            {
                try {
                    view.getContext().startActivity(Intent.parseUri(url, 0));
                } catch ( URISyntaxException        e ) {
                    return false;
                } catch ( ActivityNotFoundException e ) {
                    return false;
                }
            }
            //폴라리스 용
            else
            {
                Intent intent = null;
                try {
                    intent = Intent.parseUri( url, Intent.URI_INTENT_SCHEME );
                } catch ( URISyntaxException ex ) {
                    return false;
                }
                // 앱설치 체크를 합니다.
                if ( activity.getPackageManager().resolveActivity( intent, 0 ) == null )
                {
                	
                    String packagename = intent.getPackage();
                    
                    if ( packagename != null )
                    {
                    	mContext.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( "market://search?q=pname:" + packagename ) ) );
                        
                        return true;
                    }
                }
                
                intent = new Intent( Intent.ACTION_VIEW, Uri.parse( intent.getDataString() ) );
                
                try{
                	mContext.startActivity( intent );
                }catch( ActivityNotFoundException e ) {
                    return false;
                }
            }
        }
        // 기존 방식
        else
        {
            /*
            if ( url.startsWith( "ispmobile" ) )
            {
                if( !new PackageState( this ).getPackageDownloadInstallState( "kvp.jjy.MispAndroid" ) )
                {
                    startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse( "market://details?id=kvp.jjy.MispAndroid320" ) ) );
                    
                    return true;
                }
            }
            else if ( url.startsWith( "paypin" ) )
            {
                if( !new PackageState( this ).getPackageDownloadInstallState( "com.skp.android.paypin" ) )
                {
                    if( !url_scheme_intent( "tstore://PRODUCT_VIEW/0000284061/0" ) )
                    {
                        url_scheme_intent( "market://details?id=com.skp.android.paypin&feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5za3AuYW5kcm9pZC5wYXlwaW4iXQ.k" );
                    }
                    
                    return true;
                }
            }
            */
            
            // 삼성과 같은 경우 어플이 없을 경우 마켓으로 이동 할수 있도록 넣은 샘플 입니다.
            // 실제 구현시 업체 구현 여부에 따라 삭제 처리 하시는것이 좋습니다.
            if ( url.startsWith( "mpocket.online.ansimclick" ) )
            {
                if( !new PackageState( activity ).getPackageDownloadInstallState( "kr.co.samsungcard.mpocket" ) )
                {
                    Toast.makeText(mContext, "어플을 설치 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                    
                    mContext.startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse( "market://details?id=kr.co.samsungcard.mpocket" ) ) );
                    return true;
                }
            }
            
            try
            {
                mContext.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
            }
            catch(Exception e)
            {
                // 어플이 설치 안되어 있을경우 오류 발생. 해당 부분은 업체에 맞게 구현
                Toast.makeText(mContext, "해당 어플을 설치해 주세요.", Toast.LENGTH_LONG).show();
//                return false;
            }
        }

        return true;
    }


    /*
    // 하나SK 카드 선택시 User가 선택한 기본 정보를 가지고 오기위해 사용
    private class KCPPayCardInfoBridge
    {
        public void getCardInfo( final String card_cd, final String quota )
        {
            handler.post( new Runnable() {
                public void run()
                {
                    Log.d( SampleApplication.m_strLogTag, "[PayDemoActivity] KCPPayCardInfoBridge=[" + card_cd + ", " + quota + "]" );

                    CARD_CD = card_cd;
                    QUOTA   = quota;

                    PackageState ps = new PackageState( PayDemoActivity.this );

                    if(!ps.getPackageDownloadInstallState( "com.skt.at" ))
                    {
                        alertToNext();
                    }
                }
            });
        }

        private void alertToNext()
        {
            AlertDialog.Builder  dlgBuilder = new AlertDialog.Builder( PayDemoActivity.this );
            AlertDialog          alertDlg;

            dlgBuilder.setMessage( "HANA SK 모듈이 설이 되어있지 않습니다.\n설치 하시겠습니까?" );
            dlgBuilder.setCancelable( false );
            dlgBuilder.setPositiveButton( "예",
                                          new DialogInterface.OnClickListener()
                                              {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which)
                                                  {
                                                      dialog.dismiss();

                                                      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://cert.hanaskcard.com/Ansim/HanaSKPay.apk" ) );

                                                      m_nStat = PROGRESS_STAT_IN;

                                                      startActivity( intent );
                                                  }
                                              }
                                          );
            dlgBuilder.setNegativeButton( "아니오",
                                          new DialogInterface.OnClickListener()
                                              {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which)
                                                  {
                                                      dialog.dismiss();
                                                  }
                                              }
                                          );

            alertDlg = dlgBuilder.create();
            alertDlg.show();
        }
    }
    */
    
    private class KCPPayPinReturn
    {
        public String getConfirm()
        {
            AKPlazaApplication myApp = (AKPlazaApplication) activity.getApplication();
            
            if( myApp.b_type )
            {
                myApp.b_type = false;
                
                return "true";
            }
            else
            {
                return "false";
            }
        }
    }
    
    private class KCPPayPinInfoBridge
    {
        public void getPaypinInfo(final String url)
        {
            handler.post( new Runnable() {
                public void run()
                {

                    PackageState ps = new PackageState( activity );

                    if(!ps.getPackageAllInstallState( "com.skp.android.paypin" ))
                    {
                        paypinConfim();
                    }
                    else
                    {
                        url_scheme_intent( null, url );
                    }
                }
            });
        }

        private void paypinConfim()
        {
            AlertDialog.Builder  dlgBuilder = new AlertDialog.Builder( activity );
            AlertDialog          alertDlg;

            dlgBuilder.setTitle( "확인" );
            dlgBuilder.setMessage( "PayPin 어플리케이션이 설치되어 있지 않습니다. \n설치를 눌러 진행 해 주십시요.\n취소를 누르면 결제가 취소 됩니다." );
            dlgBuilder.setCancelable( false );
            dlgBuilder.setPositiveButton( "설치",
                                          new DialogInterface.OnClickListener()
                                              {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which)
                                                  {
                                                      dialog.dismiss();
                                                      
                                                      if( 
                                                          //url_scheme_intent( "https://play.google.com/store/apps/details?id=com.skp.android.paypin&feature=nav_result#?t=W10." );
                                                          //url_scheme_intent( "market://details?id=com.skp.android.paypin&feature=nav_result#?t=W10." );
                                                          !url_scheme_intent( null, "tstore://PRODUCT_VIEW/0000284061/0" )
                                                      )
                                                      {
                                                          url_scheme_intent( null, "market://details?id=com.skp.android.paypin&feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5za3AuYW5kcm9pZC5wYXlwaW4iXQ.k" );
                                                      }
                                                  }
                                              }
                                          );
            dlgBuilder.setNegativeButton( "취소",
                                          new DialogInterface.OnClickListener()
                                              {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which)
                                                  {
                                                      dialog.dismiss();
                                                      
                                                      Toast.makeText(mContext, "결제를 취소 하셨습니다." , Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          );

            alertDlg = dlgBuilder.create();
            alertDlg.show();    
        }
    }
    
    private class KCPPayBridge
    {
        public void launchMISP( final String arg )
        {
            handler.post( new Runnable() {
                public void run()
                {
                    String  strUrl;
                    String  argUrl;

                    PackageState ps = new PackageState( activity );

                    argUrl = arg;

                    if(!arg.equals("Install"))
                    {
                        if(!ps.getPackageDownloadInstallState( "kvp.jjy.MispAndroid" ))
                        {
                            argUrl = "Install";
                        }
                    }

                    strUrl = ( argUrl.equals( "Install" ) == true )
                                ? "market://details?id=kvp.jjy.MispAndroid320" //"http://mobile.vpay.co.kr/jsp/MISP/andown.jsp"
                                : argUrl;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( strUrl ) );

                    m_nStat = PROGRESS_STAT_IN;

                    mContext.startActivity( intent );
                }
            });
        }
    }

}
