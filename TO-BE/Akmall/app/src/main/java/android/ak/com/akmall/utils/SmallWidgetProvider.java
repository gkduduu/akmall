package android.ak.com.akmall.utils;

import android.ak.com.akmall.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * Created by gkdud on 2016-11-07.
 * 짝은위젯
 */

public class SmallWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_small);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
//            Bundle extras = intent.getExtras();
//            if (extras != null) {
//                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//                if (appWidgetIds != null && appWidgetIds.length > 0) {
//                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
//                }
//            }
//        }
//        else if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
//            Bundle extras = intent.getExtras();
//            if (extras != null && extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
//                final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
//                this.onDeleted(context, new int[] { appWidgetId });
//            }
//        }
//        else if (AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)) {
//            this.onEnabled(context);
//        }
//        else if (AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)) {
//            this.onDisabled(context);
//        }
//    }
}
