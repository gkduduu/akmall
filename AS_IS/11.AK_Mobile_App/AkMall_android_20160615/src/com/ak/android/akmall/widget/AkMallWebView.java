
package com.ak.android.akmall.widget;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.ak.android.akmall.BuildConfig;
import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.AkMallIntro;
import com.ak.android.akmall.common.CommonDialog;
import com.ak.android.akmall.gcm.GcmManager;
import com.ak.android.akmall.util.AkMallUriHandler;
import com.ak.android.akmall.util.OutsideUriHandler;
import com.ak.android.akmall.util.OutsideUriHandler.HandleResult;
//import android.webkit.CacheManager;//p65458 20150716 4.2.2 버전 이후로 기본 동작이 적용되어 해당 라이브러리가 삭제 되었음

public class AkMallWebView extends WebView {
	
	private OnScrollChangedCallback mOnScrollChangedCallback;
	
	//test 박보람
	private String mInitUserAgent;
	public static final String INTENT_PROTOCOL_START = "intent:";
    public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
    public static final String INTENT_PROTOCOL_END = ";end";
    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";
    public static final String GOOGLE_PLAY_STORE_PACKAGE = ";package=";
    public static final String INTENT_PROTOCOL_SCHEME = "scheme=";
    //test 박보람

    public static final String TAG = "AkMallWebView";
    public static final boolean DBG = BuildConfig.DEBUG;

    private static final String PAYURL = "i://returnUrl=";

    private static final String SCHEME_HTTP = "http:";
    private static final String SCHEME_HTTPS = "https:";
    private static final String ABOUT_BLANK = "about:blank";

    private ImageView image;
    private Context mContext;
    private WebViewListener mWebViewListener;
    private boolean mEnabledZoom = false;

    private WebSettings mWebSettings;
    private String mOrigUserAgent;
    private String mExtraUserAgent;
    private boolean mAddedExtraHeader;
    
    public static Activity activity = null;
    
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    public AkMallWebView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public AkMallWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public AkMallWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setup();
    }

    public void setWebViewListener(WebViewListener listener) {
        mWebViewListener = listener;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setup() {

    	mWebSettings = getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setSavePassword(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebSettings.setSupportMultipleWindows(false);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO)
        	mWebSettings.setTextSize(mWebSettings.getTextSize().NORMAL);

        addExtraHeader(mWebSettings, AkMallFacade.getAppInfoHttpHeaderMap(mContext));
        checkAndDisableDiskCache();
        setVerticalScrollbarOverlay(true);
        setOnTouchListener(mOnTouchListener);

        //setWebChromeClient(mDefaultWebChromeClient);
        setWebViewClient(mDefaultWebViewClient);
    }

    /**
     * trick.. add extra http header
     * 
     * @param setting
     * @param extraHeaderString HTTP 규격에 맞는 헤더들..
     */
    private void addExtraHeader(WebSettings setting, Map<String, String> extraHeaderMap) {

    	 mOrigUserAgent = getResolvedUserAgentString(setting);
         mExtraUserAgent = mOrigUserAgent;
         for (String key : extraHeaderMap.keySet()) {
             mExtraUserAgent += "\r\n" + key + ": " + extraHeaderMap.get(key);
         }
         setting.setUserAgentString(mExtraUserAgent);
         mAddedExtraHeader = true;
    }

    private void checkAndDisableDiskCache() {

        // IO 성능이 떨어지는 갤럭시 S, 갤럭시 K, 갤럭시 U 모델에서 디스크 캐시 끔.
        // SHW-M110S, SHW-M130L, SHW-M130K
    	if (Build.MODEL.equals("SHW-M110S") || Build.MODEL.startsWith("SHW-M130")) {
            try {
            	//p65458 20150716 4.2.2 버전 이후로 기본 동작이 적용되어 해당 라이브러리가 삭제 되었음
            	/*
                Method m = CacheManager.class.getDeclaredMethod("setCacheDisabled", boolean.class);
                m.setAccessible(true);
                m.invoke(null, true);
                */
              //p65458 20150716 4.2.2 버전 이후로 기본 동작이 적용되어 해당 라이브러리가 삭제 되었음
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
        	CommonDialog commonDialog = new CommonDialog(mContext, true, 
        			message, 
        			mContext.getString(android.R.string.ok), 
        			mContext.getString(android.R.string.no));
    		commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
    			@Override
    			public void onDismiss(DialogInterface dialog) {
    				// TODO Auto-generated method stub
    				CommonDialog commonDialog = (CommonDialog) dialog;
    				if(commonDialog.IsOk()) {
    					result.confirm();
    				}
    			}
    		});
    		commonDialog.show(); 

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
    	public final static String INTENT_PROTOCOL_START_KAKAOLINK = "kakaolink:";
    	public final static String INTENT_PROTOCOL_START_KAKAOSTORY = "storylink:";
    	public final static String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

    	@Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, false);
        }

    	@Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            if (errorCode != WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
                loadUrl("file:///android_asset/networkerr.html");
            }
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

            if (url.startsWith(INTENT_PROTOCOL_START_KAKAOSTORY)){
            	try {
            	
            		Uri uri = Uri.parse(url);
            		Intent intent = new Intent(Intent.ACTION_SEND, uri);
            		activity.startActivity(intent);
            	} catch (ActivityNotFoundException e) {
            		Intent intent = new Intent(Intent.ACTION_VIEW);
            		intent.setData(Uri.parse(GOOGLE_PLAY_STORE_PREFIX + "com.kakao.story"));
            		activity.startActivity(intent);
            	}
            	return true;
            }
            if (url.startsWith(INTENT_PROTOCOL_START_KAKAOLINK)){
            	try {
            	
            		Uri uri = Uri.parse(url);
            		Intent intent = new Intent(Intent.ACTION_SEND, uri);
            		activity.startActivity(intent);
            	} catch (ActivityNotFoundException e) {
            		Intent intent = new Intent(Intent.ACTION_VIEW);
            		intent.setData(Uri.parse(GOOGLE_PLAY_STORE_PREFIX + "com.kakao.talk"));
            		activity.startActivity(intent);
            	}
            	return true;
            }
            
            // akmall:// handle..
            if (AkMallUriHandler.handleInternalUri(mContext, url)) {
                return true;
            }

            // 결제 처리
            HandleResult handleResult = OutsideUriHandler.handlePaymentUri(mContext, url);
            if (handleResult.isSupport) {
                return handleResult.isDone;
            }

            handleResult = OutsideUriHandler.handleExternalUri(mContext, url);
            if (handleResult.isSupport) {
                return handleResult.isDone;
            }

            // ISP 설치 마켓 경로
            if (url.contains("https://market.android.com/details?id=kvp.jjy.MispAndroid320")) {
                return AkMallFacade.startExternalActivity(mContext,
                        "market://details?id=kvp.jjy.MispAndroid320");
            }
            
            // 기타 다른 앱으로 처리 넘김..
            if (url.startsWith("http://market.android.com")
                    || url.startsWith("https://market.android.com") || url.endsWith(".apk")
                    || url.contains("http://m.ahnlab.com/kr/site/download")
                    || "http://www.akmall.com/index.jsp?in_mobile=Y".equals(url) // PC버전
                                                                                 // AK몰
            ) {
                return AkMallFacade.startExternalActivity(mContext, url);
            }

            if (url.startsWith(PAYURL)) {
                url = url.substring(PAYURL.length());
                loadUrl(url);
                return true;
            }
            
            // 회원가입
            if (url.contains("/signup/?site=makmall")) {
                int versionCode = AkMallFacade.getVersionCode(mContext);
                url += mContext.getString(R.string.param_signup, versionCode);
                return AkMallFacade.startExternalActivity(mContext, url);
            } 
            
            if (url.indexOf("facebook.com") > -1) {

				loadUrl(url);
				
        		return true;
            }
            
            if (url.indexOf("clip") > -1) {
            	ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE); 
            	String text = url.replace("clip://?body=","");
            	ClipData clip = ClipData.newPlainText("AKMALL", text);
            	clipboard.setPrimaryClip(clip);
            	Toast.makeText(mContext, "URL이 복사되었습니다.원하는 곳에 붙여넣기 해주세요.",Toast.LENGTH_LONG).show();
            	return true;
            }
            
            if(url.indexOf("BarCode") > -1){
            	AkMallFacade.startBarCodeScanActivityForResult(mContext);
            	return true;
            }
            
            if(url.indexOf("QRCode") > -1){
            	AkMallFacade.startQrCodeScanActivityForResult(mContext);
            	return true;
            }
            
            if(url.indexOf("newtab") > -1){
            	String path = url.replace("newtab","http");
            	return AkMallFacade.startExternalActivity(mContext, path);
            }


            Uri uri = Uri.parse(url);

            // User-agent 설정을 통해 편법으로 Http header 값을 추가하는 것을 on/off 함.
            changeUserAgentIfNeed(uri);
            
            if (uri.isHierarchical()) {
                if (url.contains("/Login.do")
                        && TextUtils.isEmpty(uri.getQueryParameter("token"))) {
                    // 로그인 페이지로 이동시 푸시 토큰 정보를 파라미터에 추가 한다.
                    // 자동 로그인 설정시 미작성 상품평 알림을 받기 위함이다.
                	Log.d(TAG, "token: " + GcmManager.getRegistrationId(getContext()));
                    loadUrl(uri + "&token=" + GcmManager.getRegistrationId(getContext()));
                    return true;
                } else if ("insertGoodsComment".equals(uri.getQueryParameter("act"))
                        && AkMallAPI.isLogin(mContext)) {
                    // 상품평 쓰기
                    AkMallFacade.startImageReviewActivity(mContext, url);
                    return true;
                }
            }

            if (url.startsWith(SCHEME_HTTP) || url.startsWith(SCHEME_HTTPS)
                    || ABOUT_BLANK.equals(url)) {
                return false;
            } else {
                return AkMallFacade.startExternalActivity(mContext, url);
            }
        }

		@Override
        public void onPageFinished(WebView view, String url) {
        	
            if (mWebViewListener != null) {
                mWebViewListener.onPageFinished(url);
            }
        }
    }; // WebViewClient
    
    
    private void fixBuiltInZoomControls(String url) {

        if (mEnabledZoom) {
            getSettings().setSupportZoom(false);
            getSettings().setBuiltInZoomControls(false);
            mEnabledZoom = false;
        }

        // 확대보기
        //if (url.contains("goods.do?act=viewEnlargeImage")) {//p65458 20150729 zoom view fixed block 
            getSettings().setSupportZoom(true);
            getSettings().setBuiltInZoomControls(true);
            getSettings().setDisplayZoomControls(false);//p65458 20150727 zoom view fixed
            mEnabledZoom = true;
        //}//p65458 20150729 zoom view fixed block
    }

    /**
     * addExtraHeader() 방식으로 Http header를 추가하면 facebook에 접속시 에러 발생함 facebook 접근시
     * 오리지널 user-agent로 변경
     *
     * @param uri 접근할 페이지 주소
     */
    private void changeUserAgentIfNeed(Uri uri) {
        final String host = uri.getHost();
        if (host != null && host.indexOf("facebook.com") != -1) {
            //mWebSettings.setUserAgentString(mOrigUserAgent);
            //mAddedExtraHeader = false;
            return;
        }else if(uri.getPath().contains("goods/GoodsDetail.do") || uri.getPath().contains("/event/EventDetail.do")
        		|| uri.getPath().contains("/special/PowerDealDetail.do") || host.indexOf("storylink") != -1){
        	//mWebSettings.setUserAgentString(mInitUserAgent);
            //mAddedExtraHeader = false;
            return;
        }
        
        if (!mAddedExtraHeader) {
            mWebSettings.setUserAgentString(mExtraUserAgent);
            mAddedExtraHeader = true;
        }
    }

    public interface WebViewListener {
        public void onProgressChanged(int progress);

        public void onPageStarted(String url);

        public void onPageFinished(String url);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

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
    };
    
    /**
     * WebView는 빌드 타겟 버전에 따라 엔진을 Chromium 혹은 WebKit 으로 서로 다르게 이용한다.<br>
     * 하지만 Chromium 엔진이 포함된 안드로이드 버전(예를 들면 Android 4.4)에서는<br>
     * 엔진 종류에 상관 없이 User-agent에 "Chrome/30.0.0.0" 형태의 문자가 포함된다.<br>
     * 엔진에 따라 Uri를 이용한 앱 호출 동작이 다르기 때문에<br>
     * 결제 페이지에서는 User-agent를 참조하여 다르게 동작하게 되어 있다.<br>
     * AkMall 앱은 빌드 타겟 버전이 Android 2.2인 관계로 WebView의 엔진으로 WebKit이 사용된다.<br>
     * 하지만 User-agent에는 Chrome이 포함되어 문제가 발생하므로 해당 문자를 삭제 처리한다.
     *
     * @param setting
     * @return 올바르게 동작 할 수 있는 User-Agent String
     */
    private String getResolvedUserAgentString(WebSettings setting) {
        String userAgent = setting.getUserAgentString();
        
        //mInitUserAgent = userAgent;
        
        if (getContext().getApplicationInfo().targetSdkVersion < 19) { // Android-19 == Android 4.4.2
            //userAgent = userAgent.replaceAll("(\\s)Chrome/(\\S*)\\s", " ");
        }
        return userAgent;
    }
    
    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback()
    {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback)
    {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback
    {
        public void onScroll(int l, int t);
    }
    
}
