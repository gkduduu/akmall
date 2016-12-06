package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.SharedPreferencesManager;
import com.ak.android.akmall.utils.gcm.GcmManager;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.AddingResult;
import com.ak.android.akmall.utils.json.result.PushSettingResult;
import com.ak.android.akmall.utils.json.result.UserInfoResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_setting)
public class SettingActivity extends Activity {

    @ViewById
    TextView SETTING_TV_ID; /* 아이디 */
    @ViewById
    Switch SETTING_SW_ALARM_RECEIVE; /* 쇼핑정보 알람받기 스위치 */
    @ViewById
    Switch SETTING_SW_ALARM_RECEIVE2; /* 구매정보 알람받기 스위치 */
    @ViewById
    Switch SETTING_SW_ALARM_TIME; /* 알람금지시간설정 스위치 */
    @ViewById
    Switch SETTING_SW_ALARM_NOSOUND; /* 무음알림받기 스위치 */
    @ViewById
    Switch SETTING_SW_BANNER_AUTO;/* 동영상 자동재생생스위치 */

    @ViewById
    TextView SETTING_TV_AFTERTIME;
    @ViewById
    TextView SETTING_TV_BEFORETIME;

    @ViewById
    Button SETTING_BT_LOGOUT;

    @ViewById
    Switch SETTING_SW_LOGIN_AUTO;/* 자동로그인 */
    @ViewById
    TextView SETTING_TV_VERSION; /* 버전 */

    //종료버튼
    @Click(R.id.SETTING_CLOSE)
    void clickClose() {
        finish();
    }

    //로그아웃
    @Click(R.id.SETTING_BT_LOGOUT)
    void clickLogout() {
        if(Feature.isLogin) {
            requestDoLogout();
        }else {

        }
    }

    //전화하기
    @Click(R.id.SETTING_BT_CALL)
    void clickCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1588-2055"));
        startActivity(intent);
    }

    //상담하기
    @Click(R.id.SETTING_BT_CONSULT)
    void clickConsult() {
    }

    //알림금지 시간 앞
    @Click(R.id.SETTING_RL_TIME_BEFORE)
    void clickTimeBefore() {
        final CharSequence[] time = new CharSequence[24];
        for (int i = 0; i < 24; i++) {
            if (i < 9) {
                time[i] = "0" + (i+1) + ":00";
            } else {
                time[i] = (i+1) + ":00";
            }
        }
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setSingleChoiceItems(time, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SETTING_TV_BEFORETIME.setText(time[item]);
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    //알림금지 시간 ㅇ뒤
    @Click(R.id.SETTING_RL_TIME_AFTER)
    void clickTimeAfter() {
        final CharSequence[] time = new CharSequence[24];
        for (int i = 0; i < 24; i++) {
            if (i < 9) {
                time[i] = "0" + (i+1) + ":00";
            } else {
                time[i] = (i+1) + ":00";
            }
        }
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setSingleChoiceItems(time, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                SETTING_TV_AFTERTIME.setText(time[item]);
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    //알림받기
    @Click(R.id.SETTING_SW_ALARM_RECEIVE)
    void clickAlarm1() {
        //쇼핑정보알림
        String yn = "";
        if(SETTING_SW_ALARM_RECEIVE.isChecked()) {
            yn = "Y";
        }else {
            yn = "N";
        }
        requestSettingSetting("shopping_alarm_yn",yn);
        SharedPreferencesManager.setBoolean(this, Const.ALARM_RECEIVE, SETTING_SW_ALARM_RECEIVE.isChecked());
    }

    //알림받기2
    @Click(R.id.SETTING_SW_ALARM_RECEIVE2)
    void clickAlarm2() {
        //구매정보 알림
        String yn = "";
        if(SETTING_SW_ALARM_RECEIVE2.isChecked()) {
            yn = "Y";
        }else {
            yn = "N";
        }
        requestSettingSetting("buy_alarm_yn",yn);
        SharedPreferencesManager.setBoolean(this, Const.ALARM_RECEIVE2, SETTING_SW_ALARM_RECEIVE2.isChecked());
    }

    //알림금지 시간설정
    @Click(R.id.SETTING_SW_ALARM_TIME)
    void clickAlarmTime() {
        if(SETTING_SW_ALARM_TIME.isChecked()) {

        }else {
            requestDenyAlarmTime("00", "00");
        }
        SharedPreferencesManager.setBoolean(this, Const.ALARM_TIME_SETTING_, SETTING_SW_ALARM_TIME.isChecked());
    }

    //무음 알림 받기
    @Click(R.id.SETTING_SW_ALARM_NOSOUND)
    void clickAlarm3() {
        String yn = "";
        if(SETTING_SW_ALARM_NOSOUND.isChecked()) {
            yn = "Y";
        }else {
            yn = "N";
        }
        requestSettingSetting("nosound_yn",yn);
        SharedPreferencesManager.setBoolean(this, Const.ALARM_NO_SOUND, SETTING_SW_ALARM_NOSOUND.isChecked());
    }

    //자동로그인
    @Click(R.id.SETTING_SW_LOGIN_AUTO)
    void clickLoginAuto() {
        SharedPreferencesManager.setBoolean(this, Const.LOGIN_AUTO, SETTING_SW_LOGIN_AUTO.isChecked());
    }

    //동영상 자동재샘
    @Click(R.id.SETTING_SW_BANNER_AUTO)
    void clickBannerAuto() {
        SharedPreferencesManager.setBoolean(this, Const.MOVIE_AUTO_PLAY, SETTING_SW_BANNER_AUTO.isChecked());
    }

    //알람금지 저장
    @Click(R.id.SETTING_BT_DENYSAVE)
    void clickDeny() {
        //알림 설정을 변경 하였습니다.
        if (SharedPreferencesManager.getBoolean(this, Const.ALARM_TIME_SETTING_)) {
            requestDenyAlarmTime(SETTING_TV_BEFORETIME.getText().toString().substring(0, 2), SETTING_TV_AFTERTIME.getText().toString().substring(0, 2));
        }
    }

    @AfterViews
    void afterView() {
        requestMain();
    }

    private void initView(PushSettingResult result, UserInfoResult userInfo) {

        //알림금지시간설정
        SETTING_TV_BEFORETIME.setText(BaseUtils.nvl(result.start_hh,"00") + ":00");
        SETTING_TV_AFTERTIME.setText(BaseUtils.nvl(result.end_hh,"00") + ":00");

        if (userInfo.custId.equals("")) {
            Feature.isLogin = false;
            SETTING_BT_LOGOUT.setText("로그인");
        } else {
            Feature.isLogin = true;
            SETTING_BT_LOGOUT.setText("로그아웃");
        }
        SETTING_TV_ID.setText(userInfo.custId);

        SETTING_SW_ALARM_RECEIVE.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_RECEIVE));
        SETTING_SW_ALARM_RECEIVE2.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_RECEIVE2));
        SETTING_SW_ALARM_TIME.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_TIME_SETTING_));
        SETTING_SW_ALARM_NOSOUND.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_NO_SOUND));
        SETTING_SW_LOGIN_AUTO.setChecked(SharedPreferencesManager.getBoolean(this, Const.LOGIN_AUTO));
        SETTING_SW_BANNER_AUTO.setChecked(SharedPreferencesManager.getBoolean(this, Const.MOVIE_AUTO_PLAY));

        //버전정보
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            SETTING_TV_VERSION.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.getStackTrace();
        }
    }

    //로그인정보 가져오기
    private void requestMain() {
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestMain(SettingActivity.this,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
                                //{"resultMsg":"","resultCode":"0000","hasLogin":"Y",
                                // "userInfo":{"custId":"lotus1121","custNo":72979126,"custName":"송부련"}}
                                requestGetSettingConfig(Parser.parsingUserInfo(responseData.toString()));
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

    //알림금지 시간 설정 변경
    private void requestDenyAlarmTime(String start, String end) {
        String token = GcmManager.getRegistrationId(this);
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestSettingAlarmDenyTime(SettingActivity.this, start, end, token,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                Toast.makeText(SettingActivity.this, "알림 설정을 변경 하였습니다.", Toast.LENGTH_SHORT).show();
                                JHYLogger.d(responseData.toString());
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

    //알람 설정 변경
    private void requestSettingSetting(String what, String yn) {
        String token = GcmManager.getRegistrationId(this);
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestSettingSetting(SettingActivity.this, what, yn, token,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                Toast.makeText(SettingActivity.this, "알림 설정을 변경 하였습니다.", Toast.LENGTH_SHORT).show();
                                JHYLogger.d(responseData.toString());
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

    //설정정보 가져오기
    private void requestGetSettingConfig(final UserInfoResult userInfo) {
        String token = GcmManager.getRegistrationId(this);
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestGetSettingConfig(SettingActivity.this, token,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
                                initView(Parser.parsingPushSetting(responseData.toString()),userInfo);
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

    //로그아웃
    private void requestDoLogout() {
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestDoLogout(SettingActivity.this,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
                                startActivity(new Intent(SettingActivity.this,MainActivity_.class));
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
}