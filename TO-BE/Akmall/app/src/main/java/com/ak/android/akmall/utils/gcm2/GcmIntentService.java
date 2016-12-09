package com.ak.android.akmall.utils.gcm2;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AlarmDialogActivity;
import com.ak.android.akmall.activity.TestActivity_;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.URLManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? :
 */

public class GcmIntentService extends IntentService {
    public static final String TAG = "icelancer";
    public static final int NOTI_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;



    public GcmIntentService() {
//        Used to name the worker thread, important only for debugging.
        super("GcmIntentService");
    }

    //푸시받아서 처리하는 곳
    @Override
    protected void onHandleIntent(Intent intent) {
        JHYLogger.D("AKSDJHKJLASDHFKLASDHFJKASHDFKL");
        JHYLogger.D("push~~~!!!");
        if (Feature.DEBUG_MODE) {
            Log.d(TAG, intent.getExtras().toString());
        }

        String type = intent.getStringExtra("type");
        if (Feature.DEBUG_MODE) {
            Log.d(TAG, "received push type : " + type);
        }

        if ("alarm".equals(type)) {
            showNormalAlarmPush(this, intent);
        } else if ("goods_review".equals(type)) {
            showGoodsReviewAlarmPush(this, intent);
        }
    }

    /**
     * 미작성 상품평 알림을 표시 한다.
     *
     * @param context
     * @param intent  푸시로 받은 인텐트
     */
    private void showGoodsReviewAlarmPush(Context context, Intent intent) {
        // 자동 로그인 설정한 사람만 대상으로 한다.
        // 메시지의 규격은 alarm과 동일하므로 같은 방식으로 처리 한다.
        if (Feature.isAutoLogin) {
            showNormalAlarmPush(context, intent);
        }
    }
    /**
     * 일반 Alarm 알림을 표시 한다.
     *
     * @param context
     * @param intent  푸시로 받은 인텐트
     */
    private void showNormalAlarmPush(Context context, Intent intent) {
        String message = intent.getStringExtra("msg");
        String pid = intent.getStringExtra("pid");
        String imgUrl = intent.getStringExtra("imgUrl");

        // 이미지가 포함되어 있으면 미리 다운로드 함
        // 이후 동일한 glide로 로드하면 빠르게 로딩 가능
        if (!TextUtils.isEmpty(imgUrl)) {
            doPreDownloadImage(context, imgUrl);
        }

        // 알림 표시
        showAlarmNotification(context, message, pid, imgUrl);

        // 다이얼로그 표시
        showAlarmDialog(context, message, pid, imgUrl);
    }

    private void doPreDownloadImage(Context context, String imgUrl) {
        try {
            Glide
                    .with(context)
                    .load(imgUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            // 비동기 호출!
        } catch (Exception e) {
            Log.e(TAG, "Failed preload image");
        }
    }

    /**
     * 알림 다이얼로그를 표시 한다.
     *
     * @param context
     * @param message 메시지
     * @param pid     Push Id
     */
    private void showAlarmDialog(Context context, String message, String pid, String imgUrl) {

        //p65458 20150729 add 현재 앱이 foreground 상태일경우는 다이얼로그를 보여주고 아니면 토스트로 보여준
        ActivityManager activityapp = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> list = (List<ActivityManager.RunningAppProcessInfo>) activityapp.getRunningAppProcesses();

    	  for(int i = 0 ; i < list.size() ; i++) {
    	      ActivityManager.RunningAppProcessInfo info = list.get(i);
    	      if ( info.processName.equals("com.ak.android.akmall") && info.importance == info.IMPORTANCE_FOREGROUND ){
    	          Intent alertIntent = new Intent(context, AlarmDialogActivity.class);
    	          alertIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_PUSH_ID, pid);
    	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_MESSAGE, message);
    	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_NOTIFICATION_ID, NOTI_ID);
    	          alertIntent.putExtra(AlarmDialogActivity.EXTRA_IMGURL, imgUrl);
    	          context.startActivity(alertIntent);
    	          return;
    	      }else{
    	          Toast.makeText(context, message, Toast.LENGTH_LONG)
    	          .show();
    	          return;
    	      }
    	  }
        //p65458 20150729 add 현재 앱이 foreground 상태일경우는 다이얼로그를 보여주고 아니면 토스트로 보여준

    }

    /**
     * 알림을 Notification (status bar)에 표시한다.
     *
     * @param context
     * @param message
     * @param pid
     */
    private void showAlarmNotification(Context context, String message, String pid, String imgUrl) {
        NotificationManager notiMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = R.drawable.list_ak_logo;
        CharSequence tickerText = message;
        CharSequence contentTitle = "AKmall";
        CharSequence contentText = message;
        String pageUrl = URLManager.getServerUrl() + "/app/lib.do?" + "act=viewPushDetail&push_id=" + pid + "&isAkApp=Android";
        long when = System.currentTimeMillis();

        // 알림 링크 페이지로 이동하는 pending 인텐트 생성
        Intent notiIntent = new Intent(context, TestActivity_.class);
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        notiIntent.setData(Uri.parse(pageUrl));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notiIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bigPicture = loadBitmap(context, imgUrl);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(when)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);


        if(bigPicture!=null)
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
                    .setBigContentTitle(contentTitle)
                    .setSummaryText(contentText));

        setNotificationSound(context, builder);
        notiMgr.notify(NOTI_ID, builder.build());
    }


    private void setNotificationSound(Context context, NotificationCompat.Builder builder) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                builder = builder.setSound(Uri.parse("android.resource://" + context.getPackageName()
                        + "/" + R.raw.sound0));
                break;
            default:
                builder = builder.setDefaults(Notification.DEFAULT_VIBRATE);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private Bitmap loadBitmap(Context context, String imgUrl) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int maxWidth;
        int maxHeight;
        if (Build.VERSION.SDK_INT < 13) {
            maxWidth = display.getWidth();
            maxHeight = display.getHeight();
        } else {
            Point screenSize = new Point();
            display.getSize(screenSize);
            maxWidth = screenSize.x;
            maxHeight = screenSize.y;
        }

        try {
            return Glide
                    .with(context)
                    .load(imgUrl)
                    .asBitmap()
                    .into(maxWidth, maxHeight)
                    .get();
        } catch (InterruptedException e) {
            Log.e(TAG, "Failed image downlaod");
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }


}