package com.ak.android.akmall.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.BigWidgetProvider;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.gcm2.GcmManager;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.SplashResult;
import com.ak.android.akmall.utils.json.result.WidgetResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import net.daum.mf.speech.api.SpeechRecognizerManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    @ViewById
    WebView SPLASH_WEBVIEW;

    String SENDER_ID = Const.GCM_SENDER_ID;
    String regid;

    GoogleCloudMessaging gcm;


    @AfterViews
    void afterView() {
        requestSplash();
    }

    private void initView(SplashResult result) {
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        SPLASH_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        SPLASH_WEBVIEW.getSettings().setUseWideViewPort(true);
        SPLASH_WEBVIEW.setWebContentsDebuggingEnabled(true);
        SPLASH_WEBVIEW.getSettings().setLoadWithOverviewMode(true);
        SPLASH_WEBVIEW.getSettings().setUseWideViewPort(true);
        SPLASH_WEBVIEW.loadUrl(result.link);

        gcm = GoogleCloudMessaging.getInstance(this);
        regid = GcmManager.getRegistrationId(this);

        JHYLogger.d(regid);

        if (regid.isEmpty()) {
            registerInBackground();
        } else {
            registPushInfoInServer();
        }
    }

    //서버에 푸시 등록
    private void registPushInfoInServer() {
        String appid = Settings.Secure.getString(SplashActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String deny_all = "no";//이거안씀씨 ㅏㄹ7
        String registeredVersion = GcmManager.getAppVersion(SplashActivity.this) + "";
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestGCMRegister(SplashActivity.this, appid, deny_all, registeredVersion, regid, "",
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(SplashActivity.this, MainActivity_.class));
                                        String move = BaseUtils.nvl(getIntent().getStringExtra("move"));
                                        JHYLogger.d(move + "$$$$$$$$$$$$$$");
                                        if (move.equals("check")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", Const.WIDGET_CHECK));
                                        } else if (move.equals("event")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", Const.WIDGET_EVENT));
                                        } else if (move.equals("bag")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
                                        } else if (move.equals("delivery")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", Const.WIDGET_DELIVERY));
                                        } else if (move.equals("login")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", "/login/Login.do?isAkApp=Android"));
                                        } else if (move.equals("search")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", Const.MENU_SEARCH));
                                        } else if (move.equals("my")) {
                                            startActivity(new Intent(SplashActivity.this, MyWebviewActivity_.class).putExtra("url", Const.WIDGET_MY));

                                        }
                                        finish();
                                    }
                                }, 2000);
                            }
                        },
                        new RequestFailureListener() {
                            @Override
                            public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
                            }
                        }
                ));
        DataControlManager.getInstance().runScheduledCommandOnAsync();
    }

    private void requestSplash() {
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestSplash(SplashActivity.this,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
                                initView(Parser.parsingSplash(responseData.toString()));
                            }
                        },
                        new RequestFailureListener() {
                            @Override
                            public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
                            }
                        }
                ));
        DataControlManager.getInstance().runScheduledCommandOnAsync();
    }

    Context context;

    private void registerInBackground() {
        context = getApplicationContext();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(Const.GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // 서버에 발급받은 등록 아이디를 전송한다.
                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
                    sendRegistrationIdToBackend();

                    // 등록 아이디를 저장해 등록 아이디를 매번 받지 않도록 한다.
                    GcmManager.storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                JHYLogger.d(msg);
                registPushInfoInServer();
            }

        }.execute(null, null, null);
    }


    private void sendRegistrationIdToBackend() {

    }

}
