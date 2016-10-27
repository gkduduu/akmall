package com.ak.android.akmall.my;

import java.util.List;
import java.util.Map;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.util.NetworkUtil;
import com.ak.android.akmall.util.SharedUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NotiSetView extends RelativeLayout implements OnClickListener{
	
	private TextView header_title;
	private TextView check_time_string;

	private LinearLayout check_time_layout;
	private LinearLayout sound_set_layout;
	
	
	private TextView notset_text_01;
	private TextView notset_text_02;
	private TextView notset_text_03;
	private TextView notset_text_04;
	private TextView sound_set_text;
	
	private Button btnPrev;
	
	private String mStart_hh = "";
	private String mEnd_hh = "";
	
	private List<Map<String, String>> denyList = null;
	private List<Map<String, String>> denyTimeList = null;
	private List<Map<String, String>> sepcialList = null;
	
	private Button button1_on;
	private Button button1_off;
	
	private Button button2_on;
	private Button button2_off;
	
	private Button button3_on;
	private Button button3_off;
	
	private Button button4_on;
	private Button button4_off;
	
	private Button time_check_on;
	private Button time_check_off;
	
	private String birthday = "";
	private String wedding_date = "";
	private String solar_gb = "";
	
	public NotiSetView(Context context) {
		super(context);
		View layout;
		layout = (View) LayoutInflater.from(context).inflate(R.layout.aknotiset, null);
		this.addView(layout);
		initialView();
		init();
	}
	
	private void initialView() {
		//setup title
		this.header_title = (TextView)findViewById(R.id.header_title);
		this.header_title.setText(R.string.alarm_setting);
		
		//this.notset_text_01 = (TextView)findViewById(R.id.notset_text_01);
		this.notset_text_02 = (TextView)findViewById(R.id.notset_text_02);
		//this.notset_text_03 = (TextView)findViewById(R.id.notset_text_03);
		//this.notset_text_04 = (TextView)findViewById(R.id.notset_text_04);
		this.sound_set_text = (TextView)findViewById(R.id.sound_set_text);
		
		this.btnPrev = (Button)findViewById(R.id.btnPrev);
		this.btnPrev.setOnClickListener(this);
		
		//this.button1_on = (Button)findViewById(R.id.button_on_1);
		//this.button1_off = (Button)findViewById(R.id.button_off_1);
		//this.button1_on.setOnClickListener(this);
		//this.button1_off.setOnClickListener(this);
		
		this.button2_on = (Button)findViewById(R.id.button_on_2);
		this.button2_off = (Button)findViewById(R.id.button_off_2);
		this.button2_on.setOnClickListener(this);
		this.button2_off.setOnClickListener(this);
		
		//this.button3_on = (Button)findViewById(R.id.button_on_3);
		//this.button3_off = (Button)findViewById(R.id.button_off_3);
		//this.button3_on.setOnClickListener(this);
		//this.button3_off.setOnClickListener(this);
		
		//this.button4_on = (Button)findViewById(R.id.button_on_4);
		//this.button4_off = (Button)findViewById(R.id.button_off_4);
		//this.button4_on.setOnClickListener(this);
		//this.button4_off.setOnClickListener(this);
		
		this.time_check_on = (Button)findViewById(R.id.time_check_on);
		this.time_check_off = (Button)findViewById(R.id.time_check_off);
		this.time_check_on.setOnClickListener(this);
		this.time_check_off.setOnClickListener(this);
		
		this.check_time_layout = (LinearLayout)findViewById(R.id.check_time_layout);
		this.check_time_layout.setOnClickListener(this);
		
		this.check_time_string = (TextView)findViewById(R.id.check_time_string);

		this.sound_set_layout = (LinearLayout)findViewById(R.id.sound_set_layout);
		this.sound_set_layout.setOnClickListener(this);
	}
	
	private void init() {
		networkCheck();
//		updateDeviceUid();		
		loadAlarmType();
	}
	
	private void updateDeviceUid() {
		
		if(NetworkUtil.NETWORK_NONE != NetworkUtil.getStatus(AkMallActivity.mainActivity)) {
			AkMallAPI.updateDeviceUserId(AkMallActivity.mainActivity);
		} else {
			AkMallFacade.showNetworkErrorDialog(AkMallActivity.mainActivity);
		}
	}
	
	private void networkCheck() {
		int data_info = NetworkUtil.getStatus(AkMallActivity.mainActivity);
		if (data_info == NetworkUtil.NETWORK_NONE) {
			NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
		} else {
			getData();
			if(!validAlarmData()) {
				AkMallAPI.deviceRegister(AkMallActivity.mainActivity);
				getData();
				//notiTimeSet();//p65458 20150722 test 
			}
			notiTimeSet(); 
			//setListAdapter();
		}
	}
	
	public void notiTimeSet() {
		// getData();

		if (denyTimeList != null && denyTimeList.size() > 0) {

			for (int i = 0; i < denyTimeList.size(); i++) {
				Map<String, String> map = (Map<String, String>) denyTimeList.get(i);
				mStart_hh = map.get("START_HH").toString();
				mEnd_hh = map.get("END_HH").toString();
			}

			int starthhh = 0;
			int endhhh = 0;
			String mStart_hhh = "";
			String mEnd_hhh = "";

			mStart_hhh = mStart_hh;
			mEnd_hhh = mEnd_hh;

			starthhh = Integer.valueOf(mStart_hhh);
			endhhh = Integer.valueOf(mEnd_hhh);

			if (starthhh > 12 && starthhh <= 24) {
				starthhh = starthhh - 12;
			} else if (endhhh > 12 && endhhh <= 24) {
				endhhh = endhhh - 12;
			}

			if (starthhh < 10) {
				mStart_hhh = "am " + starthhh;
			}
			if (endhhh < 10) {
				mEnd_hhh = "am " + endhhh;
			}
			if (starthhh >= 10) {
				mStart_hhh = "pm " + starthhh;
			}
			if (endhhh >= 10) {
				mEnd_hhh = "pm " + endhhh;
			}

			if (!mStart_hh.equals("00") && !mEnd_hh.equals("00")) {
				time_check_on.setVisibility(View.VISIBLE);
				time_check_off.setVisibility(View.GONE);

				if (mStart_hh != null)
					check_time_string.setTextColor(Color.parseColor("#e10064"));
					check_time_string.setText(mStart_hhh + ": 00 ~ " + mEnd_hhh + ": 00");
			}

		}

	}
	
	private boolean validAlarmData() {
		return denyList != null && denyList.size() > 4
				&& sepcialList != null
				&& denyTimeList != null && denyTimeList.size() > 0;
	}
	
	private void getData() {
		
		Map<String, List<Map<String, String>>> dataMap = AkMallAPI.getAlarmDenyList(AkMallActivity.mainActivity);
		if(dataMap != null && dataMap.size() > 0) {
			denyList = dataMap.get("DENY");
			sepcialList = dataMap.get("SPECIALDAY");
			denyTimeList = dataMap.get("DENYTIME");
		}
		
		
		
		Boolean isLogin = AkMallAPI.isLogin(AkMallActivity.mainActivity);
		
		// 알림설정, 결혼기념일 알림
		if(sepcialList != null && sepcialList.size() > 0) {
			Map<String, String> specialDay = sepcialList.get(0);
			birthday = specialDay.get("BIRTHDAY");
			wedding_date = specialDay.get("WEDDING_DATE");
			solar_gb = specialDay.get("SOLAR_GB");

			if("moon".equals(solar_gb)) {
				solar_gb = AkMallActivity.mainActivity.getString(R.string.solar_calendar);
			} else {
				solar_gb = AkMallActivity.mainActivity.getString(R.string.lunar_calendar);
			}
		}
		
		// DENY값이 0이면 on 1 이면 off 셋팅
		if(denyList != null && denyList.size() > 0) {
			for(int i=0;i<denyList.size();i++){
				Map<String, String> info = denyList.get(i);
				
				//아싸특가
				/*
				if(info.get("EVENT_ID").equals("5")){
					if(info.get("DENY").equals("0")){
						this.button1_on.setVisibility(View.VISIBLE);
						this.button1_off.setVisibility(View.GONE);
						this.notset_text_01.setVisibility(View.GONE);
					}
					else{
						this.button1_on.setVisibility(View.GONE);
						this.button1_off.setVisibility(View.VISIBLE);
					}
				}*/
				
				//New 이벤트 소식
				if(info.get("EVENT_ID").equals("3")){
					if(info.get("DENY").equals("0")){
						this.button2_on.setVisibility(View.VISIBLE);
						this.button2_off.setVisibility(View.GONE);
						this.notset_text_02.setVisibility(View.GONE);
					}
					else{
						this.button2_on.setVisibility(View.GONE);
						this.button2_off.setVisibility(View.VISIBLE);
					}
				}
				
				//생일알림
				/*
				if(info.get("EVENT_ID").equals("1")){
					if(info.get("DENY").equals("0")){ // && isLogin추가
						this.button3_on.setVisibility(View.VISIBLE);
						this.button3_off.setVisibility(View.GONE);
						this.notset_text_03.setText(birthday+" (" + solar_gb + ")");
					}
					else{
						this.button3_on.setVisibility(View.GONE);
						this.button3_off.setVisibility(View.VISIBLE);
					}
				}*/
				
				//결혼기념일
				/*
				if(info.get("EVENT_ID").equals("2")){
					if(info.get("DENY").equals("0")){
						this.button4_on.setVisibility(View.VISIBLE);
						this.button4_off.setVisibility(View.GONE);
						this.notset_text_04.setText(wedding_date);
					}
					else{
						this.button4_on.setVisibility(View.GONE);
						this.button4_off.setVisibility(View.VISIBLE);
					}
				}*/
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		int data_info = NetworkUtil.getStatus(AkMallActivity.mainActivity);
		
		switch (v.getId()) {
		case R.id.btnPrev:
			AkMallActivity.showWebView();
			break;
		/*
		case R.id.button_on_1:
			//아싸특가 ON --> OFF
			if (data_info == 2)
				NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
			
			if (setData("5", "1")) {
				this.button1_on.setVisibility(View.GONE);
				this.button1_off.setVisibility(View.VISIBLE);
				this.notset_text_01.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.button_off_1:
			//아싸특가 OFF --> ON			
			if (data_info == 2)
				NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
			
			if (setData("5", "0")) {
				this.button1_on.setVisibility(View.VISIBLE);
				this.button1_off.setVisibility(View.GONE);
				this.notset_text_01.setVisibility(View.GONE);
			}
			break;	
		*/
		
		case R.id.button_on_2:
			//New 이벤트 소식	 ON --> OFF
			if (data_info == 2)
				NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
			
			if (setData("3", "1")) {
				this.button2_on.setVisibility(View.GONE);
				this.button2_off.setVisibility(View.VISIBLE);
				this.notset_text_02.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.button_off_2:
			//New 이벤트 소식	 OFF --> ON
			if (data_info == 2)
				NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
			
			if (setData("3", "0")) {
				this.button2_on.setVisibility(View.VISIBLE);
				this.button2_off.setVisibility(View.GONE);
				this.notset_text_02.setVisibility(View.GONE);
			}
			break;
		/*
		case R.id.button_on_3:
			////생일알림	 ON --> OFF
			if (data_info == 2)
				NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
			
			if (setData("1", "1")) {
				this.button3_on.setVisibility(View.GONE);
				this.button3_off.setVisibility(View.VISIBLE);
				this.notset_text_03.setText(R.string.none_setting);
			}
			break;
		case R.id.button_off_3:
			////생일알림	 OFF --> ON
			if (data_info == 2)
				NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
				
				if(!AkMallAPI.isLogin(AkMallActivity.mainActivity)) {
					AkMallFacade.startLoginActivityForResult(AkMallActivity.mainActivity);
					return;
				}

				if (birthday == null || birthday.length() == 0){
					showDialogs(2);
				} else {
					if (setData("1", "0")) {
						this.button3_on.setVisibility(View.VISIBLE);
						this.button3_off.setVisibility(View.GONE);
						this.notset_text_03.setText(birthday + " (" + solar_gb + ")");
					}
				}
		case R.id.button_on_4:
			if (setData("2", "1")) {
				this.button4_on.setVisibility(View.GONE);
				this.button4_off.setVisibility(View.VISIBLE);
				this.notset_text_04.setText(R.string.none_setting);
			}
			break;
		case R.id.button_off_4:
			if(!AkMallAPI.isLogin(AkMallActivity.mainActivity)) {
				AkMallFacade.startLoginActivityForResult(AkMallActivity.mainActivity);
				return;
			}

			if (wedding_date == null || wedding_date.length() == 0) {
				showDialogs(3);
			} else {
				if (setData("2", "0")) {
					this.button4_on.setVisibility(View.VISIBLE);
					this.button4_off.setVisibility(View.GONE);
					this.notset_text_04.setText(R.string.none_setting);
				}
			}
			break;
		*/
		case R.id.time_check_on:
			if(setNotiTime("00", "00")) {
				notiTimeSet2();
			} else {
				Toast.makeText(AkMallActivity.mainActivity, R.string.noti_deny_set_fail, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.time_check_off:
			AkMallActivity.showNotiTimeSetView();
			break;
		case R.id.check_time_layout:
			AkMallActivity.showNotiTimeSetView();
			break;
		case R.id.sound_set_layout:
			AkMallActivity.showNotiSetSoundView();
			break;
		}
	}
	
	public void notiTimeSet2() {
		time_check_on.setVisibility(View.GONE);
		time_check_off.setVisibility(View.VISIBLE);
		check_time_string.setText(R.string.none_setting);
	}
	
	
	private boolean setNotiTime(String start_hh, String end_hh) {
		return  AkMallAPI.saveNotiTime(AkMallActivity.mainActivity, start_hh, end_hh);
	}
	
	private boolean setData(String eventId, String deny) {
		return AkMallAPI.updateDeny(AkMallActivity.mainActivity, eventId, deny);
	}
	
	private void showDialogs(int type) {
		switch (type) {
		case 2:
			new AlertDialog.Builder(AkMallActivity.mainActivity).setIcon(R.drawable.logo_small).setTitle(" ")
					.setMessage("생일 정보가 없습니다").setPositiveButton("확인", null).show();
			break;
		case 3:
			new AlertDialog.Builder(AkMallActivity.mainActivity).setIcon(R.drawable.logo_small).setTitle(" ")
					.setMessage("결혼기념일 정보가 없습니다").setPositiveButton("확인", null).show();
			break;
		}

	}
	
	private void loadAlarmType() {
		String type = SharedUtil.getSharedString(AkMallActivity.mainActivity, "ALARM", "type");
		if (type.equals("no")) {
			sound_set_text.setText(R.string.silent);
		} else if (type.equals("vi")) {
			sound_set_text.setText(R.string.vibration);
		} else if (type.equals("so")) {
			sound_set_text.setText(R.string.sound);
		} else {
			sound_set_text.setText(R.string.sound);
		}
	}
}
