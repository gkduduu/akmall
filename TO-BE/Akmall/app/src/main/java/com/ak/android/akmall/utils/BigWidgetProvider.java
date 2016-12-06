package com.ak.android.akmall.utils;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.TestActivity;
import com.ak.android.akmall.activity.TestActivity_;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Created by gkdud on 2016-11-07.
 * 큰위젯
 */

public class BigWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_big);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            initView(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
//            Bundle extras = intent.getExtras();
//            if (extras != null) {
//                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//                if (appWidgetIds != null && appWidgetIds.length > 0) {
//                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
//                }
//            }
        }
        else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                this.onDeleted(context, new int[] { appWidgetId });
            }
        }
        else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
            this.onEnabled(context);
        }
        else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
            this.onDisabled(context);
        }
    }

    private void initView(Context con, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(con.getPackageName(), R.layout.widget_big);
        //WIDGET_BIG_HOME
        //WIDGET_BIG_CHECK
        //WIDGET_BIG_EVENT
        //WIDGET_BIG_BUCKET
        //WIDGET_BIG_DELIVERY
        //wjdgkdwuhdjkfawjhdkff
        Intent intent = new Intent(con, TestActivity_.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(con,0,intent,0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_HOME,pendingIntent);

        for(int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
