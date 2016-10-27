
package com.ak.android.akplaza.common;

import org.json.JSONException;
import org.json.JSONObject;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.qrcode.QrCodeScanActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Activity 연결 창구
 */
public class AkPlazaFacade {

    public static final String TAG = "AkPlazaFacade";
    public static final boolean DBG = true;

    public static final String HTTP_HEADER_APP_VERSION = "makp-version";
    public static final String HTTP_HEADER_APP_DEVICE = "makp-device";

    public static final int REQUEST_SCAN_QRCODE = 1001;
    public static final int REQUEST_SCAN_BARCODE = 1002;

    public static final boolean startExternalActivity(Context context, String url,
            int notFoundMessageId) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, notFoundMessageId, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static final boolean startExternalActivity(Context context, String url) {
        return startExternalActivity(context, url, R.string.not_found_other_app);
    }

    public static final void startActivityForResult(Context context, Intent intent, int requestCode) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (context instanceof Activity) {
            log("request login startActivityForResult");
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            log("request login startActivity");
            context.startActivity(intent);
        }
    }

    public static String getVersionName(Context context) {
        String versionName = "1.0";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // won't happen
            Log.w(TAG, "don't get versionName.. so default versionName set 1.0..");
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // won't happen
            Log.w(TAG, "Don't get versionCode.. so default verionCode set 1..");
        }
        return versionCode;
    }

    public static String getAppInfoHttpHeaderString(Context context) {
        // makp-version: 1.0.5
        // makp-device: Android
        return String.format("%s: %s\n%s: %s", HTTP_HEADER_APP_VERSION,
                AkPlazaFacade.getVersionName(context), HTTP_HEADER_APP_DEVICE, "Android");
    }

    public static void showCallDialog(final Context context, final String url) {
        final String number = url.replace("tel:", "");
        if (number.length() == 0) {
            return;
        }

        new AlertDialog.Builder(context).setIcon(R.drawable.logo_small)
                .setTitle(R.string.confirm_call).setMessage(number)
                .setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                    }
                }).setNegativeButton(android.R.string.cancel, null).show();
    }

    public static AlertDialog showMessageDialog(final Context context, int messageId,
            final boolean finish) {
        AlertDialog dialog = new AlertDialog.Builder(context).setIcon(R.drawable.logo_small)
                .setTitle(R.string.empty).setMessage(messageId)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finish && context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }).setCancelable(false).create();

        dialog.show();
        return dialog;
    }

    public static void showNetworkErrorDialog(final Context context) {
        new AlertDialog.Builder(context).setIcon(R.drawable.logo_small).setTitle(R.string.alert)
                .setMessage(R.string.network_error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                }).setCancelable(false).show();
    }

    public static final void startQrCodeScanActivityForResult(Context context) {
        Intent intent = new Intent(context, QrCodeScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (context instanceof Activity) {
            log("request qrcode scan startActivityForResult");
            ((Activity) context).startActivityForResult(intent, REQUEST_SCAN_QRCODE);
        } else {
            log("request qrcode scan startActivity");
            context.startActivity(intent);
        }
    }

    public static final void startBarCodeScanActivityForResult(Context context) {
        Intent intent = new Intent(context, QrCodeScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(QrCodeScanActivity.EXTRA_SCAN_MODE, QrCodeScanActivity.SCAN_MODE_BARCODE);
        if (context instanceof Activity) {
            log("request barcode scan startActivityForResult");
            ((Activity) context).startActivityForResult(intent, REQUEST_SCAN_BARCODE);
        } else {
            log("request barcode scan startActivity");
            context.startActivity(intent);
        }
    }

    private static void log(String msg) {
        if (DBG) {
            Log.d(TAG, msg);
        }
    }

    public static void startMainActivity(Context context, Uri data) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setAction(WebViewActivity.ACTION_VIEW_URL);
        intent.setData(data);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }
    
    public static void checkVersion(final Context context){
		String strUrl = Const.URL_BASE+"/app/versionAndroid.jsp";
        String result = PostHttpClient.HttpConnect(strUrl);
        int versionCode = AkPlazaFacade.getVersionCode(context);
        try {
			JSONObject jsonResult = new JSONObject(result);
			int thisVersion = jsonResult.getInt("version");
			String msg = jsonResult.getString("msg");
			final String url = jsonResult.getString("url");
			if(thisVersion > versionCode) {
				AlertDialog.Builder ab = new AlertDialog.Builder(context);
				ab.setIcon(R.drawable.logo_small).create();
				ab.setTitle(" ").create();
				ab.setMessage(msg).create();
				ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri u = Uri.parse(url);
						intent.setData(u);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
						context.startActivity(intent);
						((Activity) context).finish();
					}
				}).create();
//				ab.setNegativeButton("아니오", null).create(); //업데이트해야만 사용할수있도록.
				ab.setCancelable(false); //백버튼 막기.
				ab.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
