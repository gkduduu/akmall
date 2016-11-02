package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.app.Activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_setting)
public class SettingActivity extends Activity {


    @Click(R.id.SETTING_CLOSE)
    void clickClose() {
        finish();
    }
}
