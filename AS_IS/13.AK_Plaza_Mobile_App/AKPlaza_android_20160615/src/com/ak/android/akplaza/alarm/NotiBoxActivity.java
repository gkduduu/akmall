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

package com.ak.android.akplaza.alarm;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.ActivityTaskManager;
import com.ak.android.akplaza.common.AkPlazaFacade;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.HeaderClient;
import com.ak.android.akplaza.common.LoginManager;
import com.ak.android.akplaza.common.NetworkUtil;
import com.ak.android.akplaza.common.PostHttpClient;
import com.ak.android.akplaza.common.ProgressBarManager;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.StringUtil;
import com.ak.android.akplaza.common.XMLController;
import com.ak.android.akplaza.maintab.BottomTab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class NotiBoxActivity extends Activity {

    public static final String TAG = "NotiBoxActivity";

    public static final String BC_COMMON = "99";
    public static final int REQUEST_LOGIN = 11;

    private List<Map<String, String>> mNotiBoxList = null;
    private ProgressBarManager progressmanager = null;
    private String mEvent;
    private String mPid = null;
    private String mBc;
    private RelativeLayout mMainLayout;

    private boolean mIsRequestedLogin;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aknotibox);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LoginManager.isLogin(this)) {
            LoginManager.appAutoLogin(this);
        }

        loadData();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        ActivityTaskManager.getInstance().deleteActivity(this);
    }

    private void init() {
        ActivityTaskManager.getInstance().addActivity(this);
        notifiCancel();
        getIntentExtra();

        mIsRequestedLogin = false;
        mMainLayout = (RelativeLayout) findViewById(R.id.notibox_relative);
        WebView appheader = (WebView) findViewById(R.id.appheader);
        
        appheader.getSettings().setJavaScriptEnabled(true);
        HeaderClient headerClient = new HeaderClient();
        headerClient.setmContext(NotiBoxActivity.this);
        appheader.setWebViewClient(headerClient);
        String url = Const.URL_LIB + this.getString(R.string.act_header);
        appheader.loadUrl(url);
    }

    private void loadData() {
        if (progressmanager == null) {
            progressmanager = new ProgressBarManager(this);
            progressmanager.ableKeyEvent(true);
            mMainLayout.addView(progressmanager);
        } else {
            progressmanager.stopProgress();
        }

        String cmd[] = {
                "getData", "setListAdapter", "getDataFinish"
        };
        progressmanager.setCommandClass(this, cmd, null);

        networkCheck();
    }

    public void getDataFinish() {
        if (mPid != null) {
            String pushId = null;
            for (Map<String, String> item : mNotiBoxList) {
                pushId = item.get("PUSH_ID");
                if (pushId.equals(mPid)) {
                    showMessage(item);
                    mPid = null;
                    break;
                }
            }
        }
        progressmanager.stopProgress();
    }

    private void showMessage(Map<String, String> item) {
        String eventName = " ";//getString(R.string.app_name);
        String content = item.get("TITLE");
        String url = item.get("SHORT_URL");
        
        String token = SharedUtil.getSharedString(this, "C2DM", "token");
        String strUrl = Const.URL_LIB+"act=pushDetail&token="+token+"&push_id="+item.get("PUSH_ID");
        PostHttpClient.HttpConnect(strUrl);
        
        
        BottomTab botTab = (BottomTab)findViewById(R.id.bottom);
        botTab.setBadge();
        
        showMessageDialog(eventName, content, url);
    }

    private void showMessageDialog(String title, String message, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.logo_small)
                .setTitle(title)
                .setMessage(message);

        if (url == null || url.length() == 0) {
            builder.setPositiveButton(android.R.string.ok, null);
        } else {
            builder.setPositiveButton(R.string.show, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AkPlazaFacade.startMainActivity(NotiBoxActivity.this, Uri.parse(url));
                }
            })
                    .setNegativeButton(android.R.string.cancel, null);
        }

        builder.show();

    }

    private void networkCheck() {

        String data_info = NetworkUtil.checkStatus(this);
        if (data_info.equals(NetworkUtil.NETWORK_NONE)) {
            NetworkUtil.NetworkErrorDialog(this);
        } else {
            progressmanager.startProgress();
        }
    }

    private void getIntentExtra() {
        Intent intent = getIntent();

        mEvent = intent.getStringExtra("EVENT");
        mPid = intent.getStringExtra("PID");
        mBc = intent.getStringExtra("BC");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getData() {
        mNotiBoxList = XMLController.getAlarmList(this);
    }

    public void setListAdapter() {
        if (mNotiBoxList == null) {
            return;
        }

        ListAdapter listAdapter = new ListAdapter(this, R.layout.notiboxlistview, mNotiBoxList);
        ListView mAlram_save_listView = (ListView) findViewById(R.id.alram_save_listView);
        mAlram_save_listView.setAdapter(listAdapter);
        mAlram_save_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	TextView mAlram_save_text = (TextView) v.findViewById(R.id.alram_save_text);
            	mAlram_save_text.setTextColor(Color.parseColor("#CCCCCC"));
            	mNotiBoxList.get(position).put("READ", "1");
                showMessage(mNotiBoxList.get(position));
            }
        });

//		else{
//			notiBoxList = new ArrayList();
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//			notiBoxList.add("test");
//
//			ListAdapter listAdapter = new ListAdapter(this, R.layout.notiboxlistview, notiBoxList);
//			ListView mAlram_save_listView = (ListView)findViewById(R.id.alram_save_listView);
//			mAlram_save_listView.setAdapter(listAdapter);
//		}

    }

    public class ListAdapter extends BaseAdapter {

        Context maincon;
        LayoutInflater inflater;
        List<Map<String, String>> arSrc;
        int layout;

        public ListAdapter(Context context, int alayout, List<Map<String, String>> aarSrc) {
            maincon = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arSrc = aarSrc;
            layout = alayout;
        }

        @Override
        public int getCount() {
            return arSrc.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView mAlram_save_text = (TextView) convertView.findViewById(R.id.alram_save_text);
            TextView mAlarm_save_date = (TextView) convertView.findViewById(R.id.alram_save_date);
//            RelativeLayout mAlram_save_listbg = (RelativeLayout) convertView
//                    .findViewById(R.id.alram_save_listbg);
//            mAlram_save_listbg.setBackgroundResource(R.drawable.ak_category_listbg_a);
//			if(position%2 == 0){
//				mAlram_save_listbg.setBackgroundResource(R.drawable.ak_category_listbg_a);
//			}else{
//				mAlram_save_listbg.setBackgroundResource(R.drawable.ak_category_listbg_b);
//			}
            Map<String, String> map = arSrc.get(position);
//			String text = (String)map.get("TEXT");
            String title = (String) map.get("TITLE");
            String eDate = (String) map.get("SEND_DT");
            String Read = StringUtil.nullTo((String) map.get("READ"), "0") ;
            mAlram_save_text.setText(title);
            mAlarm_save_date.setText(eDate);
            if(Read.equals("1")) {
            	mAlram_save_text.setTextColor(Color.parseColor("#CCCCCC"));
            } else {
            	mAlram_save_text.setTextColor(Color.parseColor("#000000"));
            }

            return convertView;
        }
    }

    public void backButtonPressed(View view) {
        finish();
    }

    private void notifiCancel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOGIN:
                postProcessLogin(resultCode, data);
                break;

            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        setIntent(intent);
        getIntentExtra();
        mIsRequestedLogin = false;

        super.onNewIntent(intent);
    }

    private void postProcessLogin(int resultCode, Intent data) {

        mIsRequestedLogin = true;

        if (resultCode != RESULT_OK) {

            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setIcon(R.drawable.logo_small);
            ab.setTitle(" ");
            ab.setMessage(R.string.login_required_notibox);
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                }
            }).create();

            ab.show();
        }
    }
}
