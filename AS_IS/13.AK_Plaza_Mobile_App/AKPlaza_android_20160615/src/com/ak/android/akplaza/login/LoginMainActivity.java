
package com.ak.android.akplaza.login;

import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.ActivityTaskManager;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.HeaderClient;
import com.ak.android.akplaza.common.LoginManager;
import com.ak.android.akplaza.common.NetworkUtil;
import com.ak.android.akplaza.common.PostHttpClient;
import com.ak.android.akplaza.common.ProgressBarManager;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.sns.facebook.AsyncFacebookRunner;
import com.ak.android.akplaza.common.sns.facebook.BaseRequestListener;
import com.ak.android.akplaza.common.sns.facebook.DialogError;
import com.ak.android.akplaza.common.sns.facebook.FaceBookUtility;
import com.ak.android.akplaza.common.sns.facebook.Facebook;
import com.ak.android.akplaza.common.sns.facebook.Facebook.DialogListener;
import com.ak.android.akplaza.common.sns.facebook.FacebookError;
import com.ak.android.akplaza.common.sns.facebook.SessionEvents;
import com.ak.android.akplaza.common.sns.facebook.SessionEvents.AuthListener;
import com.ak.android.akplaza.common.sns.facebook.SessionEvents.LogoutListener;
import com.ak.android.akplaza.common.sns.facebook.SessionStore;
import com.ak.android.akplaza.common.sns.facebook.UpdateStatusResultDialog;
import com.ak.android.akplaza.common.sns.twitter.C;
import com.ak.android.akplaza.common.sns.twitter.TwitterController;
import com.ak.android.akplaza.maintab.BottomTab;

public class LoginMainActivity extends Activity implements OnClickListener {

    private static final String TAG = "LoginMainActivity";

    private RelativeLayout mLogin_btn = null;
    private RelativeLayout mTwitt_btn = null;
    private RelativeLayout mFacebook_btn = null;
    private TextView mTwText = null;
    private TextView mFbText = null;
    private TextView mLogin_state = null;
    private TextView mLogin_set = null;
    private ProgressBarManager progressmanager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginalrimset);
        ActivityTaskManager.getInstance().addActivity(this);
        CookieSyncManager.createInstance(this);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();

        String data_info = NetworkUtil.checkStatus(this);
        if (data_info == "NONE") {
            NetworkUtil.NetworkErrorDialog(this);
        } else {
            loginCheck();
            // checkDeny();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
        loginFinish();
    }

    public void loginFinish() {
        progressmanager.stopProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log.d(TAG, "onDestory()");
        ActivityTaskManager.getInstance().deleteActivity(this);
    }

    private void init() {
        findView_make();

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.ak_loginalrimset);
        progressmanager = null;
        progressmanager = new ProgressBarManager(this);
        progressmanager.ableKeyEvent(false);
        String cmd[] = {
                "", "logout", "loginFinish"
        };
        progressmanager.setCommandClass(this, cmd, null);
        mainLayout.addView(progressmanager);
        
        WebView appheader = (WebView) findViewById(R.id.appheader);
        appheader.getSettings().setJavaScriptEnabled(true);
        HeaderClient headerClient = new HeaderClient();
        headerClient.setmContext(LoginMainActivity.this);
        appheader.setWebViewClient(headerClient);
        String url = Const.URL_LIB + this.getString(R.string.act_header);
        appheader.loadUrl(url);
    }

    private void loginCheck() {
        int state = LoginManager.appGetAutoLogin(this);

        if (LoginManager.isLogin(this) == true || state == 1) {
            login_setText(0);
        } else if (LoginManager.isLogin(this) == false || state == 0) {
            login_setText(1);
        }

        if (TwitterController.checkLoginState(this)) {
            mTwText.setText(TwitterController.getUserScreenName().toString());
        }

    }

    private void login_setText(int state) {
        switch (state) {
            case 0:
                String id = SharedUtil.getSharedString(LoginMainActivity.this, "login", "USERID");
                mLogin_set.setText(R.string.logout_set);
                mLogin_state.setText(id);
                break;
            case 1:
                mLogin_set.setText(R.string.login_set);
                mLogin_state.setText(R.string.no_set);
                break;
        }
    }

    private void findView_make() {
        mLogin_btn = (RelativeLayout) findViewById(R.id.login_bg);
        mLogin_btn.setOnClickListener(this);
        mTwitt_btn = (RelativeLayout) findViewById(R.id.twitt_bg);
        mTwitt_btn.setOnClickListener(this);
        mFacebook_btn = (RelativeLayout) findViewById(R.id.facebook_bg);
        mFacebook_btn.setOnClickListener(this);

        mTwText = (TextView) findViewById(R.id.twitt_text);
        mFbText = (TextView) findViewById(R.id.fb_text);
        mLogin_set = (TextView) findViewById(R.id.login_set);
        mLogin_state = (TextView) findViewById(R.id.login_state);
    }

    private boolean setAlarimState(boolean onoff) {
        boolean is = false;
        String result = "";
        String sessionId = SharedUtil
                .getSharedString(getApplicationContext(), "login", "SESSIONID");
        String token = SharedUtil.getSharedString(this, "C2DM", "token");
        String deny = "";

        if (onoff) {
            deny = "1";
            SharedUtil.setSharedString(this, "DEVICE", "push", "on");
        } else {
            deny = "0";
            SharedUtil.setSharedString(this, "DEVICE", "push", "off");
        }
        String url = Const.URL_LIB + getString(R.string.act_updatedeny);
        url = url.replace("#token#", token);
        url = url.replace("#deny#", deny);
        Log.d("DENYURL", url);
        result = PostHttpClient.appLogin(LoginMainActivity.this, url);

        try {
            JSONObject json = new JSONObject(result);
            String resultMessage = json.getString("result").toString();
            if (resultMessage.equals("success")) {
                is = true;
			}
            Log.d("DENY", resultMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    public void c2dmRegister() {
        String token = SharedUtil.getSharedString(this, "C2DM", "token");
        if (token.equals("") || token.equals(null)) {
            Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
            registrationIntent
                    .putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // 어플리케이션ID
            registrationIntent.putExtra("sender", "wwworld0927@gmail.com"); // 개발자ID
            startService(registrationIntent);
        }
    }

    @Override
    public void onClick(View v) {
        /*
         * //알림 설정 if(v.equals(mAlrim_on)){ if(setAlarimState(false)){
         * mAlrim_on.setVisibility(View.GONE);
         * mAlrim_off.setVisibility(View.VISIBLE); } }else
         * if(v.equals(mAlrim_off)){ if(setAlarimState(true)){
         * mAlrim_on.setVisibility(View.VISIBLE);
         * mAlrim_off.setVisibility(View.GONE); } } if(v.equals(mAlram_Check)){
         * String deviceOk = SharedUtil.getSharedString(this, "DEVICE", "OK");
         * if("".equals(deviceOk) || "1".equals(deviceOk)){
         * mAlram_Check.setChecked(false); showAlert(1); }else{
         * if(mAlram_Check.isChecked()){ if(!setAlarimState(true)){
         * mAlram_Check.setChecked(false); showAlert(0); } }else{
         * if(!setAlarimState(false)){ mAlram_Check.setChecked(true);
         * showAlert(0); } } } }
         */

        // 로그인 , 로그아웃
        if (v.equals(mLogin_btn)) {
            if (LoginManager.isLogin(this) == true) {
                progressmanager.startProgress();
            } else if (LoginManager.isLogin(this) == false) {
            	WebView appheader = (WebView) findViewById(R.id.appheader);
            	String url = Const.URL_LIB + this.getString(R.string.act_login);
                appheader.loadUrl(url);
            	
//                Intent intent = new Intent(LoginMainActivity.this, LoginSubActivity.class);
//                startActivity(intent);
            }
        }
        if (v.equals(mTwitt_btn)) {
            Boolean is = TwitterController.checkLoginState(this);
            if (is == false) {
                TwitterController.login(LoginMainActivity.this);
            } else if (is == true) {
                TwitterController.logout(LoginMainActivity.this);
                // Log.d(TAG, "LOGOUT_SUCCESS");
                mTwText.setText("미설정");
            }
        }
        if (v.equals(mFacebook_btn)) {
            // Log.d("SNS", "FACEBOOK");

            FaceBookUtility.mFacebook = new Facebook("252917478114810");
            // Facebook facebook = new Facebook("103407816421910");
            // FaceBookUtility.mFacebook = facebook;
            // CuQcGzCKUnjw2cvGvPe71Bw6JgM
            // CuQcGzCKUnjw2cvGvPe71Bw6JgM

            FaceBookUtility.mAsyncRunner = new AsyncFacebookRunner(FaceBookUtility.mFacebook);

            SessionStore.restore(FaceBookUtility.mFacebook, this);
            SessionEvents.addAuthListener(new SampleAuthListener());
            SessionEvents.addLogoutListener(new SampleLogoutListener());

            // if(FaceBookUtility.mFacebook.isSessionValid()) {
            // mFbText.setText("설정");
            // }else {
            // mFbText.setText("미설정");
            // }

            String[] permissions = {
                    "offline_access", "publish_stream", "user_photos", "publish_checkins",
                    "photo_upload"
            };
            if (FaceBookUtility.mFacebook.isSessionValid()) {
                String access_token = FaceBookUtility.mFacebook.getAccessToken();
                // Log.d("TOKEN", access_token);
                SessionEvents.onLogoutBegin();
                FaceBookUtility.mAsyncRunner.logout(this, new LogoutRequestListener());
            } else {
                FaceBookUtility.mFacebook.authorize(LoginMainActivity.this, permissions, 0,
                        new LoginDialogListener());
            }
        }
    }

    public void logout() {
        LoginManager.appLogout(this);
        CookieSyncManager.getInstance().sync();
        loginCheck();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("LOGIN", "LOGIN");
        if (resultCode == RESULT_OK) {
            if (requestCode == C.TWITTER_LOGIN_CODE) {
                TwitterController.setToken(this, data);

                mTwText.setText(TwitterController.getUserScreenName().toString());
            } else if (requestCode == 0) {
                FaceBookUtility.mFacebook.authorizeCallback(requestCode, resultCode, data);
            }
        }
    }

    // ///facebook

    public class SampleAuthListener implements AuthListener {

        @Override
        public void onAuthSucceed() {
            mFbText.setText("설정");

            // Log.d("FaceBook", "You have logged in! ");
        }

        @Override
        public void onAuthFail(String error) {
            mFbText.setText("Login Failed: " + error);
            // Log.d("FaceBook", "Login Failed: " + error);
        }
    }

    public class SampleLogoutListener implements LogoutListener {
        @Override
        public void onLogoutBegin() {
            mFbText.setText("Logging out...");
            // Log.d("FaceBook", "Logging out... ");
        }

        @Override
        public void onLogoutFinish() {
            mFbText.setText("미설정");

            // Log.d("FaceBook", "미설정");

        }
    }

    private final class LoginDialogListener implements DialogListener {
        @Override
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
            mFbText.setText("설정 ");
        }

        @Override
        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        @Override
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        @Override
        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }

    private class LogoutRequestListener extends BaseRequestListener {
        @Override
        public void onComplete(String response, final Object state) {
            /*
             * callback should be run in the original thread, not the background
             * thread
             */
            SessionEvents.onLogoutFinish();
            mFbText.setText("미설정");
            // mHandler.post(new Runnable() {
            // public void run() {
            //
            // }
            // });
        }
    }

    public class UpdateStatusListener extends
            com.ak.android.akplaza.common.sns.facebook.BaseDialogListener {
        @Override
        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                new UpdateStatusResultDialog(LoginMainActivity.this, "Update Status executed",
                        values).show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "No wall post made",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onFacebookError(FacebookError error) {
            Toast.makeText(getApplicationContext(), "Facebook Error: " + error.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast toast = Toast.makeText(getApplicationContext(), "Update status cancelled",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void backButtonPressed(View view) {
        finish();
    }

}
