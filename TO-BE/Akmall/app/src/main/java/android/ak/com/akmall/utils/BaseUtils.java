package android.ak.com.akmall.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

        if (str == null) {
            str = "";
        }
        return str;
    }

    /**
     * 콤마 찍어버리기~~
     * @param won
     * @return
     */
    public static String wonFormat(String won) {
        if(won.equals("") || null == won) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(Integer.parseInt(won));
    }
    public static String wonFormat(int won) {
        if(won == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(won);
    }
}
