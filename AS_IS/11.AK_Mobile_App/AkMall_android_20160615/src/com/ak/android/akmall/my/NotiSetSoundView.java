package com.ak.android.akmall.my;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.util.SharedUtil;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotiSetSoundView extends RelativeLayout implements OnClickListener{
	
	private TextView header_title;
	private Button btnPrev;
	
	private Button mSilentCheck_On;
	private Button mSilentCheck_Off;
	
	private Button mShakeCheck_On;
	private Button mShakeCheck_Off;
	
	private Button mSoundCheck_On;
	private Button mSoundCheck_Off;
	
	public NotiSetSoundView(Context context) {
		super(context);
		View layout;
		layout = (View) LayoutInflater.from(context).inflate(R.layout.aknotisetsoundview, null);
		this.addView(layout);
		initialView();
	}
	
	private void initialView() {
		//setup title
		this.header_title = (TextView)findViewById(R.id.header_title);
		this.header_title.setText(R.string.my_alram_12);

		this.btnPrev = (Button)findViewById(R.id.btnPrev);
		this.btnPrev.setOnClickListener(this);
		
		this.mSilentCheck_On = (Button) findViewById(R.id.mSilentCheck_On);
		this.mShakeCheck_On = (Button) findViewById(R.id.mShakeCheck_On);
		this.mSoundCheck_On = (Button) findViewById(R.id.mSoundCheck_On);

		this.mSilentCheck_On.setOnClickListener(this);
		this.mShakeCheck_On.setOnClickListener(this);
		this.mSoundCheck_On.setOnClickListener(this);

		this.mSilentCheck_Off = (Button) findViewById(R.id.mSilentCheck_Off);
		this.mShakeCheck_Off = (Button) findViewById(R.id.mShakeCheck_Off);
		this.mSoundCheck_Off = (Button) findViewById(R.id.mSoundCheck_Off);

		this.mSilentCheck_Off.setOnClickListener(this);
		this.mShakeCheck_Off.setOnClickListener(this);
		this.mSoundCheck_Off.setOnClickListener(this);
		
		setAlarmType();
	}
	
	private void setAlarmType() {
		String type = SharedUtil.getSharedString(AkMallActivity.mainActivity, "ALARM", "type");
		
		if (type.equals("no")) {
			mSilentCheck_On.setVisibility(View.VISIBLE);
			mSilentCheck_Off.setVisibility(View.GONE);
			mShakeCheck_On.setVisibility(View.GONE);
			mShakeCheck_Off.setVisibility(View.VISIBLE);
			mSoundCheck_On.setVisibility(View.GONE);
			mSoundCheck_Off.setVisibility(View.VISIBLE);
		} else if (type.equals("vi")) {
			mSilentCheck_On.setVisibility(View.GONE);
			mSilentCheck_Off.setVisibility(View.VISIBLE);
			mShakeCheck_On.setVisibility(View.VISIBLE);
			mShakeCheck_Off.setVisibility(View.GONE);
			mSoundCheck_On.setVisibility(View.GONE);
			mSoundCheck_Off.setVisibility(View.VISIBLE);
		} else if (type.equals("so")) {
			mSilentCheck_On.setVisibility(View.GONE);
			mSilentCheck_Off.setVisibility(View.VISIBLE);
			mShakeCheck_On.setVisibility(View.GONE);
			mShakeCheck_Off.setVisibility(View.VISIBLE);
			mSoundCheck_On.setVisibility(View.VISIBLE);
			mSoundCheck_Off.setVisibility(View.GONE);
		} else {
			setVisility(2);
		}
	}
	
	public void setVisility(int i) {
		switch (i) {
		case 0:
			// silent check_on/off
			mSilentCheck_On.setVisibility(View.VISIBLE);
			mSilentCheck_Off.setVisibility(View.GONE);
			mShakeCheck_On.setVisibility(View.GONE);
			mShakeCheck_Off.setVisibility(View.VISIBLE);
			mSoundCheck_On.setVisibility(View.GONE);
			mSoundCheck_Off.setVisibility(View.VISIBLE);
			break;
		case 1:
			mSilentCheck_On.setVisibility(View.GONE);
			mSilentCheck_Off.setVisibility(View.VISIBLE);
			mShakeCheck_On.setVisibility(View.VISIBLE);
			mShakeCheck_Off.setVisibility(View.GONE);
			mSoundCheck_On.setVisibility(View.GONE);
			mSoundCheck_Off.setVisibility(View.VISIBLE);

			break;
		case 2:
			// shake check
			mSilentCheck_On.setVisibility(View.GONE);
			mSilentCheck_Off.setVisibility(View.VISIBLE);
			mShakeCheck_On.setVisibility(View.GONE);
			mShakeCheck_Off.setVisibility(View.VISIBLE);
			mSoundCheck_On.setVisibility(View.VISIBLE);
			mSoundCheck_Off.setVisibility(View.GONE);
			break;

		}

	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
			case R.id.btnPrev:
				AkMallActivity.closeNotiSetSoundView();
				break;
				
			case R.id.mSilentCheck_On:
				setVisility(0);
				break;
			case R.id.mSilentCheck_Off:
				setVisility(0);
				SharedUtil.setSharedString(AkMallActivity.mainActivity, "ALARM", "type", "no");
				break;
				
			case R.id.mShakeCheck_On:
				setVisility(1);
				break;
			case R.id.mShakeCheck_Off:
				setVisility(1);
				platVibrator();
				SharedUtil.setSharedString(AkMallActivity.mainActivity, "ALARM", "type", "vi");
				break;
				
			case R.id.mSoundCheck_On:
				setVisility(2);
				break;
			case R.id.mSoundCheck_Off:
				setVisility(2);
				playSound();
				SharedUtil.setSharedString(AkMallActivity.mainActivity, "ALARM", "type", "so");
				break;
				
		}
	}
	
	private void platVibrator() {
		Vibrator m_clsVibrator = (Vibrator) AkMallActivity.mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		m_clsVibrator.vibrate(500);
	}
	
	private void playSound() {
		MediaPlayer mp = MediaPlayer.create(AkMallActivity.mainActivity, R.raw.sound0);
		mp.setVolume(1000, 1000);
		mp.start();
	}
}
