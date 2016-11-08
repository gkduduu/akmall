package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.utils.Const;
import android.ak.com.akmall.utils.SharedPreferencesManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
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
    Switch SETTING_SW_ALARM_RECEIVE; /* 알람받기 스위치 */
    @ViewById
    Switch SETTING_SW_ALARM_TIME; /* 알람금지시간설정 스위치 */
    @ViewById
    Switch SETTING_SW_ALARM_NOSOUND; /* 무음알림받기 스위치 */
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

    //알림받기
    @Click(R.id.SETTING_SW_ALARM_RECEIVE)
    void clickAlarm1() {
        SharedPreferencesManager.setBoolean(this,Const.ALARM_RECEIVE,SETTING_SW_ALARM_RECEIVE.isChecked());
    }
    //알림금지 시간설정
    @Click(R.id.SETTING_SW_ALARM_TIME)
    void clickAlarm2() {
        SharedPreferencesManager.setBoolean(this,Const.ALARM_TIME_SETTING_,SETTING_SW_ALARM_TIME.isChecked());
    }
    //무음 알림 받기
    @Click(R.id.SETTING_SW_ALARM_NOSOUND)
    void clickAlarm3() {
        SharedPreferencesManager.setBoolean(this,Const.ALARM_NO_SOUND,SETTING_SW_ALARM_NOSOUND.isChecked());
    }

    @AfterViews
    void afterView() {
        SETTING_SW_ALARM_RECEIVE.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_RECEIVE));
        SETTING_SW_ALARM_TIME.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_TIME_SETTING_));
        SETTING_SW_ALARM_NOSOUND.setChecked(SharedPreferencesManager.getBoolean(this, Const.ALARM_NO_SOUND));

        //버전정보
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            SETTING_TV_VERSION.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.getStackTrace();
        }
    }
}
