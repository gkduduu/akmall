
package com.ak.android.akmall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.gcm.GcmBroadcastReceiver;
import com.bumptech.glide.Glide;
import com.mtracker.mea.sdk.MTrackerManager;//p65458 20150716 mtracker 연동 add


public class AlarmDialogActivity extends Activity {

    public static final String TAG = "AlarmDialogActivity";

    public static final String EXTRA_PUSH_ID = "pid";
    public static final String EXTRA_MESSAGE = "msg";
    public static final String EXTRA_NOTIFICATION_ID = "noti_id";
    public static final String EXTRA_IMGURL = "imgUrl";

    private int mNotificationId;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String pushId = intent.getStringExtra(EXTRA_PUSH_ID);
        String content = intent.getStringExtra(EXTRA_MESSAGE);
        mNotificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        String imgUrl = intent.getStringExtra(EXTRA_IMGURL);

        String eventName = " "; // 빈 제목
        String pageUrl = Const.URL_LIB + getString(R.string.act_view_push_detail, pushId);

        // 사용자에게 보여줄 메시지가 없거나, 이동할 링크 정보가 없는 경우 해당 푸시 알림 요청은 무시한다.
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(pushId)) {
            Log.e(TAG, "invalid push data..");
            finish();
            return;
        }

        showMessageDialog(eventName, content, pageUrl, imgUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    private void showMessageDialog(String title, String message, final String url, String imgUrl) {

        
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog, (ViewGroup)findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.logo_small).setTitle(title).setMessage(message)
                .setPositiveButton(R.string.show, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelNotificationAll();
                        AkMallFacade.startMainActivity(AlarmDialogActivity.this, Uri.parse(url),
                                false);
                    }
                }).setNegativeButton(R.string.alarm_show_later, null);
        if(imgUrl!=null){
        	Glide.with(this)
        	.load(imgUrl)
        	.into(image);           
            builder.setView(layout);
        }
        
        
        mAlertDialog = builder.create();
        mAlertDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                mAlertDialog = null;
                finish();
            }
        });

        mAlertDialog.show();
    }

    private void cancelNotificationAll() {
        if (mNotificationId == -1) {
            return;
        }
        NotificationManager notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiMgr.cancel(mNotificationId);
    }
}
