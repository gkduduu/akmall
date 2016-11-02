package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

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

    //설정
    @Click(R.id.TEST_SETTING)
    void clickSetting() {
        startActivity(new Intent(this,SettingActivity_.class));
    }

    @AfterViews
    void afterView(){
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        //비디오뷰에 비디오 세팅
        TEST_VIDEO.setVideoURI(Uri.parse("http://www.androidbegin.com/tutorial/AndroidCommercial.3gp"));
        final MediaController mediaController =
                new MediaController(this);
        TEST_VIDEO.setMediaController(mediaController);
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
