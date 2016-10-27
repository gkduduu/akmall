package com.ak.android.akmall.util;

import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.common.CommonDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	public static final String TAG = "NetworkUtil";

	public static final int NETWORK_WIFI = 0;
	public static final int NETWORK_3G_4G = 1;
	public static final int NETWORK_NONE = 2;

	public static int getStatus(Context context) {

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo mobile_4g = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

		if (wifi != null && wifi.isConnected()) {
			return NETWORK_WIFI;
		} else if ( (mobile != null && mobile.isConnected())
				|| (mobile_4g != null && mobile_4g.isConnected()) ) {
			return NETWORK_3G_4G;
		} else {
			return NETWORK_NONE;
		}
	}

	public static void showNetworkErrorDialog(Context context) {
		CommonDialog commonDialog = new CommonDialog(AkMallActivity.mainActivity, false, 
				"Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!", 
				context.getString(android.R.string.ok), context.getString(android.R.string.no));
		commonDialog.show(); 		
	}

}
