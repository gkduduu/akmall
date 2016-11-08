package android.ak.com.akmall.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gkdud on 2016-08-02.
 */
public class SharedPreferencesManager {

    public static void initSP(Context context) {
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("bcSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("bcSP", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences("bcSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("bcSP", Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

}