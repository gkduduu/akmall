package com.ak.android.akplaza.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkUtil {
	private static final String TAG = "NetworkUtil";

	public static final String NETWORK_WIFI = "WIFI";
	public static final String NETWORK_3G = "3G/4G";
	public static final String NETWORK_NONE = "NONE";

	public static String checkStatus(Context mContext) {

		final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		final android.net.NetworkInfo mobile_4g = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

		if (wifi.isConnected()) {
			Log.d(TAG, "NETWORK_WIFI");

			return "WIFI";

		} else if (mobile.isConnected() || mobile_4g.isConnected()) {
			Log.d(TAG, "NETWORK_3G,NETWORK_4G");

			return "3G/4G";
		} else {
			Log.d(TAG, "NETWORK_NONE");

			return "NONE";
		}
	}

	public static void NetworkErrorDialog(Context mContext) {
		AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
		ab.setTitle("알림").create();
		ab.setMessage("Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!")
				.create();
		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).create();
		ab.show();
	}

}
