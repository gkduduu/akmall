package com.ak.android.akmall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.VideoView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BigWidgetProvider;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.WidgetResult;
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

    @Override
    protected void onResume() {
        super.onResume();
    }


}
