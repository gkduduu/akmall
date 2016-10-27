package com.ak.android.akplaza.common;


import android.app.Instrumentation;
import android.content.Context;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HeaderClient extends WebViewClient {
	private Context mContext;
	public Context getmContext() {
		return mContext;
	}
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		AkPlazaFacade.startMainActivity(mContext, Uri.parse(url));
		new Thread(new Runnable() {         
		       public void run() {                 
		           new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		       }   
		    }).start();
		
		return false;
	}
	
}
