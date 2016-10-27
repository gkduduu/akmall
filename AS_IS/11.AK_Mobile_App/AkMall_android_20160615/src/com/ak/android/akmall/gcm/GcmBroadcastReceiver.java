
package com.ak.android.akmall.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "GcmBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Received gcm.");
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
			startMessageHandleService(context, intent);
		} // else ignore..
	}

	private void startMessageHandleService(Context context, Intent intent) {
		Intent serviceIntent = new Intent(context, GcmHandleIntentService.class);
		serviceIntent.putExtras(intent);
		context.startService(serviceIntent);
	}

}