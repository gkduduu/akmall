package com.ak.android.akmall.utils;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.SplashActivity;
import com.ak.android.akmall.activity.SplashActivity_;
import com.ak.android.akmall.activity.TestActivity;
import com.ak.android.akmall.activity.TestActivity_;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.WidgetResult;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.RemoteViews;

/**
 * Created by gkdud on 2016-11-07.
 * 큰위젯
 */

public class BigWidgetProvider extends AppWidgetProvider {

    public static String ACTION_WIDGET_HOME = "ACTION_WIDGET_HOME";
    public static String ACTION_WIDGET_CHECK = "ACTION_WIDGET_CHECK";
    public static String ACTION_WIDGET_EVENT = "ACTION_WIDGET_EVENT";
    public static String ACTION_WIDGET_BAG = "ACTION_WIDGET_BAG";
    public static String ACTION_WIDGET_DELIVERY = "ACTION_WIDGET_DELIVERY";
    public static String ACTION_WIDGET_SEARCH = "ACTION_WIDGET_SEARCH";
    public static String ACTION_WIDGET_REFRESH = "ACTION_WIDGET_REFRESH";
    public static String ACTION_WIDGET_ALARM = "ACTION_WIDGET_ALARM";

    public static String ACTION_WIDGET_LOGIN = "ACTION_WIDGET_LOGIN";

    public static String NO_LOGIN_STATE = "로그인 후 편리하게 이용하세요.";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_big);

        appWidgetManager.updateAppWidget(appWidgetIds, view);
    }

    public void onReceive(Context context, Intent intent) {
        JHYLogger.D("ASDFKJH23PORYUHFJKLHDFJLADHF");
        String action = intent.getAction();
        JHYLogger.D(action);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            updateWidget(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())), intent);
//            Bundle extras = intent.getExtras();
//            if (extras != null) {
//                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//                if (appWidgetIds != null && appWidgetIds.length > 0) {
//                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
//                }
//            }
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
        } else if (action.equals(ACTION_WIDGET_HOME)) {
            context.startActivity(new Intent(context, SplashActivity_.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (action.equals(ACTION_WIDGET_DELIVERY)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "delivery").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else if (action.equals(ACTION_WIDGET_CHECK)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "check").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if (action.equals(ACTION_WIDGET_EVENT)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "event").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if (action.equals(ACTION_WIDGET_BAG)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "bag").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if(action.equals(ACTION_WIDGET_LOGIN)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "login").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if(action.equals(ACTION_WIDGET_SEARCH)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "search").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if(action.equals(ACTION_WIDGET_ALARM)) {
            context.startActivity(new Intent(context, SplashActivity_.class).putExtra("move", "alarm").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void initView(Context con, AppWidgetManager appWidgetManager, int[] appWidgetIds, Intent it,String feed) {
        RemoteViews views = new RemoteViews(con.getPackageName(), R.layout.widget_big);
        //WIDGET_BIG_HOME
        //WIDGET_BIG_CHECK
        //WIDGET_BIG_EVENT
        //WIDGET_BIG_BUCKET
        //WIDGET_BIG_DELIVERY
        //wjdgkdwuhdjkfawjhdkff

        Intent home = new Intent(con, BigWidgetProvider.class);
        home.setAction(ACTION_WIDGET_HOME);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(con, 0, home, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_HOME, pendingIntent1);

        Intent check = new Intent(con, BigWidgetProvider.class);
        check.setAction(ACTION_WIDGET_CHECK);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(con, 0, check, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_CHECK, pendingIntent2);

        Intent event = new Intent(con, BigWidgetProvider.class);
        event.setAction(ACTION_WIDGET_EVENT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(con, 0, event, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_EVENT, pendingIntent3);

        Intent bag = new Intent(con, BigWidgetProvider.class);
        bag.setAction(ACTION_WIDGET_BAG);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(con, 0, bag, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_BUCKET, pendingIntent4);

        Intent delivery = new Intent(con, BigWidgetProvider.class);
        delivery.setAction(ACTION_WIDGET_DELIVERY);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(con, 0, delivery, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_DELIVERY, pendingIntent5);

        Intent search = new Intent(con, BigWidgetProvider.class);
        search.setAction(ACTION_WIDGET_SEARCH);
        PendingIntent pendingIntent7 = PendingIntent.getBroadcast(con, 0, search, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_SEARCH, pendingIntent7);

        AppWidgetManager mgr = AppWidgetManager.getInstance(con);
        Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        update.setClass(con, BigWidgetProvider.class);
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(con, BigWidgetProvider.class)));
        PendingIntent pendingIntent8 = PendingIntent.getBroadcast(con, 0, update, 0);
        views.setOnClickPendingIntent(R.id.WIDGET_BIG_REFRESH, pendingIntent8);

        //위젯 알림 변경경
        if(feed.length()>30) {
            feed = feed.substring(0,29) + "..";
        }
        views.setTextViewText(R.id.WIDGET_BIG_TEXT, feed);

        if(feed.equals(NO_LOGIN_STATE)) {
            //로그인 안됨
            views.setViewVisibility(R.id.WIDGET_BIG_ALARM, View.GONE);
            views.setViewVisibility(R.id.WIDGET_BIG_LOGIN, View.VISIBLE);
            Intent login = new Intent(con, BigWidgetProvider.class);
            login.setAction(ACTION_WIDGET_LOGIN);
            PendingIntent pendingIntent6 = PendingIntent.getBroadcast(con, 0, login, 0);
            views.setOnClickPendingIntent(R.id.WIDGET_BIG_LOGIN, pendingIntent6);
        }else {
            //로그인 됨됨
            views.setViewVisibility(R.id.WIDGET_BIG_ALARM, View.VISIBLE);
            views.setViewVisibility(R.id.WIDGET_BIG_LOGIN, View.GONE);

            Intent alarm = new Intent(con, BigWidgetProvider.class);
            alarm.setAction(ACTION_WIDGET_ALARM);
            PendingIntent pendingIntent9 = PendingIntent.getBroadcast(con, 0, alarm, 0);
            views.setOnClickPendingIntent(R.id.WIDGET_BIG_TEXT, pendingIntent9);
       }

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void updateWidget(final Context con, final AppWidgetManager appWidgetManager, final int[] appWidgetIds,final Intent it) {
        JHYLogger.D("업데이트 위젯!!!!!!!!!!!!!!!!!!!!!!!!!");
        DataControlManager.getInstance().addSchedule(
                    new DataControlHttpExecutor().requestWidget(con,
                            new RequestCompletionListener() {
                                @Override
                                public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                    JHYLogger.d("updateWidget >> "+responseData.toString());
                                    WidgetResult result = Parser.parsingWidget(responseData.toString());
                                    JHYLogger.D(result.feed_msg);
                                    JHYLogger.D(result.hasLogin);
                                    if(result.hasLogin.equals("N")) {
                                        initView(con,appWidgetManager,appWidgetIds,it,NO_LOGIN_STATE);
                                    }else {
                                        initView(con,appWidgetManager,appWidgetIds,it,result.feed_msg);
                                    }
                                }
                            },
                            new RequestFailureListener() {
                                @Override
                                public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
                                }
                            }
                    ));
            DataControlManager.getInstance().runScheduledCommandOnAsync();
    }
}
