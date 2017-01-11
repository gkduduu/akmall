package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.JHYLogger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_voice)
public class VoiceActivity extends Activity {

    SpeechRecognizerClient client;

    @ViewById
    TextView VOICE_RESULT_TEXT;

    @ViewById
    ImageView VOICE_START_RECORDING;

    AnimationDrawable drawable = null;//음성인식 애니메이션
    @ViewById
    ImageView VOICE_ANIMATION;

//    @Click(R.id.VOICE_START_RECORDING)
//
//    @Click(R.id.VOICE_STOP_RECORDING)
//    void clickStop() {
//        //client.cancelRecording(); 은 음성인식을 취소 ->서버 result가 없음
//        client.stopRecording();
//    }

    @Click(R.id.VOICE_CLOSE)
    void clickClose() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //음성인식 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //음성인식 소멸
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    @AfterViews
    void afterView() {
        //음성인식 인스턴스생성
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder()
                .setApiKey(Const.DAUM_API_KEY)  // 발급받은 api key
                .setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB);

        client = builder.build();

        //음성인식ㄷ 리스너
        client.setSpeechRecognizeListener(new SpeechRecognizeListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i, String s) {
                JHYLogger.D("음성인식 오류 >> "+s);
            }

            @Override
            public void onPartialResult(String s) {

            }

            @Override
            public void onResults(Bundle bundle) {
                drawable.stop();
                VOICE_START_RECORDING.setImageResource(R.drawable.voice_off);
                ArrayList<String> texts = bundle.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
                StringBuffer strResult = new StringBuffer();
                strResult.append("Text ===> ");

                for(int i = 0; i<texts.size();i++) {
                    strResult.append(texts.get(i));
                    strResult.append("  |  ");
                }
                if(texts.size()!=0) {
                    Intent intent  = new Intent();
                    intent.putExtra("result", texts.get(0));
                    setResult(Const.VOICE_RESULT,intent);
                    finish();
                }

            }

            @Override
            public void onAudioLevel(float v) {

            }

            @Override
            public void onFinished() {

            }
        });

        clickStart();
    }

    void clickStart() {
        drawable = (AnimationDrawable) VOICE_ANIMATION.getBackground();
        VOICE_ANIMATION.post(new Runnable() {
            @Override
            public void run() {
                drawable = (AnimationDrawable) VOICE_ANIMATION.getBackground();
                drawable.start();
            }
        });
        VOICE_START_RECORDING.setImageResource(R.drawable.voice_on);
        client.startRecording(true);
    }
}
