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

package com.ak.android.akplaza;

import com.ak.android.akplaza.common.AkPlazaAPI;
import com.ak.android.akplaza.common.SharedUtil;
import com.ak.android.akplaza.common.WebViewActivity;
import com.ak.android.akplaza.intro.IntroActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class C2dm_BroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "C2dm_BroadcastReceiver";

    public static final String ACTION_C2DM_REGISTER = "com.google.android.c2dm.intent.REGISTER";
    public static final String ACTION_C2DM_REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";
    public static final String ACTION_C2DM_RECEIVE = "com.google.android.c2dm.intent.RECEIVE";

    // 아이디 : zzpase@gmail.com 비번 : zz071515
    static String registration_id = null;
    static boolean isAlive = false;
    private static final int NOTI_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (ACTION_C2DM_REGISTRATION.equals(action)) {

            handleRegistration(context, intent);

        } else if (ACTION_C2DM_RECEIVE.equals(action)) {

            handleReceive(context, intent);
        }
    }

    private void handleReceive(Context context, Intent intent) {
        String msg = intent.getExtras().getString("msg");
        String bc = intent.getExtras().getString("BC");
        String pid = intent.getExtras().getString("PID");
        String onlyMsg = getOnlyMsg(msg);

//        Log.d(TAG, "msg: " + msg + ", bc" + bc + ", pid: " + pid);
//        Toast toast = Toast.makeText(context, "메시지 도착!\n" + onlyMsg, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 150);
//        toast.show();

        notifyPush(context, bc, pid, onlyMsg);
    }

    private void notifyPush(Context context, String bc, String pid, String onlyMsg) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence tickerText = onlyMsg;
        CharSequence contentTitle = context.getString(R.string.app_name);
        CharSequence contentText = onlyMsg;

        Intent notiIntent = null;

        if (WebViewActivity.isExist()) {
            notiIntent = new Intent(context, WebViewActivity.class);
            notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        } else {
            notiIntent = new Intent(context, IntroActivity.class);
        }

        notiIntent.putExtra("EVENT", "PUSHEVENT");
        notiIntent.putExtra("PID", pid);
        notiIntent.putExtra("BC", bc);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notiIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_logo_small)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources()
                        , R.drawable.ic_launcher))
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        Notification noti = builder.build();

        setNotificationSound(context, noti);

        mNotificationManager.notify(NOTI_ID, noti);
    }

    private void setNotificationSound(Context context, Notification notification) {
        AudioManager audioManager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                notification.sound = Uri.parse("android.resource://" + context.getPackageName()
                        + "/" + R.raw.sound0);
                break;
            default:
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                break;
        }
    }

    private String getOnlyMsg(String msg) {

        int start = 0;
        if (msg == null || (start = msg.indexOf('&')) == -1) {
            return msg;
        }

        return msg.substring(start + 1);
    }

    private void handleRegistration(Context context, Intent intent) {

        String push = SharedUtil.getSharedString(context, "DEVICE", "push"); // on: 허용, off: 거절

        String deny = "0"; // 알림 거절
        if ("on".equals(push)) {
            deny = "1"; // 알림 승인
        }

        registration_id = intent.getStringExtra("registration_id");

        System.out.println("registration_id====>" + registration_id);

        if (intent.getStringExtra("error") != null) {

            Log.w(TAG, "Registration failed, should try again later.");
            Toast.makeText(context, "알림 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            SharedUtil.setSharedString(context, "DEVICE", "OK", "1");

        } else if (intent.getStringExtra("unregistered") != null) {

            Log.w(TAG,
                    "unregistration done, new messages from the authorized sender will be rejected");
            Toast.makeText(context, "알림 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            SharedUtil.setSharedString(context, "DEVICE", "OK", "1");

        } else if (registration_id != null) {

            Log.d(TAG, "registration_id complete!!");
            String appid = Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            AkPlazaAPI api = new AkPlazaAPI(context);
            boolean isSuccess = api.registPushDeviceToken(registration_id, appid, deny);

            if (isSuccess) {
                SharedUtil.setSharedString(context, "C2DM", "token", registration_id);
                SharedUtil.setSharedString(context, "DEVICE", "OK", "0");
            } else {
                SharedUtil.setSharedString(context, "DEVICE", "OK", "1");
                Toast.makeText(context, "알림 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.w(TAG, "Registration failed, should try again later.");
            Toast.makeText(context, "알림 인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
