package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.SharedPreferencesManager;
import com.ak.android.akmall.utils.gcm.GcmManager;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.arasthel.asyncjob.AsyncJob;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.daum.mf.speech.api.SpeechRecognizerManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_test)
public class TestActivity extends Activity {

    @ViewById
    VideoView TEST_VIDEO;

    //qr코드 스캔/6

    @Click(R.id.TEST_QRCODE)
    void clickQR() {
        new IntentIntegrator(this).initiateScan();
    }

    //음성인식
    @Click(R.id.TEST_VOICE)
    void clickVoice() {
        //	056d36313be683772dd4d3662952304b
        startActivityForResult(new Intent(this,VoiceActivity_.class),999);
    }

    //파워딜
    @Click(R.id.TEST_POWERDEAL)
    void clickPower() {
        startActivity(new Intent(this,ShopPowerDealActivity_.class));
    }

    //베스트
    @Click(R.id.TEST_BEST)
    void clickBest() {
        startActivity(new Intent(this,ShopContentActivity_.class).putExtra("url","/display/ShopFront.do?isAkApp=Y&ctgId=10"));
    }

    //바둑판식
    @Click(R.id.TEST_GRID)
    void clickGrid() {
        startActivity(new Intent(this,SplashActivity_.class));
    }

    //설정
    @Click(R.id.TEST_SETTING)
    void clickSetting() {
        startActivity(new Intent(this,SettingActivity_.class));
    }

    //서버연동 테스트
    @Click(R.id.TEST_SERVER)
    void clickServer(){startActivity(new Intent(this,MainActivity_.class));}

    @AfterViews
    void afterView(){
        Log.d("asdf", "asdfasdfdasf");
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        GcmManager.register(TestActivity.this);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        String appid = Settings.Secure.getString(TestActivity.this.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        String deny_all = SharedPreferencesManager.getBoolean(TestActivity.this, Const.ALARM_RECEIVE) ? "no" : "yes";
                        String oldToken = SharedPreferencesManager.getString(TestActivity.this,Const.GCM_PUSH_TOKEN);
                        final String token = GcmManager.getRegistrationId(TestActivity.this);
                        String registeredVersion = GcmManager.getRegisteredVersion(TestActivity.this);

                        JHYLogger.d("appid  =  " + appid);
                        JHYLogger.d("deny_all  =  " + deny_all);
                        JHYLogger.d("oldToken  =  " + oldToken);
                        JHYLogger.d("token  =  " + token);
                        JHYLogger.d("registeredVersion  =  " + registeredVersion);

                        DataControlManager.getInstance().addSchedule(
                                new DataControlHttpExecutor().requestGCMRegister(TestActivity.this,appid,deny_all,registeredVersion,token,oldToken,
                                        new RequestCompletionListener() {
                                            @Override
                                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                                JHYLogger.d(responseData.toString());
                                                GcmManager.setRegisteredServer(TestActivity.this,true);
                                                GcmManager.setRegistrationId(TestActivity.this,token);
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
                }).create().start();


        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        //비디오뷰에 비디오 세팅
//        TEST_VIDEO.setVideoURI(Uri.parse("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp"));
//        final MediaController mediaController =
//                new MediaController(this);
//        TEST_VIDEO.setMediaController(mediaController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 999) {
            //음성인식 결과

        }else {
            // QR코드/바코드를 스캔한 결과 값
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            // 결과값 출력
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(result.getContents() + " [" + result.getFormatName() + "]")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
