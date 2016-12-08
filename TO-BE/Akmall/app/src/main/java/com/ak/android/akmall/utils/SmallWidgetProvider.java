package com.ak.android.akmall.utils;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.SplashActivity_;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Created by gkdud on 2016-11-07.
 * 짝은위젯
 */

public class SmallWidgetProvider extends AppWidgetProvider {

    public static String ACTION_WIDGET_CHECK = "ACTION_WIDGET_CHECK";
    public static String ACTION_WIDGET_BAG = "ACTION_WIDGET_BAG";
    public static String ACTION_WIDGET_DELIVERY = "ACTION_WIDGET_DELIVERY";
    public static String ACTION_WIDGET_SEARCH = "ACTION_WIDGET_SEARCH";
    public static String ACTION_WIDGET_REFRESH = "ACTION_WIDGET_REFRESH";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_small);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            initView(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())), intent);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
                this.onDeleted(context, new int[]{appWidgetId});
            }
        } else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
            this.onEnabled(context);
        } else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
            this.onDisabled(context);
        } else if (action.equals(ACTION_WIDGET_DELIVERY)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "delivery").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (action.equals(ACTION_WIDGET_CHECK)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "check").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (action.equals(ACTION_WIDGET_BAG)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "my").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (action.equals(ACTION_WIDGET_SEARCH)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "search").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void initView(Context con, AppWidgetManager appWidgetManager, int[] appWidgetIds, Intent it) {
        RemoteViews views = new RemoteViews(con.getPackageName(), R.layout.widget_small);

        Intent check = new Intent(con, SmallWidgetProvider.class);
        check.setAction(ACTION_WIDGET_CHECK);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(con, 0, check, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_SMALL_CHECK, pendingIntent2);


        Intent bag = new Intent(con, SmallWidgetProvider.class);
        bag.setAction(ACTION_WIDGET_BAG);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(con, 0, bag, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_SMALL_MY, pendingIntent4);

        Intent delivery = new Intent(con, SmallWidgetProvider.class);
        delivery.setAction(ACTION_WIDGET_DELIVERY);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(con, 0, delivery, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_SMALL_DELIVERY, pendingIntent5);

        Intent search = new Intent(con, SmallWidgetProvider.class);
        search.setAction(ACTION_WIDGET_SEARCH);
        PendingIntent pendingIntent7 = PendingIntent.getBroadcast(con, 0, search, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_SMALL_SEARCH, pendingIntent7);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
