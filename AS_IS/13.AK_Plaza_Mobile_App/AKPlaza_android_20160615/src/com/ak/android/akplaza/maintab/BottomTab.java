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
package com.ak.android.akplaza.maintab;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.alarm.NotiBoxActivity;
import com.ak.android.akplaza.common.BadgeView;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.NetworkUtil;
import com.ak.android.akplaza.common.PostHttpClient;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.WebViewActivity;
import com.ak.android.akplaza.widget.AkPlazaWebView;


public class BottomTab extends LinearLayout implements OnClickListener{

	public static final int TAB_HOME = 0;

	private Context mContext = null;
	private LayoutInflater inflater	= null;
	private View convertView = null;
	private static int currentTab = 0;

	private Button mtab_home = null;
    private Button mtab_myak = null;
    private Button mtab_akmall = null;
    private Button mtab_search = null;
    private Button mtab_notice = null;
    private static AkPlazaWebView mWebView = null;
    private BadgeView badge = null;

    public static void setmWebView(AkPlazaWebView mWeb) {
		mWebView = mWeb;
	}

	private static List<Button> btnList = null;

	public BottomTab(Context context) {
		super(context);
		this.mContext = context;
		setContentView();
		setTabImage();
	}

	public BottomTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setContentView();
		setTabImage();
	}

	public static void tabClear(){
		currentTab = 0;
	}

	public static void tabClearforMy(){
		currentTab = 4;
		setTabImage();
	}

	public static void setCurrentTab(int tabIndex) {
		currentTab = tabIndex;
		setTabImage();
	}

	private void setContentView(){
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.include_bottom, this, false);
		addView(convertView);

		btnList = new ArrayList<Button>();

		mtab_home = (Button)findViewById(R.id.tab_home);
		btnList.add(mtab_home);
        mtab_myak = (Button)findViewById(R.id.tab_myak);
        btnList.add(mtab_myak);
        mtab_search = (Button)findViewById(R.id.tab_search);
        btnList.add(mtab_search);
        mtab_notice = (Button)findViewById(R.id.tab_notice);
        btnList.add(mtab_notice);
        mtab_akmall = (Button)findViewById(R.id.tab_akmall);
        btnList.add(mtab_akmall);

        mtab_home.setOnClickListener(this);
        mtab_myak.setOnClickListener(this);
        mtab_search.setOnClickListener(this);
        mtab_notice.setOnClickListener(this);
        mtab_akmall.setOnClickListener(this);
        
        
        badge = new BadgeView(mContext, mtab_notice);
        
        setBadge();
        
	}
	
	public void setBadge() {
		String pushCount = "0";
		String token = SharedUtil.getSharedString(mContext, "C2DM", "token");
        String strUrl = Const.URL_LIB+"act=pushCount&token="+token;
        String result = PostHttpClient.HttpConnect(strUrl);
        try {
			JSONObject jsonResult = new JSONObject(result);
			pushCount = jsonResult.getString("count");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        badge.setText(pushCount);
        if(pushCount.equals("0")){
        	badge.hide();
        }else{
        	badge.show();
        }
	}
	
	

	private static void setTabImage(){
		for(int i = 0 ; i < btnList.size() ; i++){
			Button btn = btnList.get(i);
        	int imgRes = 0;
        	if(currentTab == i ){
        		imgRes = buttonImgs[i];
			}else{
				imgRes = buttonImg[i];
			}
        	btn.setBackgroundResource(imgRes);
		}
	}

	@Override
	public void onClick(View v) {
		String data_info = NetworkUtil.checkStatus(mContext);
		if(data_info == "NONE"){
			NetworkUtil.NetworkErrorDialog(mContext);
		}else{
			ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
			ComponentName topActivity = taskInfo.get(0).topActivity;
			String strClass = topActivity.getClassName();
			
			if(v.equals(mtab_home)){
				currentTab = 0;
//				Intent intent = new Intent(mContext,WebViewActivity.class);
//				String url = Const.URL_ADDRESS + mContext.getString(R.string.act_main);
//				intent.putExtra("URL",url );
//				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				mContext.startActivity(intent);
				
//				if (mWebView.canGoBack()) {
//		            mWebView.goBack();
//		        }
				
				new Thread(new Runnable() {         
			       public void run() {                 
			           new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			       }   
			    }).start();
			}else if(v.equals(mtab_myak)){
				currentTab = 0;
//				startMyAkActivity();
				String url = Const.URL_ADDRESS + mContext.getString(R.string.act_myak);
				mWebView.loadUrl(url);
				
				if(!strClass.equals("com.ak.android.akplaza.common.WebViewActivity"))
				{
					new Thread(new Runnable() {         
					       public void run() {                 
					           new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
					       }   
					    }).start();
				}

			}else if(v.equals(mtab_akmall)){
				currentTab = 2;

				AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
				ab.setIcon(R.drawable.logo_small).create();
				ab.setTitle(" ").create();
				ab.setMessage("AKmall로 이동 하시겠습니까?").create();
				ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri u = Uri.parse("http://m.akmall.com");
						intent.setData(u);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						mContext.startActivity(intent);
					}
				}).create();
				ab.setNegativeButton("아니오", null).create();
				ab.show();

//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				Uri u = Uri.parse("http://m.akmall.com");
//				intent.setData(u);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				mContext.startActivity(intent);

			}else if(v.equals(mtab_search)){
				currentTab = 0;
//				Intent intent = new Intent(mContext,WebViewActivity.class);
//				String url = Const.URL_ADDRESS + mContext.getString(R.string.act_searchBrand);
//				intent.putExtra("URL",url );
//				intent.putExtra("BARCORD","BARCORD" );
//				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				mContext.startActivity(intent);
				
				String url = Const.URL_ADDRESS + mContext.getString(R.string.act_searchBrand);
				mWebView.loadUrl(url);

				if(!strClass.equals("com.ak.android.akplaza.common.WebViewActivity"))
				{
					new Thread(new Runnable() {         
					       public void run() {                 
					           new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
					       }   
					    }).start();
				}
				
			}else if(v.equals(mtab_notice)){
//					currentTab = 4;
//					Intent intent = new Intent(mContext,WebViewActivity.class);
//					String url = Const.URL_ADDRESS + mContext.getString(R.string.act_certificate);
//					intent.putExtra("URL",url );
//					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					mContext.startActivity(intent);
				currentTab = 4;
				Intent intent = new Intent(mContext, NotiBoxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent);
			}
		}
	}

	private void startMyAkActivity() {
		Intent intent = new Intent(mContext,WebViewActivity.class);
		String url = Const.URL_ADDRESS + mContext.getString(R.string.act_myak);
		intent.putExtra("URL",url );
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		mContext.startActivity(intent);
	}

	private static int [] buttonImg = {R.drawable.tab_back,
			R.drawable.tab_myak, R.drawable.tab_search,
			R.drawable.tab_notice,R.drawable.tab_estore
	};

	private static int [] buttonImgs = {R.drawable.tab_back,
		R.drawable.tab_myak, R.drawable.tab_search,
		R.drawable.tab_notice,R.drawable.tab_estore
	};
}
