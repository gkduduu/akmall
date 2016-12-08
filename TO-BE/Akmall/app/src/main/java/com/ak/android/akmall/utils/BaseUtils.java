package com.ak.android.akmall.utils;

import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.webkit.CookieManager;

import com.ak.android.akmall.activity.SplashActivity;
import com.ak.android.akmall.utils.http.URLManager;

import java.net.URISyntaxException;
import java.text.DecimalFormat;

/**
 * Created by gkdud on 2016-11-02.
 */

//각종 유틸들 저장소
public class BaseUtils {

    /**
     * 통신상태 여부 확인
     *
     * @param context
     * @return
     */
    public static boolean networkCheck(Context context) throws Exception {

        ConnectivityManager mng = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mng == null)
            return false;

        NetworkInfo mobile = mng
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = mng.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return (mobile.isConnected() || wifi.isConnected());
    }


    /**
     * arg0 이 null 일 경우 "" 로 변환
     *
     * @param str
     * @return
     */
    public static String nvl(String str) {

        if (null == str) {
            str = "";
        }
        return str;
    }

    /**
     * arg0 이 null 일 경우 "" 로 변환
     *
     * @param str
     * @return
     */
    public static String nvl(String str, String replace) {

        if (null == str) {
            str = replace;
        }
        if (str.equals("")) {
            str = replace;
        }
        return str;
    }

    /**
     * 콤마 찍어버리기~~
     *
     * @param won
     * @return
     */
    public static String wonFormat(String won) {
        if (won.equals("") || null == won) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(Integer.parseInt(won));
    }

    public static String wonFormat(int won) {
        if (won == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(won);
    }

    /**
     * PX->DP
     *
     * @param dp
     * @param context
     * @return
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * DP -> PX
     *
     * @param px      dp
     * @param context
     * @return
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    //자동로그인 여부
    public static boolean isAutoLogin() {
        if (null != CookieManager.getInstance().getCookie(URLManager.getServerUrl())) {
            for (String aKey : CookieManager.getInstance().getCookie(URLManager.getServerUrl()).split(";")) {
                if (aKey.split("=")[0].equals(" loginf") || aKey.split("=")[0].equals("loginf")) {
                    if (aKey.split("=")[1].equals("YN") || aKey.split("=")[1].equals("NN")) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void updateWidget(Context context) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        update.setClass(context, BigWidgetProvider.class);
        update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, mgr.getAppWidgetIds(new ComponentName(context, BigWidgetProvider.class)));
        context.sendBroadcast(update);
    }

    public static final boolean startv3mobileActivity(Context context, String url) {
        try {
            Intent intent = null;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException ex) {
                // TODO: handle exception
                return false;
            }

            if (context.getPackageManager().resolveActivity(intent, 0) == null) {
                String packagename = intent.getPackage();
                if (packagename != null) {
                    Uri uri = Uri.parse("market://details?id=" + packagename);
                    // 저희 모비페이 App 패키지명은 “com.hanaskcard.paycla” 입니다.
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                    return true;
                }
            }

            int runType = 43;
            if (runType == 43) {
                Uri uri = Uri.parse(intent.getDataString());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else {
                Uri uri = Uri.parse(url);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }

        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static final boolean startExternalActivity(Context context, String url,
                                                      int notFoundMessageId) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
