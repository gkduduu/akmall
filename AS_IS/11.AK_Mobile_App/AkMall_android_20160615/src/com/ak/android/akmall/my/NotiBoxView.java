package com.ak.android.akmall.my;

import java.util.List;
import java.util.Map;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.CommonDialog;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.common.ProgressBarManager;
import com.ak.android.akmall.util.NetworkUtil;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotiBoxView extends RelativeLayout implements OnClickListener{
	
	private TextView header_title;
	private Button btnPrev;
	
	private ListView mAlramListView;
	private ProgressBarManager progressmanager = null;

	private List<Map<String, String>> mNotiBoxList = null;
	private String mPid = null;
	
	public NotiBoxView(Context context) {
		super(context);
		View layout;
		layout = (View) LayoutInflater.from(context).inflate(R.layout.aknotibox, null);
		this.addView(layout);
		initialView();	
		init();
	}

	private void initialView() {
		//setup title
		this.header_title = (TextView)findViewById(R.id.header_title);
		this.header_title.setText(R.string.alarmbox);
		
		this.btnPrev = (Button)findViewById(R.id.btnPrev);
		this.btnPrev.setOnClickListener(this);

		//setupTab();
		mAlramListView = (ListView) findViewById(R.id.alram_save_listView);
	}
	
	private void init() {

		notifiAllCancel();
		//getIntentExtra();

		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.notibox_relative);
		progressmanager = null;
		progressmanager = new ProgressBarManager(AkMallActivity.mainActivity);
		progressmanager.ableKeyEvent(true);
		String cmd[] = { "getNotiData", "setListAdapter", "getDataFinish" };
		progressmanager.setCommandClass(this, cmd, null);
		mainLayout.addView(progressmanager);

		networkCheck();
	}
	
	public void getNotiData() {
		mNotiBoxList = AkMallAPI.getAlarmList(AkMallActivity.mainActivity);
	}
	
	public void setListAdapter() {

		
		if (mNotiBoxList == null) {
			return;
		}

		NotiListAdapter listAdapter = new NotiListAdapter(AkMallActivity.mainActivity, R.layout.notiboxlistview, mNotiBoxList);
		mAlramListView.setAdapter(listAdapter);
		mAlramListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showMessage(mNotiBoxList.get(position));
				/*
				 * String title = (String) map.get("TITLE"); String
				 * product_name = (String) map.get("EVENT_NAME"); String
				 * short_url = (String) map.get("SHORT_URL"); String
				 * facebook_url = (String) map.get("FACEBOOK"); String
				 * twitter_url = (String) map.get("TWITTER");
				 * Intent intent = new Intent(NotiBoxActivity.this,
				 * NotiBoxSubActivity.class); intent.putExtra("TITLE",
				 * title); intent.putExtra("EVENT_NAME", product_name);
				 * intent.putExtra("SHORT_URL", short_url);
				 * intent.putExtra("TWITTER", twitter_url);
				 * intent.putExtra("FACEBOOK", facebook_url);
				 * startActivity(intent);
				 */
			}
		});

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPrev:
			AkMallActivity.showWebView();
			break;
		}
	}
	
	public class NotiListAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<Map<String, String>> arSrc;
		int layout;

		public NotiListAdapter(Context context, int alayout, List<Map<String, String>> aarSrc) {
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

			TextView alram_save_text 	= (TextView) convertView.findViewById(R.id.alram_save_text);
			TextView alram_send_dt_text = (TextView) convertView.findViewById(R.id.alram_send_dt_text);

			LinearLayout mAlram_save_listbg = (LinearLayout) convertView.findViewById(R.id.alram_save_listbg);

			mAlram_save_listbg.setBackgroundResource(R.drawable.ak_category_listbg_a);

			Map<String, String> map = arSrc.get(position);
			String title = map.get("TITLE");
			String send_dt = map.get("SEND_DT").replace("-", ".");
			
			alram_save_text.setText(title);
			alram_send_dt_text.setText(send_dt);
			
			return convertView;
		}
	}
	
	private void notifiAllCancel() {
		NotificationManager mNotificationManager = (NotificationManager) AkMallActivity.mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
	}
	
	public void setPushPid(String PID) {
		mPid = PID;
	}
	
	private void showMessage(Map<String, String> item) {
		String eventName = item.get("EVENT_NAME");
		String content = item.get("CONTENT");
		String pageUrl = item.get("PAGE_URL");
		if(pageUrl != null && !pageUrl.startsWith("http://")) {
			pageUrl = Const.URL_BASE + pageUrl;
		}
		showMessageDialog(eventName, content, pageUrl);
	}
	
	private void networkCheck() {
		int data_info = NetworkUtil.getStatus(AkMallActivity.mainActivity);
		if (data_info == NetworkUtil.NETWORK_NONE) {
			NetworkUtil.showNetworkErrorDialog(AkMallActivity.mainActivity);
		} else {
			progressmanager.startProgress();
		}
	}
	
	public void getDataFinish() {
		if (mPid != null) {

			for (Map<String, String> item : mNotiBoxList) {
				String pushId = (String) item.get("PUSH_ID");
				if (pushId.equals(mPid)) {
					showMessage(item);
					// String shotUrl = (String) map.get("SHORT_URL");
					// String title = (String) map.get("TITLE");
					// String twitter_url = (String) map.get("TWITTER");
					// String facebook_url = (String) map.get("FACEBOOK");
					break;
				}
			}

		}
		progressmanager.stopProgress();
	}
	
	
	public void showMessageDialog(String title, String message, final String url) {
		
		CommonDialog commonDialog = new CommonDialog(AkMallActivity.mainActivity, false, message, "보러가기", "취소");
		commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				CommonDialog commonDialog = (CommonDialog) dialog;
				if(commonDialog.IsOk()) {
					AkMallFacade.startMainActivity(AkMallActivity.mainActivity, Uri.parse(url), false);
					AkMallActivity.showWebView();
				}
			}
		});
		commonDialog.show();
		
	}
}
