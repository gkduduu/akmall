package com.ak.android.akplaza.intro;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.AkPlazaAPI;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.LoginManager;
import com.ak.android.akplaza.common.NetworkUtil;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.WebViewActivity;
import com.igaworks.IgawCommon;


public class IntroActivity extends Activity {

     public static final String TAG ="IntroActivity";

     private static final int DIALOG_DATA_INFO = 0;
     private static final int DIALOG_NO_CONNECTION = 1;
     private static final int DIALOG_PUSH_CONFIRM = 2;

	 private ImageView animation = null;
	 private AnimationDrawable ani = null;
	 private Context mContext = null;
	 private String mNetworkStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.intro);
	    mContext = this;
	    animation = (ImageView)this.findViewById(R.id.introanimation);
	    CookieSyncManager.createInstance(this);
	    CookieManager.getInstance().removeSessionCookie();
	    
	    IgawCommon.startApplication(IntroActivity.this);//p65458 20150716 IgawCommon add sdk
		String deviceID = getDeviceID();
		IgawCommon.setUserId(deviceID.replace("-", ""));
	}

	@Override
	protected void onResume() {
		IgawCommon.startSession(this);		
		mNetworkStatus =  NetworkUtil.checkStatus(this);
		
		if(mNetworkStatus.equals("NONE")) {

			showDialogs(DIALOG_NO_CONNECTION);
			// after finish..

		} else if(mNetworkStatus.equals("WIFI")) {
			checkPushConfrim();
		} else { // 3g, 4g..
			showDialogs(DIALOG_DATA_INFO);
		}
		
		
		
		super.onResume();
	}

	private void checkPushConfrim() {
		String push = SharedUtil.getSharedString(this, "DEVICE", "push");
		if(push.length() == 0) {
			showDialogs(DIALOG_PUSH_CONFIRM);
		} else {
			c2dmRegister();
			initialize();
		}
	}

	public void autoLoginCheck(){
		LoginManager.appAutoLogin(this);
	}

	private void initialize() {
		Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {

				AkPlazaAPI api = new AkPlazaAPI(mContext);
				api.signinDevice();

				autoLoginCheck();

				Intent dataintent = getIntent();
				String event = dataintent.getStringExtra("EVENT");
				String pid = dataintent.getStringExtra("PID");
				String bc = dataintent.getStringExtra("BC");

				String url = Const.URL_ADDRESS + getString(R.string.act_main);
				Intent intent = new Intent(IntroActivity.this, WebViewActivity.class);
				intent.putExtra("URL",url );

				if(event!=null){
					intent.putExtra("EVENT",event );
				}
				if(pid!=null){
					intent.putExtra("PID",pid );
				}
				intent.putExtra("BC", bc);

				if(ani != null) {
					ani.stop();
				}
				startActivity(intent);
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 3000);
	}

	public void c2dmRegister(){
		String regiStr = SharedUtil.getSharedString(this, "C2DM", "token");
		if( regiStr.equals("") || regiStr.equals(null) ){
			Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0) ); // 어플리케이션ID
		registrationIntent.putExtra("sender", "wwworld0927@gmail.com"); // 개발자ID
		startService(registrationIntent);
		}
	}


    @Override
	public void onWindowFocusChanged (boolean hasFocus) {
       Log.d("hasFocus", hasFocus +"");
        if(hasFocus == true){
        animation.setBackgroundResource(R.drawable.intro_animation);
//        	animation.setBackgroundResource(R.drawable.default1);
//        	animation.setAlpha(1);
        ani = (AnimationDrawable)animation.getBackground();

	        animation.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ani.start();//정상적으로 애니메이션이 구현되는예제..
				}
			});
        }
//        else{
//        	ani.stop();
//        }
    }
	public void showDialogs(int i ){
		switch (i) {
		case DIALOG_DATA_INFO:
			new AlertDialog.Builder(this)
			.setTitle("데이터 통화료 안내")
			.setMessage(R.string.data_info)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					checkPushConfrim();
				}
			})
			.setNegativeButton("종료", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.show();
			break;
		case DIALOG_NO_CONNECTION:
			new AlertDialog.Builder(this)
			.setTitle("알림")
			.setMessage(R.string.network_error)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.show();
			break;
		case DIALOG_PUSH_CONFIRM:
			new AlertDialog.Builder(this)
			.setTitle(" ")
			.setIcon(R.drawable.logo_small)
			.setMessage(R.string.pushRegistMeaage)
			.setPositiveButton("승인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedUtil.setSharedString(mContext, "DEVICE", "push", "on");
					c2dmRegister();
					initialize();
				}
			})
			.setNegativeButton("허용 안 함", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedUtil.setSharedString(mContext, "DEVICE", "push", "off");
					c2dmRegister();
					initialize();
				}
			})
			.setCancelable(false)
			.show();

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("back", "back");
			return false;
		}
		return false;
	}
	
	private String getDeviceID()
    {
    	final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(this.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        
        Log.d("Test", "==>ID=" + deviceId);
        return deviceId;        
    }

}