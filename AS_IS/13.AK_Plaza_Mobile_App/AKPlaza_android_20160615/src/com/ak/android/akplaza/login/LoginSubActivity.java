package com.ak.android.akplaza.login;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.ActivityTaskManager;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.LoginManager;
import com.ak.android.akplaza.common.NetworkUtil;
import com.ak.android.akplaza.common.PostHttpClient;
import com.ak.android.akplaza.common.ProgressBarManager;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.SoftKeyboardDectectorView;
import com.ak.android.akplaza.common.SoftKeyboardDectectorView.OnHiddenKeyboardListener;
import com.ak.android.akplaza.common.SoftKeyboardDectectorView.OnShownKeyboardListener;
import com.ak.android.akplaza.common.StringUtil;
import com.ak.android.akplaza.common.WebViewActivity;
import com.ak.android.akplaza.intro.IntroActivity;
import com.ak.android.akplaza.maintab.BottomTab;

public class LoginSubActivity extends Activity implements OnClickListener {

	public static final String TAG = "LoginSubActivity";

	private static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
	private ProgressBarManager progressmanager = null;

	private Button mAuto_login_off			= null; //자동로그인
	private Button mAuto_login_on			= null; //자동로그인
//	private Button mId_save_on 				= null; // ID 저장
//	private Button mId_save_off 			= null; // ID 저장
	private Button m_login_ok_btn			= null;  //로그인 버튼
	private Button m_login_out_btn			= null;  //로그아웃 버튼
	private EditText login_IdEdit			= null;
	private EditText login_PwEdit			= null;
	private boolean mUserFlag				= false;
	private TextView mSignup;
	private com.ak.android.akplaza.maintab.BottomTab ab = null;

	private LinearLayout mBottom_tab = null;
 	//ImageView login_logo = null;
	Context mContext = null;
	//int width = 0;
	 int width1 = 0;
	// int height = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.login);
	    Log.d(TAG, "onCreate");
	    CookieSyncManager.createInstance(this);
	    init();
	   //login_logo = (ImageView)findViewById(R.id.login_logo);
	 }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		//width = login_logo.getWidth();
	    //height =  login_logo.getHeight();

	//    Log.d("imgWidth", "imgWidth : " + width);
	//    Log.d("imgHeight", "imgHeight : " + height);
	    //Log.d("imgWidth", "imgWidth : " + width1);
	//    Log.d("imgHeight", "imgHeight : " + height);
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	//	AkBroadcastReceiver.getUnRegisterReiver(this);
		//Log.d(TAG, "onDestory()");
		ActivityTaskManager.getInstance().deleteActivity(this);
	}
	@Override
	protected void onPause() {
//		AkBroadcastReceiver.getUnRegisterReiver(this);
		super.onPause();
		progressmanager.stopProgress();
		//Log.d(TAG, "onPause");
	}
	private void init(){
		ActivityTaskManager.getInstance().addActivity(this);
		findView_make();
		setListener();
		getIntentData(); //<- 아래가 아니라 여기에서 실행되야함.
		checkLoginState();
		setDIP();
//		keyBoardState();
		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.relate_aklogin);
		progressmanager = null;
		progressmanager = new ProgressBarManager(this);
		progressmanager.ableKeyEvent(false);
		String cmd[] = { "login" ,"progressFinish"};
		progressmanager.setCommandClass(this, cmd, null);
		mainLayout.addView(progressmanager);
	}

	private void setDIP(){
	 float scale = this.getResources().getDisplayMetrics().density;
	//	width = (int)(302 / DEFAULT_HDIP_DENSITY_SCALE * scale);
	//	 Log.d("widthhhh", "widthhhh : " + width);
	}

	public void startApp() {
//		getIntentData();
//		checkLoginState();
	}

	public void progressFinish() {
		progressmanager.stopProgress();
	}

	private void getIntentData(){
		Intent intent = getIntent();
		String extra = intent.getStringExtra("UPDATEUSER");
		if(extra != null){
			mUserFlag = true;
		}

	}

	private void findView_make(){
		mAuto_login_off = (Button)findViewById(R.id.auto_login_off);
    	mAuto_login_on = (Button)findViewById(R.id.auto_login_on);
//    	mId_save_on = (Button)findViewById(R.id.id_save_on);
//    	mId_save_off = (Button)findViewById(R.id.id_save_off);
    	m_login_ok_btn = (Button)findViewById(R.id.login_ok_btn);
//    	m_login_out_btn = (Button)findViewById(R.id.login_out_btn);
    	login_IdEdit = (EditText)findViewById(R.id.login_id);
    	login_IdEdit.setPrivateImeOptions("defaultInputmode=english;");
    	login_PwEdit = (EditText)findViewById(R.id.login_pw);
    	mBottom_tab = (LinearLayout)findViewById(R.id.bottom_tab);

    	login_IdEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    	login_PwEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    	PasswordTransformationMethod passwdtm = new PasswordTransformationMethod();
    	login_PwEdit.setTransformationMethod(passwdtm);
    	ab = (BottomTab)findViewById(R.id.bottom);

    	setSignupLink();
	}


	private void setSignupLink() {
		int versionCode = 1;
    	try {
    		versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.w(TAG, "Don't get versionCode.. so default verionCode set 1..");
		}
    	String signupUrl = Const.URL_ADDRESS + getString(R.string.act_signup)
    			+ getString(R.string.param_signup, versionCode);
    	String signup = getString(R.string.login_info_01, signupUrl);

    	mSignup = (TextView)findViewById(R.id.tv_signup);
    	mSignup.setText(Html.fromHtml(signup));
    	mSignup.setMovementMethod(LinkMovementMethod.getInstance());
    	stripUnderlines(mSignup);
	}

	private void setListener(){
		m_login_ok_btn.setOnClickListener(this);
//        m_login_out_btn.setOnClickListener(this);
        mAuto_login_off.setOnClickListener(this);
        mAuto_login_on.setOnClickListener(this);
//        mId_save_on.setOnClickListener(this);
//        mId_save_off.setOnClickListener(this);
	}

	private void checkLoginState(){
		int state = LoginManager.appGetAutoLogin(this);
		if(state == 1){
			setDisplayState(2);
		}else if(state == 2){
			setDisplayState(5);
			login_IdEdit.setText(LoginManager.appGetSavedID(this));
		}

		if(LoginManager.isLogin(this)) {
			Map map = LoginManager.appLoginState(this);
			if(map != null){
				String id = (String)map.get("USERID");
				String pw = (String)map.get("USERPW");
				login_IdEdit.setText(id);
				login_IdEdit.setEnabled(false);
				login_PwEdit.setText(pw);
				login_PwEdit.setEnabled(false);
				setDisplayState(1);
			}
		}
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String data_info = NetworkUtil.checkStatus(this);

		if(v.equals(m_login_ok_btn)){
			if(data_info == "NONE"){
				NetworkUtil.NetworkErrorDialog(this);
			}else{
//				login();
				progressmanager.startProgress();
			}
		}

//		if(v.equals(m_login_out_btn)){
//			logout();
//
//		}

		if(v.equals(mAuto_login_off)){
			setDisplayState(2);
		}

		if(v.equals(mAuto_login_on)){
			setDisplayState(3);
		}

//		if(v.equals(mId_save_on)){
//			setDisplayState(4);
//		}
//
//		if(v.equals(mId_save_off)){
//			setDisplayState(5);
//		}
	}

	private void setAuto(){
		SharedUtil.clearAllSetting(this, "auto", 0);
		if(mAuto_login_on.isShown() == true){
			LoginManager.appSetAutoLogin(this, 0);
		}
			else if(mAuto_login_on.isShown() == false){
			LoginManager.appSetAutoLogin(this, 1);
		}

	}

	private void showDialogOnUiThread(final int id) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showDialog(id);
			}
		});
	}


	public void login(){

		hideKeyBoard();
		String id = StringUtil.NVL(login_IdEdit.getText().toString(), "");
		String pw = StringUtil.NVL(login_PwEdit.getText().toString(), "");


		if(id.equals("") || pw.equals("")){
			showDialogOnUiThread(1);
		}else{
			boolean is = false;
//			pw = StringUtil.encrypString(pw);
			is = LoginManager.appLogin(this, id, pw);

			if(is) {
				LoginManager.updateDeviceTokenUserid(getApplicationContext());
				setAuto();
				showDialogOnUiThread(2);
			}
			else {
				showDialogOnUiThread(3);
			}
		}
	}
	private void logout(){
		if(LoginManager.appLogout(this)){
			login_IdEdit.setText("");
			login_IdEdit.setEnabled(true);
			login_PwEdit.setText("");
			login_PwEdit.setEnabled(true);
			setDisplayState(0);
			showDialog(4);
		}
	}

	public void setDisplayState(int state){
		switch(state){
		case 0:
			//NLOGIN
			m_login_ok_btn.setVisibility(View.VISIBLE);
//			m_login_out_btn.setVisibility(View.INVISIBLE);
			break;
		case 1:
			//LOGIN
			m_login_ok_btn.setVisibility(View.INVISIBLE);
//			m_login_out_btn.setVisibility(View.VISIBLE);
			Intent i = new Intent(this, LoginMainActivity.class);
			startActivity(i);
			finish();
			break;
		case 2:
			//AUTOLOGIN_SELECTED
			mAuto_login_off.setVisibility(View.INVISIBLE);
			mAuto_login_on.setVisibility(View.VISIBLE);
			break;
		case 3:
			//AUTOLOGIN_NOTSELECTED
			mAuto_login_off.setVisibility(View.VISIBLE);
			mAuto_login_on.setVisibility(View.INVISIBLE);
			break;
//		case 4:
//			//IDSAVE_SELECTED
//			mId_save_on.setVisibility(View.INVISIBLE);
//			mId_save_off.setVisibility(View.VISIBLE);
//			break;
//		case 5:
//			//IDSAVE_NOTSELECTED
//			mId_save_on.setVisibility(View.VISIBLE);
//			mId_save_off.setVisibility(View.INVISIBLE);
//			break;
		default:
			break;
		}
	}



	@Override
	protected Dialog onCreateDialog(int id) {
    	if (id ==1){
    		return new AlertDialog.Builder(this)
    		.setIcon(R.drawable.logo_small)
    		.setTitle(" ")
    	    .setMessage("아이디 / 비밀번호를 입력해 주세요")
    	    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
    	        @Override
				public void onClick(DialogInterface dialog, int whichButton) {
    	        	    	            /* User clicked OK so do some stuff */
    	        }
    	    })
    	    .create();
    	}else if(id == 2){
    		return new AlertDialog.Builder(this)
    		.setIcon(R.drawable.logo_small)
    	    .setTitle(" ")
    	    .setMessage("로그인을 성공 했습니다")
    	    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
    	        @Override
				public void onClick(DialogInterface dialog, int whichButton) {
    	        	hideKeyBoard();
    	        	progressmanager.stopProgress();
    	        	setResult(RESULT_OK);
    	        	startWebviewActivity();
    				finish();
 //    				Intent intent = new Intent( this ,MyMainActivity.class);
//    				//intent.putExtra("LOGIN_SUCCESS", "LOGIN_SUCCESS");
//    	   			MyActivityGgoup.group.back();
    	        }
    	    })
    	    .create();
    	}else if(id == 3){
    		return new AlertDialog.Builder(this)
    		.setIcon(R.drawable.logo_small)
    		.setTitle(" ")
    	    .setMessage("로그인을 실패했습니다. 다시 입력해 주세요")
    	    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
    	        @Override
				public void onClick(DialogInterface dialog, int whichButton) {

    	        }
    	    })
    	    .create();
    	}else if(id == 4){
    		return new AlertDialog.Builder(this)
    		.setIcon(R.drawable.logo_small)
    	    .setTitle("로그아웃 되었습니다.")
    	    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
    	        @Override
				public void onClick(DialogInterface dialog, int whichButton) {

    	        }
    	    })
    	    .create();
    	}else if(id == 5){
    		return new AlertDialog.Builder(this)
    	    .setIcon(null)
    	    .setTitle("로그인 안되어 있음")
    	    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
    	        @Override
				public void onClick(DialogInterface dialog, int whichButton) {

    	        }
    	    })
    	    .create();
    	}


    	return null;
    }
	private void keyBoardState(){
		final SoftKeyboardDectectorView softKeyboardDecector = new SoftKeyboardDectectorView(this);
		addContentView(softKeyboardDecector, new FrameLayout.LayoutParams(-1, -1));

		softKeyboardDecector.setOnShownKeyboard(new OnShownKeyboardListener() {

		    @Override
		    public void onShowSoftKeyboard() {
		        //키보드 등장할 때
		    	ab.setVisibility(View.GONE);
		    }
		});

		softKeyboardDecector.setOnHiddenKeyboard(new OnHiddenKeyboardListener() {

		    @Override
		    public void onHiddenSoftKeyboard() {
		        // 키보드 사라질 때
		    	ab.setVisibility(View.VISIBLE);
		    }
		});


	}
	public void backButtonPressed(View view) {
		finish();

	}
	private void hideKeyBoard(){
		InputMethodManager mgr = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(login_PwEdit.getWindowToken(), 0);
		mBottom_tab.setVisibility(View.VISIBLE);
	}

	private void stripUnderlines(TextView textView) {
        Spannable s = (Spannable)textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

	private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setFakeBoldText(true);
        }
    }

	private void startWebviewActivity() {

		Set<String> categories = getIntent().getCategories();
		if(categories != null && categories.contains(Intent.CATEGORY_BROWSABLE)) {
			Intent intent = new Intent(this, WebViewActivity.class);
			String url = Const.URL_ADDRESS + getString(R.string.act_main);
			intent.putExtra("URL",url );
			startActivity(intent);
		}
	}


	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	setResult(RESULT_CANCELED);
	         finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}*/


}
