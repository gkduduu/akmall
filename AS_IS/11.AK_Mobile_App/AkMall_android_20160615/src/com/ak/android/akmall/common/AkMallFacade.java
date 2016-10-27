
package com.ak.android.akmall.common;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.activity.WebPopupActivity;
import com.ak.android.akmall.imagereview.ImageReviewActivity;
import com.ak.android.akmall.model.UserInfo;
import com.ak.android.akmall.my.LoginActivity;
import com.ak.android.akmall.qrcode.QrCodeScanActivity;
import com.ak.android.akmall.widget.AkMallWebView;


/**
 * Activity 연결 창구
 */
public class AkMallFacade {

	public static final String TAG = "AkMallFacade";
	public static final boolean DBG = false;

	public static final int REQUEST_LOGIN = 1001;
	public static final int REQUEST_IMAGE_REVIEW = 1002;
	public static final int REQUEST_SCAN_QRCODE = 1003;
	public static final int REQUEST_SCAN_BARCODE = 1004;

	public static final String HTTP_HEADER_APP_VERSION = "makm-version";
	public static final String HTTP_HEADER_APP_DEVICE = "makm-device";

	private static UserInfo sUserInfoCache = new UserInfo();

	public static final void startMainActivity(Context context, Uri data, boolean clearHistory) {
		Intent intent = new Intent(context, AkMallActivity.class);
		intent.setData(data);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra(AkMallActivity.EXTRA_CLEAR_HISTORY, clearHistory);
		context.startActivity(intent);
	}

	public static final void reloadMainActivity(Context context) {
		Intent intent = new Intent(context, AkMallActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.setAction(AkMallActivity.ACTION_RELOAD);
		context.startActivity(intent);
	}

	/**
	 * activity에서 호출한 경우 request code {@link #REQUEST_LOGIN}으로 로그인 엑티비티를 호출한다.
	 * 그외에는 일반 호출..
	 * 
	 * @param context
	 */
	public static final void startLoginActivityForResult(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		if (context instanceof Activity) {
			if (DBG)
				Log.d(TAG, "request login startActivityForResult");
			((Activity) context).startActivityForResult(intent, REQUEST_LOGIN);
		} else {
			if (DBG)
				Log.d(TAG, "request login startActivity");
			context.startActivity(intent);
		}
	}

	public static final void startImageReviewActivity(Context context, String imageReviewUrl) {

		Intent intent = new Intent(context, ImageReviewActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra("ImageReViewURL", imageReviewUrl);

		if (context instanceof Activity) {
			if (DBG)
				Log.d(TAG, "request write image review startActivityForResult");
			((Activity) context).startActivityForResult(intent, REQUEST_IMAGE_REVIEW);
		} else {
			if (DBG)
				Log.d(TAG, "request write image review startActivity");
			context.startActivity(intent);
		}
	}

	public static final void startWebPopupActivity(Context context, String url, String title) {
		Intent intent = new Intent(context, WebPopupActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.setData(Uri.parse(url));
		if (title != null) {
			intent.putExtra(WebPopupActivity.EXTRA_TITLE, title);
		}
		context.startActivity(intent);
	}

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

	public static final boolean startExternalActivity(Context context, String url,
			StartActivityCallback callback) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			callback.onActivityNotFound();
			return false;
		}
	}

	public static final boolean startExternalActivity(Context context, String url) {
		return startExternalActivity(context, url, R.string.not_found_other_app);
	}

	public static final AlertDialog showLoginDialog(final Context context) {
		AlertDialog dialog = new AlertDialog.Builder(context).setIcon(R.drawable.logo_small)
				.setTitle(R.string.empty).setMessage(R.string.required_login)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startLoginActivityForResult(context);
					}
				}).setNegativeButton(android.R.string.cancel, null).create();
		dialog.show();
		return dialog;
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

	public static Map<String, String> getAppInfoHttpHeaderMap(Context context) {
		// makm-version: 1.0.5-dev
		// makm-device: Android
	    Map<String, String> map = new HashMap<String, String>();
	    map.put(HTTP_HEADER_APP_VERSION, AkMallFacade.getVersionName(context));
	    map.put(HTTP_HEADER_APP_DEVICE, "Android");
	    return map;
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
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse(url));
						context.startActivity(intent);
					}
				}).setNegativeButton(android.R.string.cancel, null).show();
	}

	public static CommonDialog showMessageDialog(final Context context, int messageId,
			final boolean finish) {
		
		CommonDialog commonDialog = new CommonDialog(AkMallActivity.mainActivity, true, 
				context.getString(messageId), context.getString(android.R.string.ok), "");
		commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				CommonDialog commonDialog = (CommonDialog) dialog;
				if(commonDialog.IsOk()) {
					if (finish && context instanceof Activity) {
						((Activity) context).finish();
					}
				}
			}
		});
		commonDialog.show();  
		
		return commonDialog;
	}

	public static void showNetworkErrorDialog(final Context context) {
		CommonDialog commonDialog = new CommonDialog(context, true, 
				context.getString(R.string.network_error), context.getString(android.R.string.ok), context.getString(android.R.string.no));
		commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				CommonDialog commonDialog = (CommonDialog) dialog;
				if(commonDialog.IsOk()) {
					if (context instanceof Activity) {
						((Activity) context).finish();
					}
				}
			}
		});
		commonDialog.show();  
		
	}

	public static UserInfo getUserInfo(Context context) {
		if (!sUserInfoCache.isAuth() && AkMallAPI.isLogin(context)) {
			sUserInfoCache = AkMallAPI.getUserInfo(context);
		}

		return sUserInfoCache;
	}

	public static final void startQrCodeScanActivityForResult(Context context) {
		Intent intent = new Intent(context, QrCodeScanActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		if (context instanceof Activity) {
			if (DBG) {
				Log.d(TAG, "request qrcode scan startActivityForResult");
			}
			((Activity) context).startActivityForResult(intent, REQUEST_SCAN_QRCODE);
		} else {
			if (DBG) {
				Log.d(TAG, "request qrcode scan startActivity");
			}
			context.startActivity(intent);
		}
	}
	
	public static final void startBarCodeScanActivityForResult(Context context) {
		Intent intent = new Intent(context, QrCodeScanActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.putExtra(QrCodeScanActivity.EXTRA_SCAN_MODE, QrCodeScanActivity.SCAN_MODE_BARCODE);
		if (context instanceof Activity) {
			if (DBG) {
				Log.d(TAG, "request qrcode scan startActivityForResult");
			}
			((Activity) context).startActivityForResult(intent, REQUEST_SCAN_BARCODE);
		} else {
			if (DBG) {
				Log.d(TAG, "request qrcode scan startActivity");
			}
			context.startActivity(intent);
		}
	}
	

	public static final boolean startv3mobileActivity(Context context, String url) {
		try {
			 if (DBG) {
	                Log.d(TAG, "startv3mobileActivity: " + url);
	            }
			Intent intent = null;
			try {
				intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); 
				Log.e("intent getScheme +++===>", intent.getScheme()); 
				Log.e("intent getDataString +++===>", intent.getDataString());
			} catch (URISyntaxException ex) {
				// TODO: handle exception
				Log.e("Browser", "Bad URI " + url + ":" + ex.getMessage());
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
			
			int runType=43;
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
			Log.e("error ===>", e.getMessage()); 
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
