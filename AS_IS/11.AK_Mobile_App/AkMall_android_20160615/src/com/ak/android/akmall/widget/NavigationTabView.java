package com.ak.android.akmall.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.util.UriProvider;

public class NavigationTabView extends FrameLayout {

	public static final String TAG = "NavigationTab";
	public static final boolean DBG = false;

	public static final int TYPE_MAIN = 0;
	public static final int TYPE_SUB = 1;

	public static final int TAB_MAIN_HOME = 0;
	public static final int TAB_MAIN_SEARCH = 1;
	public static final int TAB_MAIN_HISTORY = 2;
	public static final int TAB_MAIN_SHORTCUT = 3;
	public static final int TAB_MAIN_EVENT = 4;
	public static final int TAB_MAIN_MYAK = 5;

	public static final int TAB_SUB_HOME = 0;
	public static final int TAB_SUB_BACKWARD = 1;
	public static final int TAB_SUB_FORWARD = 2;
	public static final int TAB_SUB_TOP = 3;
	public static final int TAB_SUB_MYAK = 4;


	private ViewGroup mMainTab;
	//private ViewGroup mSubTab;

	private int mCurrentMainTab = -1;

	private UriProvider mUriProvider;

	public NavigationTabView(Context context) {
		this(context, null);
	}

	public NavigationTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mUriProvider = UriProvider.getInstance(context);
		initViewChild();
	}

	public NavigationTabView setBackwardOnClickListener(OnClickListener listener) {
		findViewById(R.id.btnSubTabBackward).setOnClickListener(listener);
		return this;
	}

	public NavigationTabView setForwardOnClickListener(OnClickListener listener) {
		findViewById(R.id.btnSubTabForward).setOnClickListener(listener);
		return this;
	}

	public NavigationTabView setScrollTopOnClickListener(OnClickListener listener) {
		findViewById(R.id.btnSubTabScrollTop).setOnClickListener(listener);
		return this;
	}
	
	public NavigationTabView setBackwardEnabled(boolean enabled) {
		//findViewById(R.id.btnSubTabBackward).setEnabled(enabled);
		return this;
	}

	public NavigationTabView setForwardEnabled(boolean enabled) {
		//findViewById(R.id.btnSubTabForward).setEnabled(enabled);
		return this;
	}

	public NavigationTabView setScrollTopEnabled(boolean enabled) {
		//findViewById(R.id.btnSubTabScrollTop).setEnabled(enabled);
		return this;
	}

	public void showMainTab() {
		mMainTab.setVisibility(VISIBLE);
		//mSubTab.setVisibility(GONE);
	}

	public void setCurrentMainTab(int index) {
		if (DBG) Log.d(TAG, "setCurrentMainTab : " + index);

		if (index == mCurrentMainTab) {
			return;
		}

		if (index < 0 || index >= mMainTab.getChildCount()) {
			return;
		}

		if(mCurrentMainTab != -1 ) {
			mMainTab.getChildAt(mCurrentMainTab).setSelected(false);
		}

		mCurrentMainTab = index;
		mMainTab.getChildAt(mCurrentMainTab).setSelected(true);
	}

	public void setCurrentMainTabByUriCode(int uriCode) {
		int index;
		switch (uriCode) {
		case UriProvider.URI_MAIN:
			index = TAB_MAIN_HOME;
			break;

		case UriProvider.URI_SEARCH:
			index = TAB_MAIN_SEARCH;
			break;

		case UriProvider.URI_HISTORY:
			index = TAB_MAIN_HISTORY;
			break;
			
		case UriProvider.URI_SHORTCUT:
			index = TAB_MAIN_SHORTCUT;
			break;

		case UriProvider.URI_EVENT:
			index = TAB_MAIN_EVENT;
			break;

		case UriProvider.URI_MYAK:
			index = TAB_MAIN_MYAK;
			break;
			
		default:
			return;
		}

		setCurrentMainTab(index);
	}

	private void initViewChild() {
		Context context = getContext();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainTab = (ViewGroup) inflater.inflate(R.layout.navigation_tab_main, null);
		addView(mMainTab); // first child TAB_MAIN
		setupMainTabChild();
	}

	
	
	private void setupMainTabChild() {
		View view;
		for (int i = 0, len = mMainTab.getChildCount(); i < len; i++) {
			view = mMainTab.getChildAt(i);
			view.setOnClickListener(mDefaultOnClickListener);
		}
	}
	
	/*
	 * 
	 * private void setupMainTabChild() {
		View view;
		LinearLayout linearLayout;
		
		for (int i = 0, len = mMainTab.getChildCount(); i < len; i++) {
			linearLayout = (LinearLayout)mMainTab.getChildAt(i);
			view = linearLayout.getChildAt(0);
			view.setOnClickListener(mDefaultOnClickListener);
		}
	}
	
	private void setupSubTabChild() {
		View view;
		for (int i = 0, len = mSubTab.getChildCount(); i < len; i++) {
			view = mSubTab.getChildAt(i);
			view.setOnClickListener(mDefaultOnClickListener);
		}
	}*/

	private OnClickListener mDefaultOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnMainTabHome:
				setCurrentMainTab(TAB_MAIN_HOME);
				//startMainActivity(mUriProvider.getUri(UriProvider.URI_MAIN), true);
				//AkMallActivity.showWebView();		
				AkMallActivity.showJavaScript("javascript:clickTab(1);");
				break;

			case R.id.btnMainTabSearch:
				setCurrentMainTab(TAB_MAIN_SEARCH);
				Log.d("Test", "==========>" + mUriProvider.getUri(UriProvider.URI_SEARCH));
				//startMainActivity(mUriProvider.getUri(UriProvider.URI_SEARCH), true);
				//AkMallActivity.showWebView();
				AkMallActivity.showJavaScript("javascript:clickTab(2);");
				break;
				
			case R.id.btnMainTabHistory:
				setCurrentMainTab(TAB_MAIN_HISTORY);
				//startMainActivity(mUriProvider.getUri(UriProvider.URI_HISTORY), true);
				//AkMallActivity.showWebView();		
				AkMallActivity.showJavaScript("javascript:clickTab(3);");
				break;

			case R.id.btnMainTabShortcut:
				setCurrentMainTab(TAB_MAIN_SHORTCUT);
				//startMainActivity(mUriProvider.getUri(UriProvider.URI_SHORTCUT), true);
				//AkMallActivity.showWebView();			
				AkMallActivity.showJavaScript("javascript:clickTab(4);");
				break;

			case R.id.btnMainTabEvent:
				setCurrentMainTab(TAB_MAIN_EVENT);
				//startMainActivity(mUriProvider.getUri(UriProvider.URI_EVENT), true);
				//AkMallActivity.showWebView();		
				AkMallActivity.showJavaScript("javascript:clickTab(5);");
				break;

			case R.id.btnMainTabMyAk:
				setCurrentMainTab(TAB_MAIN_MYAK);
				//startMainActivity(mUriProvider.getUri(UriProvider.URI_MYAK), true);
				//AkMallActivity.showWebView();		
				AkMallActivity.showJavaScript("javascript:clickTab(6);");
				break;
				
			}
		}
	};

	private void startMainActivity(Uri uri, boolean clearHistory) {
		AkMallFacade.startMainActivity(getContext(), uri, clearHistory);
	}
}
