/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ak.android.akmall.my;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.common.PreferenceFacade;
import com.ak.android.akmall.common.ProgressBarManager;
import com.ak.android.akmall.util.NetworkUtil;
import com.ak.android.akmall.util.SharedUtil;
import com.ak.android.akmall.util.StringUtil;
import com.ak.android.akmall.widget.NavigationTabView;
import com.mtracker.mea.sdk.MTrackerManager;//p65458 20150716 mtracker 연동 add

public class LoginActivity extends Activity implements OnClickListener {

	public static final String TAG = "LoginActivity";

	private ProgressBarManager progressmanager = null;

	private Button mAuto_login_off = null; // 자동로그인
	private Button mAuto_login_on = null; // 자동로그인
	private Button mId_save_on = null; // ID 저장
	private Button mId_save_off = null; // ID 저장
	private Button m_login_ok_btn = null; // 로그인 버튼
	private Button m_login_out_btn = null; // 로그아웃 버튼
	private EditText login_IdEdit = null;
	private EditText login_PwEdit = null;
	private boolean mUserFlag = false;
	private Button mBtnSignup = null; // 간단 회원가입

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		initialView();
		init();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		MTrackerManager.startActivityAnalyze(this);//p65458 20150716 mtracker 연동 add
		MTrackerManager.startActivityAnalyze(this, "AIzaSyCVzqZfdndlsivLh83zoe2Uol9MvOwtl4I");//p65458 20150720 mtracker 연동 add
		super.onResume();
	}

	private void initialView() {
		setContentView(R.layout.akloginset);

		mAuto_login_off = (Button) findViewById(R.id.auto_login_off);
		mAuto_login_on = (Button) findViewById(R.id.auto_login_on);
		mId_save_on = (Button) findViewById(R.id.id_save_on);
		mId_save_off = (Button) findViewById(R.id.id_save_off);
		m_login_ok_btn = (Button) findViewById(R.id.login_ok_btn);
		m_login_out_btn = (Button) findViewById(R.id.login_out_btn);
		login_IdEdit = (EditText) findViewById(R.id.login_id);
		login_PwEdit = (EditText) findViewById(R.id.login_pw);

		login_IdEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		login_PwEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		PasswordTransformationMethod passwdtm = new PasswordTransformationMethod();
		login_PwEdit.setTransformationMethod(passwdtm);
		mBtnSignup = (Button) findViewById(R.id.btn_signup);

		// setup title
		((TextView) findViewById(R.id.header_title)).setText(R.string.login_setting);
		findViewById(R.id.btnPrev).setOnClickListener(this);
		//setupTab();

	}

	private void setupTab() {
		NavigationTabView tab = (NavigationTabView) findViewById(R.id.bottomNaviTab);
		tab.setBackwardEnabled(true).setForwardEnabled(false).setScrollTopEnabled(false)
				.setBackwardOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		progressmanager.stopProgress();
		super.onPause();
	}

	private void init() {
		setListener();
		getIntentData(); // <- 아래가 아니라 여기에서 실행되야함.
		checkLoginState();

		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.loginset);
		progressmanager = null;
		progressmanager = new ProgressBarManager(this);
		progressmanager.ableKeyEvent(false);
		String cmd[] = { "", "login", "progressFinish" };
		progressmanager.setCommandClass(this, cmd, null);
		mainLayout.addView(progressmanager);
	}

	public void progressFinish() {
		progressmanager.stopProgress();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		String extra = intent.getStringExtra("UPDATEUSER");
		if (extra != null) {
			mUserFlag = true;
		}
	}

	private void setListener() {
		m_login_ok_btn.setOnClickListener(this);
		m_login_out_btn.setOnClickListener(this);
		mAuto_login_off.setOnClickListener(this);
		mAuto_login_on.setOnClickListener(this);
		mId_save_on.setOnClickListener(this);
		mId_save_off.setOnClickListener(this);
		mBtnSignup.setOnClickListener(this);
	}

	private void checkLoginState() {

		if (PreferenceFacade.isAutoLogin(this)) {
			setDisplayState(2);

		} else if (PreferenceFacade.isSaveId(this)) {
			setDisplayState(5);
			login_IdEdit.setText(PreferenceFacade.getSavededUserId(this));
		}
	}

	@Override
	public void onClick(View v) {

		int data_info = NetworkUtil.getStatus(this);

		switch (v.getId()) {
		case R.id.btnPrev:
			finish();
			break;

		case R.id.login_ok_btn:
			if (data_info == 2) {
				NetworkUtil.showNetworkErrorDialog(this);
			} else {
				// login();
				progressmanager.startProgress();
			}
			break;

		case R.id.login_out_btn:
			logout();
			break;

		case R.id.auto_login_off:
			setDisplayState(2);
			break;

		case R.id.auto_login_on:
			setDisplayState(3);
			break;

		case R.id.id_save_on:
			setDisplayState(4);
			break;

		case R.id.id_save_off:
			setDisplayState(5);
			break;

		case R.id.btn_signup:
			startSingup();
			break;
		}
	}

	private void startSingup() {

		int versionCode = 1;
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.w(TAG, "Don't get versionCode.. so default verionCode set 1..");
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		
			String url = Const.URL_BASE + getString(R.string.uri_address) + getString(R.string.act_signup)
 + getString(R.string.param_signup, versionCode);
		
			intent.setData(Uri.parse(url));
		
			startActivity(intent);
	
		}

	private void setAuto(String userid, String hashedPassword) {
		SharedUtil.clearAllSetting(this, "auto", 0);

		if (mAuto_login_on.isShown() == true) {
			PreferenceFacade.setAutoLogin(this, userid, hashedPassword);
		} else if (mId_save_on.isShown() == true) {
			PreferenceFacade.setSaveUserId(this, userid);
		}
	}

	public void login() {

		hideKeyBoard();
		String id = StringUtil.NVL(login_IdEdit.getText().toString(), "");
		String pw = StringUtil.NVL(login_PwEdit.getText().toString(), "");

		if (id.equals("") || pw.equals("")) {
			showDialog(1);
		} else {
			boolean isSuccess = false;
			pw = StringUtil.encrypString(pw);
			isSuccess = AkMallAPI.doLogin(this, id, pw);

			if (isSuccess) {
				setResult(RESULT_OK);
				setAuto(id, pw);
//				if (mUserFlag) {
//					AkMallAPI.updateDeviceUserId(getApplicationContext());
//				}
				showDialog(2);
			} else {

				showDialog(3);
			}
		}
	}

	private void logout() {
		if (AkMallAPI.doLogout(this)) {
			login_IdEdit.setText("");
			login_IdEdit.setEnabled(true);
			login_PwEdit.setText("");
			login_PwEdit.setEnabled(true);
			setDisplayState(0);
			showDialog(4);
		}
	}

	public void setDisplayState(int state) {
		switch (state) {
		case 0:
			// NLOGIN
			m_login_ok_btn.setVisibility(View.VISIBLE);
			m_login_out_btn.setVisibility(View.INVISIBLE);
			break;
		case 1:
			// LOGIN
			m_login_ok_btn.setVisibility(View.INVISIBLE);
			m_login_out_btn.setVisibility(View.VISIBLE);
			break;
		case 2:
			// AUTOLOGIN_SELECTED
			mAuto_login_off.setVisibility(View.INVISIBLE);
			mAuto_login_on.setVisibility(View.VISIBLE);
			break;
		case 3:
			// AUTOLOGIN_NOTSELECTED
			mAuto_login_off.setVisibility(View.VISIBLE);
			mAuto_login_on.setVisibility(View.INVISIBLE);
			break;
		case 4:
			// IDSAVE_SELECTED
			mId_save_on.setVisibility(View.INVISIBLE);
			mId_save_off.setVisibility(View.VISIBLE);
			break;
		case 5:
			// IDSAVE_NOTSELECTED
			mId_save_on.setVisibility(View.VISIBLE);
			mId_save_off.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			return new AlertDialog.Builder(this).setIcon(R.drawable.logo_small).setTitle(" ")
					.setMessage("아이디 / 비밀번호를 입력해 주세요")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
						}
					}).create();
		} else if (id == 2) {
			return new AlertDialog.Builder(this).setIcon(R.drawable.logo_small).setTitle(" ")
					.setMessage("로그인을 성공 했습니다")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							hideKeyBoard();
							progressmanager.stopProgress();
							finish();
							// Intent intent = new Intent( this
							// ,MyMainActivity.class);
							// //intent.putExtra("LOGIN_SUCCESS",
							// "LOGIN_SUCCESS");
							// MyActivityGgoup.group.back();
						}
					}).create();
		} else if (id == 3) {
			return new AlertDialog.Builder(this).setIcon(R.drawable.logo_small).setTitle(" ")
					.setMessage("로그인을 실패했습니다. 다시 입력해 주세요")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					}).create();
		} else if (id == 4) {
			return new AlertDialog.Builder(this).setIcon(R.drawable.logo_small)
					.setTitle("로그아웃 되었습니다.")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					}).create();
		} else if (id == 5) {
			return new AlertDialog.Builder(this).setIcon(null).setTitle("로그인 안되어 있음")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {

						}
					}).create();
		}

		return null;
	}

	private void hideKeyBoard() {
		InputMethodManager mgr = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(login_PwEdit.getWindowToken(), 0);
	}

}
