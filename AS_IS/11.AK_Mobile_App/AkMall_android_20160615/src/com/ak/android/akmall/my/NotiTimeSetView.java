package com.ak.android.akmall.my;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.util.NetworkUtil;
import com.ak.android.akmall.util.SharedUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotiTimeSetView extends RelativeLayout implements OnClickListener{
	
	private TextView header_title;
	private TextView start_time_text;
	private TextView end_time_text;
	private Button btnPrev;
	private Button btnAction;
	
	private Button button_1;
	private Button button_2;
	
	private int mSelectedTime = 0;
	private String mStarthh = "";
	private String mEndhh = "";
	private String mStarthhh = "";
	private String mEndhhh = "";
	
	
	public NotiTimeSetView(Context context) {
		super(context);
		View layout;
		layout = (View) LayoutInflater.from(context).inflate(R.layout.aknotitimesetview, null);
		this.addView(layout);
		initialView();
	}

	private void initialView() {
		//setup title
		this.header_title = (TextView)findViewById(R.id.header_title);
		this.header_title.setText(R.string.noti_deny_time_setting);
		
		this.start_time_text = (TextView)findViewById(R.id.start_time_text);
		this.end_time_text = (TextView)findViewById(R.id.end_time_text);
		
		this.btnPrev = (Button)findViewById(R.id.btnPrev);
		this.btnPrev.setOnClickListener(this);
		
		this.btnAction = (Button)findViewById(R.id.btnAction);
		this.btnAction.setVisibility(View.VISIBLE);
		this.btnAction.setOnClickListener(this);
		
		this.button_1  = (Button)findViewById(R.id.button_1);
		this.button_2  = (Button)findViewById(R.id.button_2);
		this.button_1.setOnClickListener(this);
		this.button_2.setOnClickListener(this);
		
	}
	
	private void saveTime(int data_info) {
		if ("".equals(mStarthh)) {
			showMessageDialog(R.string.start_time_required, false);
			return;
		}

		if ("".equals(mEndhh)) {
			showMessageDialog(R.string.end_time_required, false);
			return;
		}

		if (data_info == NetworkUtil.NETWORK_NONE) {
			NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
		} else {
			if(setNotiTime(mStarthh, mEndhh)) {
				showMessageDialog(R.string.noti_deny_time_saved, false);
				AkMallActivity.closeNotiTimeSetView();
			} else {
				showMessageDialog(R.string.noti_deny_time_save_fail, false);
			}
		}
	}
	
	private boolean setNotiTime(String starthh, String endhh) {
		return AkMallAPI.saveNotiTime(AkMallActivity.mainActivity, starthh, endhh);
	}
	
	private void showMessageDialog(int messageId, final boolean finish) {
		AkMallFacade.showMessageDialog(AkMallActivity.mainActivity, messageId, finish);
	}
	
	private void showStartTimeSelectDialog() {
		new AlertDialog.Builder(AkMallActivity.mainActivity)
					.setTitle(R.string.start_time_select)
					.setSingleChoiceItems(R.array.notiSet_time, mSelectedTime,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									mSelectedTime = which;
								}
							})
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String[] start_hh = getResources().getStringArray(
									R.array.notiSet_time_url);
							String[] start_hhh = getResources()
									.getStringArray(R.array.notiSet_time);
							mStarthh = start_hh[mSelectedTime];
							mStarthhh = start_hhh[mSelectedTime];
							start_time_text.setText(mStarthhh + " : " + "00");
							button_1.setBackgroundResource(R.drawable.user_setting_check_on);
							
							SharedUtil.setSharedString(AkMallActivity.mainActivity, "ALARM", "type",
									mStarthhh);
						}
					}).setNegativeButton(android.R.string.cancel, null).show();
	}

	private void showEndTimeSelectDialog() {
		new AlertDialog.Builder(AkMallActivity.mainActivity)
					.setTitle(R.string.end_time_select)
					.setSingleChoiceItems(R.array.notiSet_time, mSelectedTime,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									mSelectedTime = which;
								}
							})
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String[] end_hh = getResources().getStringArray(
									R.array.notiSet_time_url);
							String[] end_hhh = getResources().getStringArray(R.array.notiSet_time);

							mEndhhh = end_hhh[mSelectedTime];
							mEndhh = end_hh[mSelectedTime];
							end_time_text.setText(mEndhhh + " : " + "00");
							button_2.setBackgroundResource(R.drawable.user_setting_check_on);
							SharedUtil.setSharedString(AkMallActivity.mainActivity, "ALARM", "type",
									mEndhhh);
						}
					}).setNegativeButton(android.R.string.cancel, null).show();
	}
	
	@Override
	public void onClick(View v) {
		int data_info = NetworkUtil.getStatus(AkMallActivity.mainActivity);
		
		switch (v.getId()) {
			case R.id.btnPrev:
				AkMallActivity.closeNotiTimeSetView();
				break;
				
			case R.id.btnAction:
				saveTime(data_info);
				break;
				
			case R.id.button_1:
				showStartTimeSelectDialog();
				break;
				
			case R.id.button_2:
				showEndTimeSelectDialog();
				break;
				
		}
	}
	
}
