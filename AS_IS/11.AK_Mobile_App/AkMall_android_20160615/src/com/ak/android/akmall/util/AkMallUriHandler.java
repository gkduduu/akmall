package com.ak.android.akmall.util;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.ak.android.akmall.activity.AkMallActivity;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.Const;

public class AkMallUriHandler{
	
	private AkMallUriHandler() {

	}
	
	/**
	 * akmall:// 스키마 처리.
	 * 로그인 설정 : akmall://app/mypage/setting.do?act=viewLogin
	 * 로그아웃 : akmall://app/mypage/setting.do?act=doLogout
	 * SNS 설정 : akmall://app/mypage/setting.do?act=viewSnsSetting
	 * 알림 설정 : akmall://app/mypage/setting.do?act=viewNotiSetting
	 * 알림 보관함 : akmall://app/mypage/setting.do?act=viewNotiBox
	 * @param uri
	 * @return
	 */
	public static boolean handleInternalUri(Context context, String url) {

		if(!url.startsWith(UriProvider.SCHEME_AKMALL)) {
			return false;
		}
		
		Uri uri = Uri.parse(url);
		String path = uri.getPath();

		if(isMypagePath(path)) {

			String controller = uri.getLastPathSegment();
			String act = uri.getQueryParameter("act");

			if("setting.do".equals(controller)) {

				if("viewLogin".equals(act)) {
					AkMallFacade.startLoginActivityForResult(context);
					return true;

				} else if("doLogout".equals(act)) {

					if(AkMallAPI.doLogout(context)) {
						AkMallFacade.reloadMainActivity(context);
					} else {
						Toast.makeText(context, R.string.logout_fail, Toast.LENGTH_LONG).show();
					}
					return true;

				} else if("viewNotiSetting".equals(act)) {
					/*
					Intent intent = new Intent(context, NotiSetActivity.class);
					context.startActivity(intent);*/
					AkMallActivity.showNotiSetView();
					return true;

				} else if("viewNotiBox".equals(act)) {
					AkMallActivity.showNotiBoxView("");				
					return true;

				}
			} // setting controller..
		} // isMypagePath

		return false;
	}

	/**
	 * 외부(웹브라우저, 타 앱)로 부터의 호출 처리..
	 * @param uri
	 * @param context
	 * @return 처리되면 true, 처리하지 않았으면 false.
	 */
	public static boolean handleExternalUri(Uri uri, Context context) {

		if(!UriProvider.SCHEME_AKMALL.equals(uri.getScheme())) {
			return false;
		}

		String host = uri.getHost();
		String path = uri.getPath();
		String returnUrl = uri.getQueryParameter("returnUrl");

		if(isReturnToPaymentApp(host, path, returnUrl)) {
			AkMallFacade.startMainActivity(context, Uri.parse(returnUrl), false);
			return true;
		}

		if(isViewLogin(path)) {
//앱 로그인 화면에서 웹 로그인화면으로 변환 (오창욱 2014.09.24)
//			AkMallFacade.startLoginActivityForResult(context);
			AkMallFacade.startMainActivity(context, Uri.parse(Const.URL_BASE+"/login/Login.do"), false);
			return true;
		}

		if(isViewHome(path)) {
			AkMallFacade.startMainActivity(context, UriProvider.getInstance(context).getUri(UriProvider.URI_MAIN), true);
			return true;
		}

		return false;
	}

	private static boolean isViewHome(String path) {
		return "/home/".equals(path);
	}

	private static boolean isViewLogin(String path) {
		return "/login/".equals(path);
	}

	private static boolean isReturnToPaymentApp(String host, String path, String returnUrl) {
		return (host == null || host.length() == 0)
				&& (path == null || path.length() ==0)
				&& (returnUrl != null && returnUrl.length() != 0);
	}

	private static boolean isMypagePath(String path) {
		return path.startsWith("/mypage");
	}
}
