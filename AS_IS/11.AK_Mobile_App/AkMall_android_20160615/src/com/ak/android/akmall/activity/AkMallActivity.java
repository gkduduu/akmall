
package com.ak.android.akmall.activity;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ak.android.akmall.BuildConfig;
import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.AkMallIntro;
import com.ak.android.akmall.common.CommonDialog;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.common.FinishCallback;
import com.ak.android.akmall.common.PreferenceFacade;
import com.ak.android.akmall.gcm.GcmManager;
import com.ak.android.akmall.my.NotiBoxView;
import com.ak.android.akmall.my.NotiSetSoundView;
import com.ak.android.akmall.my.NotiSetView;
import com.ak.android.akmall.my.NotiTimeSetView;
import com.ak.android.akmall.qrcode.FinishListener;
import com.ak.android.akmall.qrcode.ZxingIntents;
import com.ak.android.akmall.util.AkMallUriHandler;
import com.ak.android.akmall.util.NetworkUtil;
import com.ak.android.akmall.util.SharedUtil;
import com.ak.android.akmall.util.UriProvider;
import com.ak.android.akmall.widget.AkMallWebView;
import com.ak.android.akmall.widget.AkMallWebView.OnScrollChangedCallback;
import com.ak.android.akmall.widget.AkMallWebView.WebViewListener;
import com.ak.android.akmall.widget.NavigationTabView;
import com.ak.android.akmall.widget.ResizeAnimation;
import com.facebook.FacebookSdk;
import com.igaworks.IgawCommon;
import com.igaworks.commerce.IgawCommerce;
import com.igaworks.liveops.IgawLiveOps;
import com.igaworks.liveops.pushservice.RegistrationIdEventListener;
import com.mtracker.mea.sdk.MTrackerManager;//p65458 20150716 mtracker 연동 add
import com.mtracker.mea.sdk.MTrackerWebApp;


@SuppressWarnings("unused")
public class AkMallActivity extends Activity {

    public static final String TAG = "AkMallActivity";
    public static final boolean DBG = BuildConfig.DEBUG & true;

    /**
     * 이벤트 처리 액션으로 Notification으로 부터 발생한 인텐트를 가지고 알림 보관함으로 이동함.
     */
    public static final String ACTION_VIEW_EVENT = "com.ak.android.akmall.intent.action.VIEW_EVENT";

    /**
     * 웹뷰를 리로드 시킴.
     */
    public static final String ACTION_RELOAD = "com.ak.android.akmall.intent.action.RELOAD";

    /**
     * true이면 webview history를 삭제함. false이면 아무동작 하지 않음.
     */
    public static final String EXTRA_CLEAR_HISTORY = "com.ak.android.akmall.intent.extra.CLEAR_HISTORY";
    
    public static boolean tabVisibleCtr = true;
    private AkMallIntro intro;
    private ImageView image;
    private int dTime = 10000;
    private ViewStub mIntroView;
    private ViewStub mGuideView;//p65458 20150730 guide view add
    private static AkMallWebView mWebView;
    
    private static NotiBoxView notiBoxView;
    private static NotiSetView notiSetView;
    private static NotiTimeSetView notiTimeSetView;
    private static NotiSetSoundView notiSetSoundView;
    
    private static LinearLayout akContentsLayout;
    
    private static LinearLayout toolbar;
    
    
    private NavigationTabView mNaviTab;
    //로딩상황 프로그레스
//    private ProgressBar mCenterProgressBar;
    private ProgressBar mBarProgressBar;

    private UriProvider mUriProvider;
    private int mUriCode;

    private boolean mIsFirstLoad = true;

    private static DeviceInfoUpdateTask mDeviceInfoUpdateTask;
    private static boolean mIsRegisteredServer;
    private static boolean mIsUpdatedDevice;
    
    public static AkMallActivity mainActivity;
    public static String nowView = "";
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    
    //p65458 20150907 루팅 체크 관련 기능 추가 
    public static final String ROOT_PATH = Environment.
            getExternalStorageDirectory() + "";
//    public static final String ROOTING_PATH_1 = "/system/bin/su";
//    public static final String ROOTING_PATH_2 = "/system/xbin/su";
//    public static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
//    public static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";
    
    public static final String APP_INTRO_IMAGE = Const.URL_LIB + "act=appIntro";
    
	public static final String ROOTING_PATH_1 = "/sbin/su";
	
	public static final String ROOTING_PATH_2 = "/system/bin/su";
	
	public static final String ROOTING_PATH_3 = "/system/sbin/su";
	
	public static final String ROOTING_PATH_4 = "/system/xbin/su";
	
	public static final String ROOTING_PATH_5 = "/system/xbin/sudo";
	
	public static final String ROOTING_PATH_6 = "/system/app/superuser.apk";
	
	public static final String ROOTING_PATH_7 = "/system/app/UnRoot.apk";
	
	public static final String ROOTING_PATH_8 = "/system/app/Nakup.apk";
	
	public static final String ROOTING_PATH_9 = "/data/data/com.noshufou.android.su";
	
	public static final String ROOTING_PATH_10 = "/data/data/com/ajantech.app/UnRoot";
	
	public static final String ROOTING_PATH_11 = "/data/app/com.noshufou.android.su-2.apk";
	
	public static final String ROOTING_PATH_12 = "eu.chainfire.supersu";
	
	public static final String ROOTING_PATH_13 = "com.noshufou.android.su";
	
	public static final String ROOTING_PATH_14 = "com.koushikdutta.superuser";
	
	public static final String ROOTING_PATH_15 = "eu.chainfire.supersu.pro";
	
	public static final String ROOTING_PATH_16 = "com.marutian.quickunroot";
	
	public static final String ROOTING_PATH_17 = "com.tegrak.lagfix";
     
    public String[] RootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1 ,
            ROOT_PATH + ROOTING_PATH_2 , 
            ROOT_PATH + ROOTING_PATH_3 , 
            ROOT_PATH + ROOTING_PATH_4 ,
            ROOT_PATH + ROOTING_PATH_5 ,
            ROOT_PATH + ROOTING_PATH_6 , 
            ROOT_PATH + ROOTING_PATH_7 , 
            ROOT_PATH + ROOTING_PATH_8 ,
            ROOT_PATH + ROOTING_PATH_9 , 
            ROOT_PATH + ROOTING_PATH_9 , 
            ROOT_PATH + ROOTING_PATH_10 ,
            ROOT_PATH + ROOTING_PATH_11 , 
            ROOT_PATH + ROOTING_PATH_12 , 
            ROOT_PATH + ROOTING_PATH_13 ,
            ROOT_PATH + ROOTING_PATH_14 , 
            ROOT_PATH + ROOTING_PATH_15 , 
            ROOT_PATH + ROOTING_PATH_16 ,
            ROOT_PATH + ROOTING_PATH_17
            
    };
    //p65458 20150907 루팅 체크 관련 기능 추가 
    
    String deepLinkURL = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());//p65458 add 20151012
        deepLinkURL = MTrackerManager.deeplink(getIntent(), this, Const.URL_BASE);
      //defaultURL : Deep Link URL이 없을 경우 기본적으로 이동할 URL
        mainActivity = this;
        mWebView = new AkMallWebView(mainActivity.getApplicationContext());
        AkMallWebView.activity = this;
        
        if (DBG) {
            Log.d(TAG, "onCreate");
        }
        
        
      //p65458 20150907 루팅 체크 관련 기능 추가 
        
        boolean isRootingFlag = false;
        
        try {
            Runtime.getRuntime().exec("su");
            isRootingFlag = true;
        } catch ( Exception e) {
            // Exception 나면 루팅 false;
            isRootingFlag = false;
        }
         
        if(!isRootingFlag){
            isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
        }
         
        Log.d("test", "isRootingFlag = " + isRootingFlag);
        
        if(isRootingFlag == true){
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle(getString(R.string.app_name));
    		builder.setMessage(getString(R.string.msg_rooting_popup));
    		builder.setPositiveButton(android.R.string.ok, new FinishListener(this));
    		builder.setOnCancelListener(new FinishListener(this));
    		builder.show();
        }
      //p65458 20150907 루팅 체크 관련 기능 추가 
        
        IgawCommon.startApplication(AkMallActivity.this);//p65458 20150716 IgawCommon add sdk
        
        //Added by Eddy 2015-12-07
		//반드시 유저식별값을 설정해야 합니다.
		String deviceID = getDeviceID();
		IgawCommon.setUserId(deviceID.replace("-", ""));
		
		// 라이브옵스 SDK를 초기화 합니다.
//		IgawLiveOps.initialize(AkMallActivity.this); 
		IgawLiveOps.initialize(AkMallActivity.this, Const.GCM_SENDER_ID);
		
		IgawLiveOps.setRegistrationIdEventListener(new RegistrationIdEventListener() {
            @Override
            public void onReceiveRegistrationId(String regId) {
                Log.d("DEBUG", "onReceiveRegistrationId :: " + regId);
            }
		});

        mIsRegisteredServer = GcmManager.isRegisteredServer(this);
        mIsUpdatedDevice = false;
        
        /*
         * 애드브릭스 딥링크 신규실행이 잡히지 않는 문제에 대한 API추가
         */
        IgawCommon.registerReferrer(AkMallActivity.this);
        
        initialView();
        setup();
        
        //onNewIntent 리스너로 intent를 전달합니다.
        onNewIntent(getIntent());
        //end of Added by Eddy 2015-12-07
/*
    	//p65458 20150729 add 현재 앱이 foreground 상태일경우는 다이얼로그를 보여주고 아니면 토스트로 보여준
  	  ActivityManager activityapp = (ActivityManager)mainActivity.getSystemService(mainActivity.ACTIVITY_SERVICE);
  	  
  	  List<RunningAppProcessInfo> list = (List<RunningAppProcessInfo>)activityapp.getRunningAppProcesses();

  	  for(int i = 0 ; i < list.size() ; i++) {
  	      RunningAppProcessInfo info = list.get(i);
  	      if ( info.processName.equals("com.ak.android.akmall") && info.importance == info.IMPORTANCE_FOREGROUND ){  
  	          Intent alertIntent = new Intent(mainActivity, AlarmDialogActivity.class);
  	          alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_PUSH_ID, "1111");
  	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_MESSAGE, "IMPORTANCE_FOREGROUND");
  	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_NOTIFICATION_ID, "22222");
  	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_IMGURL, "http://www.androidpub.com/files/attach/images/41913/10fb6855fd07a3288f4700955bdb1993.gif");
  	        mainActivity.startActivity(alertIntent);
  	        return;
  	      }else{
  	          Toast.makeText(mainActivity, "IMPORTANCE_BACKROUND", Toast.LENGTH_SHORT)
  	          .show();
  	          return;
  	      }
  	  }
  	//p65458 20150729 add 현재 앱이 foreground 상태일경우는 다이얼로그를 보여주고 아니면 토스트로 보여준
*/
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                mWebView.reload();
                break;

            case R.id.sign:
                if (AkMallAPI.isLogin(this)) {
                	String url = Const.URL_BASE + getString(R.string.uri_loginOut);
                    mWebView.loadUrl(url);
                } else {
                	String url = Const.URL_BASE + getString(R.string.uri_login);
                    mWebView.loadUrl(url);
                }
                break;

            case R.id.settings:
                String url = Const.URL_BASE + getString(R.string.uri_personal_setting);
                mWebView.loadUrl(url);
                break;
                
            case R.id.finish:
                showFinishDialog();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem signMenu = menu.findItem(R.id.sign);
        if (AkMallAPI.isLogin(this)) {
            signMenu.setTitle(R.string.logout);
        } else {
            signMenu.setTitle(R.string.login);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.akmall, menu);
        return true;
    }

    @SuppressWarnings("static-access")
	private void initialView() {

        setContentView(R.layout.akmall);
        
        this.mGuideView = (ViewStub) findViewById(R.id.stubGuide);//p65458 20150730 guide view add
        this.mIntroView = (ViewStub) findViewById(R.id.stubIntro);
//        this.mCenterProgressBar = (ProgressBar) findViewById(R.id.centerProgressBar);
        this.mBarProgressBar = (ProgressBar) findViewById(R.id.barProgressBar);
        this.mWebView = (AkMallWebView) findViewById(R.id.akMallWebview);
        this.mNaviTab = (NavigationTabView) findViewById(R.id.bottomNaviTab);
        this.mWebView.setWebViewListener(this.mWebViewListener);
        this.mWebView.setHorizontalScrollBarEnabled(false);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        
                
        WebSettings settings = this.mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);       
        
        //facebook DPA for Hybrid
        this.mWebView.addJavascriptInterface(new MTrackerWebApp(this), "MTrackerWebApp");

        this.mWebView.setWebChromeClient(new WebChromeClient()
        {
        	//p65458 20150727 jellybean input type='file' tag wrong action ->block
//             @SuppressWarnings("unused")
//             public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                   mUploadMessage = uploadMsg;
//                   Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                   i.addCategory(Intent.CATEGORY_OPENABLE);
//                   i.setType("*/*");
//                   startActivityForResult(
//                           Intent.createChooser(i, "사진을 선택하세요"),
//                           FILECHOOSER_RESULTCODE);
//             }
           //p65458 20150727 jellybean input type='file' tag wrong action ->block

           //p65458 20150727 jellybean input type='file' tag wrong action ->fixed

           		    // For Android < 3.0
           		    public void openFileChooser( ValueCallback<Uri> uploadMsg ){
           		        openFileChooser( uploadMsg, "" );
           		    }
           		    // For Android 3.0+
           		    public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType ){  
           		    	
           		        mUploadMessage = uploadMsg;  
           		        Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
           		        i.addCategory(Intent.CATEGORY_OPENABLE); 
           		        
           		        //i.setType("image/*"); 
           		        i.setType("*/*");  
                        startActivityForResult(
                                Intent.createChooser(i, "사진을 선택하세요"),
                                FILECHOOSER_RESULTCODE);  
           		    }
           		    // For Android 4.1+
           		    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
           		        
           		    	openFileChooser( uploadMsg, "" );
           		    }

           	//p65458 20150727 jellybean input type='file' tag wrong action ->fixed
             
             @Override
             public boolean onJsAlert(WebView view, String url, String message,
                     final android.webkit.JsResult result) {
             	
             	CommonDialog commonDialog = new CommonDialog(AkMallActivity.this, true, 
             			message, 
             			AkMallActivity.this.getString(android.R.string.ok), 
             			AkMallActivity.this.getString(android.R.string.no));
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
             //p65458 20150803 add confirm alert 사용시 해당 코드 해제
             /*
             @Override
             public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result)
             {

	        	  new AlertDialog.Builder(AkMallActivity.this)
	                    .setTitle("AK Mall")
	                    .setMessage(message)
	                    .setPositiveButton(android.R.string.ok,
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(
	                                        DialogInterface dialog,
	                                        int which) {
	                                 result.confirm();
	                                }})
	                    .setNegativeButton(android.R.string.cancel,
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(
	                                        DialogInterface dialog,
	                                        int which) {
	                                 result.cancel();
	                                }})
	                    .show();

                 return true;
             };
             */
             //p65458 20150803 add confirm alert 사용시 해당 코드 해제

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
         });
        
        this.akContentsLayout = (LinearLayout) findViewById(R.id.akContentsLayout);
        this.akContentsLayout.setVisibility(View.GONE);
        
        this.toolbar = (LinearLayout)findViewById(R.id.toolbar);
        
        this.mWebView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
        	int oldScrollY;
			
			@Override
			public void onScroll(int l, int scrollY) {
				// TODO Auto-generated method stub
				int scrollDelta = scrollY - oldScrollY;
			    oldScrollY = scrollY;
			    
//			    float currentYTranslation = -toolbar.getTranslationY();
//			    float targetYTranslation = Math.min(Math.max(currentYTranslation + scrollDelta, 0), toolbar.getHeight());
			    if(tabVisibleCtr){
				    if(scrollDelta > 0) {
				    	expand();
				    } else {
				    	contract();
				    }
	//			    Log.d(TAG,"We Scrolled etc... : " + scrollDelta + " : " + currentYTranslation);
			    }
			}
		});
    }
    
    boolean barHidden;
    
    
    private void expand() {
    	if(barHidden)
    		return;
    	
    	barHidden = true;
    				
    	toolBarHide(true);
	}
    
    private void contract() {
    	if(!barHidden)
    		return;
    	
    	barHidden = false;
    	
    	toolBarHide(false);
	}
    
    private void toolBarHide(boolean flag) {
    	if(flag) {
	    	Animation animation = new TranslateAnimation(0,0,0,toolbar.getHeight());
	    	
	    	animation.setDuration(400);
	    	animation.setFillAfter(true);
	    	
	    	animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					toolbar.setVisibility(View.GONE);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					toolbar.getChildAt(0).setVisibility(View.GONE);
					toolbar.getChildAt(1).setVisibility(View.GONE);
				}
			});
	    	
            toolbar.startAnimation(animation);
            
    	} else {

    		Animation animation = new TranslateAnimation(0,0,toolbar.getHeight(),0);
    		
    		animation.setDuration(400);
    		animation.setFillAfter(true);
    		
    		animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					toolbar.getChildAt(0).setVisibility(View.VISIBLE);
					toolbar.getChildAt(1).setVisibility(View.VISIBLE);
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					toolbar.setVisibility(View.VISIBLE);
				}
			});
			toolbar.startAnimation(animation);
            
    	}
    	
	}


    private void setup() {
    	
    	showIntro();
    	
    	hiddenIntro();
    	int networkStatus = NetworkUtil.getStatus(this);
    	if(networkStatus != NetworkUtil.NETWORK_NONE){
	    	try{
	    		intro = new AkMallIntro();
	        	Map<String, String> map = intro.execute().get();

	            if(map != null){
		        	String useYn = map.get("use_yn");
		        	String link = map.get("link");
		        	dTime = (Integer.valueOf(map.get("dtime")) * 1000) + 1000;
		        	
		        	hiddenIntro();
		        	
		        	if(useYn.equals("Y")){
		        		image = (ImageView)findViewById(R.id.image);
		        		image.setImageBitmap(AkMallIntro.setIntro(map));
		        	}
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	intro.cancel(true);
	        }
    	}
    	
        // 네트워크 자원에 접근하지 않는 로컬 준비 작업들...
        mUriProvider = UriProvider.getInstance(this);
        CookieSyncManager.createInstance(this);
        SharedUtil.clearAllSetting(this, "login", 0);
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeExpiredCookie();

        // 사용자 확인후 네트워크 자원 접근하는 작업들..
        if (mIsFirstLoad) {
            confirmState();
        } else {
            initialPost();
        }
        
    }
    
//    @SuppressWarnings("deprecation")
    private void initialPost() {
        // auto login
        AkMallAPI.doAutoLoginInBackground(this, new FinishCallback() {

            @Override
            public void onFinish(boolean isSuccess) {
                Log.i(TAG, "auto login success: " + isSuccess);
                //hiddenIntro();
                handleIntent();
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();

        // load data, default action... (null, action.VIEW)
        loadData(intent);

        // handle action
        if (ACTION_VIEW_EVENT.equals(action)) {
        	String pushId = intent.getStringExtra("PID");
        	showNotiBoxView(pushId);
        }
        else if (ACTION_RELOAD.equals(action)) {
            mWebView.reload();
        }
    }

    private void loadData(Intent intent) {

        boolean isClearHistory = intent.getBooleanExtra(EXTRA_CLEAR_HISTORY, false);
        if (isClearHistory) {
            mWebView.clearHistory();
        }
        //if(deepLinkURL != null){
        if (!deepLinkURL.equals(Const.URL_BASE) && deepLinkURL != null) {
        	Log.d("Test", "1==>URL: " + deepLinkURL);
        	//11-23 16:03:04.561: D/Test(9086): 1==>URL: eturnUrl=http%3A%2F%2Fm.akmall.com%3FisAkApp%3DY
        	if (deepLinkURL.indexOf("eturnUrl=") >= 0)
        	{
        		String tmpURL = deepLinkURL.substring(deepLinkURL.indexOf("eturnUrl=")+9);
        		Log.d("Test", "==>decode URL: " + tmpURL);
        		
        		try
				{
					deepLinkURL = URLDecoder.decode(tmpURL, "utf-8");
				}
				catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
        	}
        	
        	mWebView.loadUrl(deepLinkURL);
        	deepLinkURL = Const.URL_BASE;
        }else{
	        Uri data = intent.getData();
	        Log.d("Test", "=======>" + data);	        
	
	        if (data != null) {
	            if (!AkMallUriHandler.handleExternalUri(data, this)) {
	            	//mtracker://m.akmall.com/main?randing_url=http://m.akmall.com/goods/GoodsDetail.do?goods_id=73017216&ct=a83616ac-8420-11e5-96f2-e41f13ed7e3a
	            	String tmpURL = data.toString();
	            	String lastURL = "";
	            	
	            	if (tmpURL.startsWith("mtracker"))
	            	{
	            		lastURL = tmpURL.substring(tmpURL.indexOf("http"));
	            	}
	            	else
	            	{
	            		lastURL = tmpURL;
	            	}
	            	
	            	Log.d("Test", "2==>URL: " + lastURL);

	                //mWebView.loadUrl(data.toString());
	            	mWebView.loadUrl(lastURL);
	            }
	        } else if (mIsFirstLoad) {
	            mWebView.loadUrl(mUriProvider.getUri(UriProvider.URI_MAIN).toString());
	        }
	        else
	        	mWebView.loadUrl(Const.URL_BASE);
        }
    }

    @Override
    protected void onResume() {
        if (DBG) {
            Log.d(TAG, "onResume");
        }
        MTrackerManager.startActivityAnalyze(this);//p65458 20150716 mtracker 연동 add
        MTrackerManager.startActivityAnalyze(this, "AIzaSyCVzqZfdndlsivLh83zoe2Uol9MvOwtl4I");//p65458 20150720 mtracker 연동 add
        IgawCommon.startSession(AkMallActivity.this);//p65458 20150716 IgawCommon add sdk
        CookieSyncManager.getInstance().startSync();
        super.onResume();
        
        //Added by Eddy 2015-12-07
		IgawCommon.startSession(this);		
		IgawLiveOps.resume(AkMallActivity.this);
		//end of Added by Eddy 2015-12-07
		mWebView.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (DBG) {
            Log.d(TAG, "onNewIntent : " + intent);
        }
        deepLinkURL = MTrackerManager.deeplink(getIntent(), this, Const.URL_BASE);
        
        super.onNewIntent(intent);
        
        /* 1. 앱 스키마 타입(myApp://deepLinkAction) 딥링크 구현
		 * 
		 * 딥링크로 전달된 앱스키마 액션을 실행합니다.
		 */
		IgawLiveOps.onNewIntent(AkMallActivity.this, intent);
		 
 
		/* 2. Json 문자열 타입({“url”:”deepLinkAction”}) 딥링크 구현
		 * 
		 * 딥링크로 전달된 Json 문자열을 추출하고 Json 오브젝트로 변환합니다.
		 */
		String jsonStr = intent.getStringExtra("com.igaworks.liveops.deepLink");
		JSONObject jsonObj;
		try {
			 jsonObj = new JSONObject(jsonStr);
		      //Json 오브젝트를 파싱하여 액션을 실행하도록 구현합니다.
			 String link = jsonObj.get("url").toString();
			 
			 if (!link.equals(Const.URL_BASE) && link != null) {
				 
				 deepLinkURL = link;
			 }
			 
			 
		} catch (Exception e) {
		      // TODO: handle exception
		}
		
		setIntent(intent);
        handleIntent();
		 
    }

    @Override
    protected void onPause() {
        if (DBG) {
            Log.d(TAG, "onPause");
        }
        IgawCommon.endSession();//p65458 20150716 IgawCommon add sdk
        CookieSyncManager.getInstance().stopSync();
        CookieSyncManager.getInstance().sync();
        super.onPause();
        
        //Added by Eddy 2015-12-07
		IgawCommon.endSession();
		//end of Added by Eddy 2015-12-07
		
		mWebView.onPause();
		
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        /**
         * 앱 종료시에도 유튜브 동영상 안꺼지는 것 때문에 추가
         */
        mWebView.destroy();
        mWebView=null;
    }
    
    public static void showWebView() {
    	
    	if(akContentsLayout.getVisibility() == View.VISIBLE){
    		akContentsLayout.startAnimation(outToRightAnimation());
    		akContentsLayout.setVisibility(View.GONE);
    		mWebView.startAnimation(inFromLeftAnimation());
    		mWebView.setVisibility(View.VISIBLE);
    		nowView = "";
    	}
    }
    
    public static void showNotiBoxView(String PID) {
    	notiBoxView =  new NotiBoxView(mainActivity.getApplicationContext());
    	if(PID!=null && !"".equals(PID)) notiBoxView.setPushPid(PID);
    	akContentsLayout.removeAllViews();
        akContentsLayout.addView(notiBoxView);
        
    	akContentsLayout.setVisibility(View.VISIBLE);
    	
    	nowView = "notiBoxView";
    	notiBoxView.startAnimation(inFromRightAnimation());
    	
    	mWebView.startAnimation(outToLeftAnimation());
    	mWebView.setVisibility(View.GONE);
    }
    
    public static void showNotiSetView() {
    	notiSetView =  new NotiSetView(mainActivity.getApplicationContext());
    	akContentsLayout.removeAllViews();
        akContentsLayout.addView(notiSetView);      
    	akContentsLayout.setVisibility(View.VISIBLE);
    	
    	nowView = "notiSetView";
    	notiSetView.startAnimation(inFromRightAnimation());
    	
    	mWebView.startAnimation(outToLeftAnimation());
    	mWebView.setVisibility(View.GONE);
    	
    }
    
    public static void showNotiTimeSetView() {
    	notiTimeSetView =  new NotiTimeSetView(mainActivity.getApplicationContext());
        akContentsLayout.addView(notiTimeSetView);      
    	akContentsLayout.setVisibility(View.VISIBLE);
    	
    	nowView = "notiTimeSetView";
    	notiTimeSetView.startAnimation(inFromRightAnimation());
    	
    	notiSetView.startAnimation(outToLeftAnimation());
    	notiSetView.setVisibility(View.GONE);
    }
    
    public static void closeNotiTimeSetView() {
    	notiSetView =  new NotiSetView(mainActivity.getApplicationContext());
        akContentsLayout.addView(notiSetView);
    	akContentsLayout.setVisibility(View.VISIBLE);
    	
    	nowView = "notiSetView";
    	notiSetView.startAnimation(inFromLeftAnimation());    	
    	notiTimeSetView.startAnimation(outToRightAnimation());
    	notiTimeSetView.setVisibility(View.GONE);
    	akContentsLayout.removeView(notiTimeSetView);
    }
    
    public static void showNotiSetSoundView() {
    	notiSetSoundView =  new NotiSetSoundView(mainActivity.getApplicationContext());
        akContentsLayout.addView(notiSetSoundView);      
    	akContentsLayout.setVisibility(View.VISIBLE);
    	
    	nowView = "notiSetSoundView";
    	notiSetSoundView.startAnimation(inFromRightAnimation());
    	
    	notiSetView.startAnimation(outToLeftAnimation());
    	notiSetView.setVisibility(View.GONE);
    }
    
    public static void closeNotiSetSoundView() {
    	notiSetView =  new NotiSetView(mainActivity.getApplicationContext());
        akContentsLayout.addView(notiSetView);
    	akContentsLayout.setVisibility(View.VISIBLE);
    	
    	nowView = "notiSetView";
    	notiSetView.startAnimation(inFromLeftAnimation());    	
    	notiSetSoundView.startAnimation(outToRightAnimation());
    	notiSetSoundView.setVisibility(View.GONE);
    	akContentsLayout.removeView(notiSetSoundView);
    }
    
    private static Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
    

    private static Animation inFromLeftAnimation() {
	    Animation inFromLeft = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, -1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    inFromLeft.setDuration(500);
	    inFromLeft.setInterpolator(new AccelerateInterpolator());
	    return inFromLeft;
    }


    
    private static Animation outToLeftAnimation() {
	    Animation outtoLeft = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, -1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoLeft.setDuration(500);
	    outtoLeft.setInterpolator(new AccelerateInterpolator());
	    return outtoLeft;
    }


    
    private static Animation outToRightAnimation() {
	    Animation outtoRight = new TranslateAnimation(
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, +1.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f,
	        Animation.RELATIVE_TO_PARENT, 0.0f);
	    outtoRight.setDuration(500);
	    outtoRight.setInterpolator(new AccelerateInterpolator());
	    return outtoRight;
    }

    private void showIntro() {
        mIntroView.inflate();
        mIntroView.setVisibility(View.VISIBLE);

    }
    
    private void showGuide() {
    	mGuideView.inflate();
    	mGuideView.setVisibility(View.VISIBLE);
    }

    private void hiddenIntro() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIntroView.setVisibility(View.GONE);
                //showGuide();//p65458 20150730 guide view add
            }
        }, dTime);
    }
    
    public static void showJavaScript(String str){
    	mWebView.loadUrl(str);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AkMallFacade.REQUEST_LOGIN:
                if (RESULT_OK == resultCode) {
                    mWebView.reload();
                }
                break;
            case AkMallFacade.REQUEST_IMAGE_REVIEW:
                if (RESULT_OK == resultCode) {
                    mWebView.reload();
                }
                break;

            case AkMallFacade.REQUEST_SCAN_QRCODE:
                if (RESULT_OK == resultCode && data != null) {
                    String url = data.getStringExtra(ZxingIntents.Scan.RESULT);
                    
                    if(url.indexOf("akmall") > -1){
                    	mWebView.loadUrl(url);
                    }else{
                    	AkMallFacade.startExternalActivity(this, url);
                    }
                }
                break;
            case AkMallFacade.REQUEST_SCAN_BARCODE:
                if (RESULT_OK == resultCode && data != null) {
                    String url = data.getStringExtra(ZxingIntents.Scan.RESULT);
                    AkMallActivity.showJavaScript("javascript:barCodeReturn('"+url+"')");
                    //AkMallFacade.startExternalActivity(this, url);
                }
                break;
            case FILECHOOSER_RESULTCODE:
            	 if (null == mUploadMessage)
                     return;
                 Uri result = data == null || resultCode != RESULT_OK ? null
                         : data.getData();
                 mUploadMessage.onReceiveValue(result);
                 mUploadMessage = null;
            	break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private WebViewListener mWebViewListener = new WebViewListener() {
        @Override
        public void onProgressChanged(int progress) {
        	mBarProgressBar.setProgress(progress);
        }

        @Override
        public void onPageStarted(String url) {
        	Log.d("Test", "*********onPageStarted");

        	mUriCode = mUriProvider.getUriCode(Uri.parse(url));
            handleUriCode(mUriCode);

//            mCenterProgressBar.setVisibility(View.VISIBLE);
            mBarProgressBar.setVisibility(View.VISIBLE);

            if (DBG) {
                Uri uri = Uri.parse(url);
                Log.d(TAG, "getScheme: " + uri.getScheme());
                Log.d(TAG, "getHost: " + uri.getHost());
                Log.d(TAG, "getPath: " + uri.getPath());
                Log.d(TAG, "getLastPathSegment: " + uri.getLastPathSegment());
                Log.d(TAG, "getQuery: " + uri.getQuery());
                Log.d(TAG, "getFragment: " + uri.getFragment());
            }

        }

        @Override
        public void onPageFinished(String url) {
        	Log.d("Test", "*********onPageFinished");
            if (mIsFirstLoad) {
                mIsFirstLoad = false;
            }

//            mCenterProgressBar.setVisibility(View.GONE);
            mBarProgressBar.setVisibility(View.GONE);
            mNaviTab.setBackwardEnabled(mWebView.canGoBack());
            mNaviTab.setForwardEnabled(mWebView.canGoForward());
            if (mDeviceInfoUpdateTask == null && (!mIsRegisteredServer || !mIsUpdatedDevice)) {
                mDeviceInfoUpdateTask = (DeviceInfoUpdateTask) new DeviceInfoUpdateTask().execute();
            }
            
            mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }
                   
        private void handleUriCode(int uriCode) {
            switch (uriCode) {
                case UriProvider.URI_MYAK:
                    mNaviTab.setCurrentMainTabByUriCode(uriCode);
                    break;

                default:
                    mNaviTab.setCurrentMainTabByUriCode(uriCode);
                    mNaviTab.showMainTab();
//                    mWebView.clearHistory();
                    break;
            }
        }

    }; // mWebViewListener

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if("notiBoxView".equals(nowView)){
        		showWebView();
        	}
        	else if("notiSetView".equals(nowView)){
        		showWebView();
        	}
        	else if("notiTimeSetView".equals(nowView)){
        		closeNotiTimeSetView();
        	}
        	else if("notiSetSoundView".equals(nowView)){
        		closeNotiSetSoundView();
        	}
        	else{
        		goBackOrFinish();
        	}
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBackOrFinish() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            showFinishDialog();
        }
    }

    private void showFinishDialog() {
    	
    	CommonDialog commonDialog = new CommonDialog(AkMallActivity.mainActivity, false, 
    			getString(R.string.service_finish), getString(android.R.string.ok), getString(android.R.string.no));
		commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				CommonDialog commonDialog = (CommonDialog) dialog;
				if(commonDialog.IsOk()) {
					finish();
				}
			}
		});
		commonDialog.show();
    }
    
    private void showPushConfirmDialog() {
    	
    	CommonDialog commonDialog = new CommonDialog(AkMallActivity.mainActivity, false, 
    			getString(R.string.request_allow_push), getString(R.string.allow), getString(R.string.disallow));
		commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				CommonDialog commonDialog = (CommonDialog) dialog;
				if(commonDialog.IsOk()) {
					 PreferenceFacade.setAllowPush(AkMallActivity.this, true);
                     GcmManager.registerInBackground(AkMallActivity.this, mRegisterGcmCallback);
                     initialPost();
				}
				else{
					PreferenceFacade.setAllowPush(AkMallActivity.this, false);
                    GcmManager.registerInBackground(AkMallActivity.this, mRegisterGcmCallback);
                    initialPost();
				}
			}
		});
		commonDialog.show();
    	
    }

    // 네트워크 연결 확인 -> 통화료 발생 알림 -> 푸시 토큰 생성/등록 확인 -> 페이지 로드
    private void confirmState() {
        int networkStatus = NetworkUtil.getStatus(this);
        switch (networkStatus) {

            case NetworkUtil.NETWORK_NONE:
                AkMallFacade.showNetworkErrorDialog(this); // after finish..
                break;

            case NetworkUtil.NETWORK_WIFI:
            case NetworkUtil.NETWORK_3G_4G:
            default:
                checkPushConfirm();
                break;
        }
    }

    private void checkPushConfirm() {
        if (PreferenceFacade.isConfirmedAllowPush(this)) {
            GcmManager.registerInBackground(AkMallActivity.this, mRegisterGcmCallback);
            initialPost();
        } else {
            showPushConfirmDialog();
        }
    }

    private FinishCallback mRegisterGcmCallback = new FinishCallback() {

        @Override
        public void onFinish(boolean isSuccess) {
            if (!isSuccess) {
                Toast.makeText(AkMallActivity.this, R.string.fail_regist_alarm, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };


    private class DeviceInfoUpdateTask extends AsyncTask<Void, Void, Void> {

        private boolean isRegistered;
        private boolean isUpdatedDevice;

        @Override
        protected Void doInBackground(Void... params) {
            isRegistered = mIsRegisteredServer;
            isUpdatedDevice = mIsUpdatedDevice;

            if (!isRegistered) {
                isRegistered = GcmManager.register(AkMallActivity.this);
            }

            if (!mIsUpdatedDevice && isRegistered) {
                isUpdatedDevice = AkMallAPI.updateDeviceUserId(AkMallActivity.this);//p65458 20150722 todo
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mIsUpdatedDevice = isUpdatedDevice;
            mIsRegisteredServer = isRegistered;
            mDeviceInfoUpdateTask = null;
        }
    }
    
    //p65458 20150907 루팅 체크 관련 기능 추가 
    /**
     * 루팅파일 의심 Path를 가진 파일들을 생성 한다.
     */
    private File[] createFiles(String[] sfiles){
        File[] rootingFiles = new File[sfiles.length];
        for(int i=0 ; i < sfiles.length; i++){
            rootingFiles[i] = new File(sfiles[i]);
        }
        return rootingFiles;
    }
     
    /**
     * 루팅파일 여부를 확인 한다.
     */
    private boolean checkRootingFiles(File... file){
        boolean result = false;
        for(File f : file){
            if(f != null && f.exists() && f.isFile()){
                result = true;
                break;
            }else{
                result = false;
            }
        }
        return result;
    }
    
    public boolean checkFile() {
        String[] arrayOfString = {"/system/bin/.ext", "/system/xbin/.ext"};
        int i= 0;
        while(true) {
                if(i >= arrayOfString.length)
                        return false;
                if(new File(arrayOfString[i]).exists())
                        return true;
 
                i++;
                }
    }


	public boolean execCmd() {
	    boolean flag = false;
	    try {
	            Runtime.getRuntime().exec("su");
	            flag = true;
	    } catch(Exception e) {
	            flag = false;
	    }
	     
	    return flag;
	}
    //p65458 20150907 루팅 체크 관련 기능 추가 
	
	
    class MyJavaScriptInterface
    {
    	private Thread TabThread;
    	private Handler TabHandelr;
    	
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            //System.out.println(html);
            if (html.trim().indexOf("purchaseJson") > 0 )
            {
            	String startStr = "var purchaseJson = '[' +";
            	String endStr = "+ ']';";
            	int nStart = html.trim().indexOf(startStr);
            	int nEnd = html.trim().indexOf(endStr, nStart);
            	String jsonStr = "[" + html.substring(nStart+startStr.length()+1, nEnd-1).trim().replace("'", "") + "]";
            	//System.out.println("**************start=" + nStart + " end=" + nEnd + " str=[" + jsonStr + "]");
            	
            	IgawCommerce.purchase(mainActivity.getApplicationContext(), jsonStr); 
            }
            if (html.trim().indexOf("appDisplayFooter") > 0 )
            {
            	if(AkMallActivity.tabVisibleCtr){
            		return;
            	}
            	AkMallActivity.tabVisibleCtr = true;
            	//toolBarHide(false);
            	tabController();
            }else{
            	AkMallActivity.tabVisibleCtr = false;
            	//toolBarHide(true);
            	tabController();
            }
        }
        
        private void tabController() {

    		// 스레드 객체 생성
        	TabThread = new Thread(new Runnable() {
    			@Override
    			public void run() {
    				try {
    					// 스레드 내에 안드로이드 UI를 바꿀 수 있는 runOnUiThread를 작동시킴
    					runOnUiThread(new Runnable() {
    						@Override
    						public void run() {
    							if(AkMallActivity.tabVisibleCtr){
    								toolBarHide(false);
    							}else{
    								toolBarHide(true);
    							}
    						}
    					});

    					TabHandelr.sendEmptyMessage(0); // 핸들러에 빈 메시지를 보냄
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		});

    		// 핸들러 객체 생성
        	TabHandelr = new Handler() {
    			@Override
    			public void handleMessage(Message msg) {
    				TabThread.interrupt(); // 스레드를 인터럽트하도록 함
    			}
    		};

    		TabThread.start(); // 스레드 시작
    	}
    }
    
    private String getDeviceID()
    {
    	final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(this.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        
        Log.d("Test", "==>ID=" + deviceId);
        return deviceId;        
    }
    
}
