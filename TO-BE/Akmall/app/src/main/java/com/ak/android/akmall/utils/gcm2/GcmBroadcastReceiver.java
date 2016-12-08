package com.ak.android.akmall.utils.gcm2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ak.android.akmall.utils.JHYLogger;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? :
 */

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        JHYLogger.D("AKSDJHKJLASDHFKLASDHFJKASHDFKL");
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}