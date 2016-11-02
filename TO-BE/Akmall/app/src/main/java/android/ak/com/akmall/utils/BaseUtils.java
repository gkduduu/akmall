package android.ak.com.akmall.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
