
package com.ak.android.akplaza.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.maintab.BottomTab;
import com.ak.android.akplaza.qrcode.ZxingIntents;
import com.ak.android.akplaza.receipt.ReceiptSubActivity;
import com.ak.android.akplaza.widget.AkPlazaWebView;
import com.ak.android.akplaza.widget.AkPlazaWebView.WebViewListener;

public class WebViewActivity extends Activity implements OnClickListener {

    public static final String EXTRA_URL = "akplaza.extra.URL";
    public static final String ACTION_VIEW_URL = "intent.action.VIEW_URL";

    
    private static final String TAG = "WebViewActivity";
    private AkPlazaWebView mWebView;
    private CookieManager cookieManager;
    private CookieSyncManager mCookieSyncMgr;
    private String mUrlStr = "";
    private ProgressBar mProgressBar = null;
    public static boolean mIsReloadable;

    
    private static boolean sIsExist = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        mIsReloadable = false;
        ActivityTaskManager.getInstance().addActivity(this);
        setContentView(R.layout.webview);
        sIsExist = true;
        mCookieSyncMgr = CookieSyncManager.createInstance(this);

        cookieManager = CookieManager.getInstance();
        
        AkPlazaWebView.activity = this;

        AkPlazaFacade.checkVersion(this);
        init();
    }

    @Override
    protected void onDestroy() {
        ActivityTaskManager.getInstance().deleteActivity(this);
        cookieManager.removeSessionCookie();
        sIsExist = false;
        super.onDestroy();
    }

    private void init() {
        setWebViewSetting();
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();

        if (ACTION_VIEW_URL.equals(action)) {
            handleViewUrlIntent(intent);
            return;
        }

        String event = intent.getStringExtra("EVENT");
        if (event != null) {
            handlePushIntent(intent);
            return;
        }

        mUrlStr = intent.getStringExtra("URL");

        Uri uri = intent.getData();
        if (uri == null) {
            mWebView.loadUrl(mUrlStr);
            return;
        }

        String url = uri.getQueryParameter("return");

        if (url == null) {
            url = uri.getQueryParameter("membersReturn");
            if (url != null && "home".equals(url)) {
                url = Const.URL_ADDRESS + getString(R.string.act_main); // home
                BottomTab.setCurrentTab(BottomTab.TAB_HOME);
            }
        }

        try {
            mUrlStr = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        }

        mWebView.loadUrl(mUrlStr);
    }

    private void handleViewUrlIntent(Intent intent) {
        Uri url = intent.getData();
        mUrlStr = url.toString();
        mWebView.loadUrl(mUrlStr);
    }

    private void handlePushIntent(Intent intent) {
        String pid = intent.getStringExtra("PID");
        if (pid == null) {
            pid = "";
        }
        String viewPushDetailUrl = Const.URL_LIB + getString(R.string.act_view_push_detail, pid);
        mUrlStr = viewPushDetailUrl;
        mWebView.loadUrl(mUrlStr);
    }

    @Override
    protected void onPause() {
        mCookieSyncMgr.stopSync();
        mCookieSyncMgr.sync();
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    public void onResume() {
        
        if (mIsReloadable) {
            mWebView.reload();
        } else {
            mIsReloadable = false;
        }
        BottomTab botTab = (BottomTab)findViewById(R.id.bottom);
        botTab.setBadge();
        super.onResume();
    }

    private void setWebViewSetting() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWebView = (AkPlazaWebView) findViewById(R.id.common_webview);
        mWebView.setWebViewListener(mWebViewListener);
        BottomTab.setmWebView(mWebView);
    } // setWebViewClient

    private WebViewListener mWebViewListener = new WebViewListener() {

        @Override
        public void onProgressChanged(int progress) {
            // nothing..
        }

        @Override
        public void onPageStarted(String url) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(String url) {
            mProgressBar.setVisibility(View.GONE);
        }

    }; // mWebViewListener

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AkPlazaFacade.REQUEST_SCAN_QRCODE:
                handleQrCodeRequest(resultCode, data);
                break;

            case AkPlazaFacade.REQUEST_SCAN_BARCODE:
                handleBarcodeRequest(resultCode, data);
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleBarcodeRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra(ZxingIntents.Scan.RESULT);
            if (result == null) {
                result = "";
            }
            Intent intent = new Intent(this, ReceiptSubActivity.class);
            intent.putExtra("DATA", result);
            startActivity(intent);
        }
    }

    private void handleQrCodeRequest(int resultCode, Intent data) {
        if (RESULT_OK == resultCode && data != null) {
            String url = data.getStringExtra(ZxingIntents.Scan.RESULT);
            String[] akUrls = getResources().getStringArray(R.array.akurl_array);

            boolean isAkUrl = false;
            for (String akUrl : akUrls) {
                if (url.startsWith(akUrl)) {
                    isAkUrl = true;
                    break;
                }
            }

            if (isAkUrl) {
                mIsReloadable = false;
                mWebView.loadUrl(url);

            } else {
                AkPlazaFacade.startExternalActivity(this, url);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBackOrFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBackOrFinish() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            setFinishDialog();
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void setFinishDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setIcon(R.drawable.logo_small).create();
        ab.setTitle(" ").create();
        ab.setMessage("서비스를 종료하시겠습니까?").create();
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.appAutoLogout(WebViewActivity.this);

                // 쿠키를 왜 모두 삭제하는가?
                // if (cookieManager != null) {
                // cookieManager.removeAllCookie();
                // }
                ActivityTaskManager.getInstance().allEndActivity();
            }
        }).create();
        ab.setNegativeButton("아니오", null).create();
        ab.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
        mIsReloadable = false;
        super.onNewIntent(intent);
    }

    public static boolean isExist() {
        return sIsExist;
    }
    
    //KCP 기능 추가 20140717

    @Override
    protected void onRestart()
    {
        super.onResume();
//        mIsReloadable  = false;

        AKPlazaApplication myApp = (AKPlazaApplication)getApplication();

        // 하나 SK 모듈로 결제 이후 해당 카드 정보를 가지고 오기위해 사용
        if(myApp.m_uriResult != null)
        {
            /*
            if( myApp.m_uriResult.getQueryParameter("realPan") != null &&
                myApp.m_uriResult.getQueryParameter("cavv")    != null &&
                myApp.m_uriResult.getQueryParameter("xid")     != null &&
                myApp.m_uriResult.getQueryParameter("eci")     != null
            )
            {
                Log.d( SampleApplication.m_strLogTag,
                       "[PayDemoActivity] HANA SK Result = javascript:hanaSK('"     + myApp.m_uriResult.getQueryParameter("realPan") +
                                                                             "', '" + myApp.m_uriResult.getQueryParameter("cavv")    +
                                                                             "', '" + myApp.m_uriResult.getQueryParameter("xid")     +
                                                                             "', '" + myApp.m_uriResult.getQueryParameter("eci")     +
                                                                             "', '" + CARD_CD                                        +
                                                                             "', '" + QUOTA                                          + "');" );

                // 하나 SK 모듈로 인증 이후 승인을 하기위해 결제 함수를 호출 (주문자 페이지)
                mWebView.loadUrl( "javascript:hanaSK('"     + myApp.m_uriResult.getQueryParameter("realPan")  +
                                                     "', '" + myApp.m_uriResult.getQueryParameter("cavv")     +
                                                     "', '" + myApp.m_uriResult.getQueryParameter("xid")      +
                                                     "', '" + myApp.m_uriResult.getQueryParameter("eci")      +
                                                     "', '" + CARD_CD                                         +
                                                     "', '" + QUOTA                                           + "');" );
            }

            if( (myApp.m_uriResult.getQueryParameter("res_cd") == null? "":
                 myApp.m_uriResult.getQueryParameter("res_cd")             ).equals("999"))
            {
                Log.d( SampleApplication.m_strLogTag,
                       "[PayDemoActivity] HANA SK Result = cancel" );

                m_nStat = 9;
            }
            */
            
            if( (myApp.m_uriResult.getQueryParameter("isp_res_cd") == null? "":
                 myApp.m_uriResult.getQueryParameter("isp_res_cd")             ).equals("0000"))
            {

                mWebView.loadUrl( "http://testpay.kcp.co.kr/lds/smart_phone_linux_jsp/sample/card/samrt_res.jsp?result=OK&a=" + myApp.m_uriResult.getQueryParameter("a") );
            }
            else
            {
            }
        }

        if ( AkPlazaWebView.m_nStat == AkPlazaWebView.PROGRESS_STAT_IN )
        {
            checkFrom();
        }

        myApp.m_uriResult = null;
    }

    private void checkFrom()
    {
    	mIsReloadable  = false;
        try
        {
            AKPlazaApplication myApp = (AKPlazaApplication)getApplication();
            if ( myApp.m_uriResult != null )
            {
            	AkPlazaWebView.m_nStat = AkPlazaWebView.PROGRESS_DONE;

                String strResultInfo = myApp.m_uriResult.getQueryParameter( "approval_key" );

                if ( strResultInfo == null || strResultInfo.length() <= 4 )  finishActivity( "ISP 결제 오류" );

                String  strResCD = strResultInfo.substring( strResultInfo.length() - 4 );


                if ( strResCD.equals( "0000" ) == true )
                {
                    String strApprovalKey = "";

                    strApprovalKey = strResultInfo.substring( 0, strResultInfo.length() - 4  );


                    mWebView.loadUrl( Const.KCP_ADDRESS+"/app.do?ActionResult=app&approval_key=" + strApprovalKey );
                }
                else if ( strResCD.equals( "3001" ) == true )
                {
                    finishActivity( "ISP 결제 사용자 취소" );
                }
                else
                {
                    finishActivity( "ISP 결제 기타 오류" );
                }
            }
        }
        catch ( Exception e )
        {
        }
        finally
        {
        }
    }

    @Override
    protected Dialog onCreateDialog( int id )
    {

        super.onCreateDialog( id );

        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder( this );
        AlertDialog alertDlg;

        dlgBuilder.setTitle( "취소" );
        dlgBuilder.setMessage( "결제가 진행중입니다.\n취소하시겠습니까?" );
        dlgBuilder.setCancelable( false );
        dlgBuilder.setPositiveButton( "예", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                finishActivity( "사용자 취소" );
            }
        });

        dlgBuilder.setNegativeButton( "아니오", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        alertDlg = dlgBuilder.create();

        return  alertDlg;
    }

    public void finishActivity( String p_strFinishMsg )
    {
        Intent intent = new Intent();

        if ( p_strFinishMsg != null )
        {
            intent.putExtra( AkPlazaWebView.ACTIVITY_RESULT, p_strFinishMsg );

            setResult( RESULT_OK, intent );
        }
        else
        {
            setResult( RESULT_CANCELED );
        }

        finish();
    }
    
}
