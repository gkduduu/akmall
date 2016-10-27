package com.ak.android.akmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.widget.AkMallWebView;
import com.ak.android.akmall.widget.AkMallWebView.WebViewListener;
import com.ak.android.akmall.widget.NavigationTabView;

import com.mtracker.mea.sdk.MTrackerManager;//p65458 20150716 mtracker 연동 add

public class WebPopupActivity extends Activity implements OnClickListener {

	public static final String TAG = "WebPopupActivity";
	public static final boolean DBG = false;

	public static final String EXTRA_TITLE = "com.ak.android.akmall.intent.extra.TITLE";

	private TextView mTvTitle;
	private AkMallWebView mWebView;
	private NavigationTabView mNaviTab;
	private ProgressBar mProgressBar;

	private boolean mHasTitle = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialView();
		loadData(getIntent());
	}

	private void loadData(Intent intent) {
		Uri data = intent.getData();
		String title = intent.getStringExtra(EXTRA_TITLE);

		if (data == null) {
			finish();

		} else {
			if(title != null) {
				mTvTitle.setText(title);
				mHasTitle = true;
			} else {
				mHasTitle = false;
			}
			mWebView.loadUrl(data.toString());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (DBG) Log.d(TAG, "onNewIntent : " + intent);
		setIntent(intent);
		loadData(intent);
		super.onNewIntent(intent);
	}

	private void initialView() {
		// load view resource
		setContentView(R.layout.web_popup);

		mTvTitle = (TextView)findViewById(R.id.header_title);
		mProgressBar = (ProgressBar) findViewById(R.id.webviewProgressBar);
		mWebView = (AkMallWebView) findViewById(R.id.akMallWebview);
		mNaviTab = (NavigationTabView) findViewById(R.id.bottomNaviTab);

		// setting view
		mWebView.setWebViewListener(mWebViewListener);
		mNaviTab.setScrollTopOnClickListener(mScrollTopClickListener);
		mNaviTab.setBackwardOnClickListener(mBackwardClickListener);
		mNaviTab.setForwardOnClickListener(mForwardClickListener);

		//setup title
		mTvTitle.setText(R.string.popup);
		findViewById(R.id.btnPrev).setOnClickListener(this);

		//setupTab();
	}

	@SuppressWarnings("unused")
	private void setupTab() {
		NavigationTabView tab = (NavigationTabView)findViewById(R.id.bottomNaviTab);
		tab.setBackwardEnabled(true)
		.setForwardEnabled(false)
		.setBackwardOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		})
		.setScrollTopOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWebView.scrollTo(0, 0);
			}
		});
	}

	private OnClickListener mScrollTopClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mWebView.scrollTo(0, 0);
		}
	};

	private OnClickListener mBackwardClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private OnClickListener mForwardClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mWebView.goForward();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case AkMallFacade.REQUEST_LOGIN:
			if(RESULT_OK == resultCode) {
				mWebView.reload();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private WebViewListener mWebViewListener = new WebViewListener() {
		@Override
		public void onProgressChanged(int progress) {
			mProgressBar.setProgress(progress);
		}

		@Override
		public void onPageStarted(String url) {
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(String url) {
			mProgressBar.setVisibility(View.GONE);
			if(!mHasTitle) {
				mTvTitle.setText(mWebView.getTitle());
			}
		}
	}; // mWebViewListener


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPrev:
			finish();
			break;
		}
	}
}
