package com.ak.android.akmall.activity;

import android.app.Activity;
import android.util.Log;

import com.ak.android.akmall.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import static android.content.ContentValues.TAG;

@EActivity(R.layout.activity_intro)
public class IntroActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @AfterViews
    void afterView() {

        //gcm 토큰 가져오기
        getInstanceIdToken();

//        //서버연동 테스트
//        DataControlManager.getInstance().addSchedule(
//                new DataControlHttpExecutor().requestLogin(this,
//                        new RequestCompletionListener() {
//                            @Override
//                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
//
//                            }
//                        },
//                        new RequestFailureListener() {
//                            @Override
//                            public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
//
//                            }
//                        }
//                ));
//        DataControlManager.getInstance().runScheduledCommandOnAsync();
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
        }
    }

    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
