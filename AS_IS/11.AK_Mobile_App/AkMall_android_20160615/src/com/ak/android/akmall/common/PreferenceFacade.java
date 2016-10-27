package com.ak.android.akmall.common;

import com.ak.android.akmall.util.SharedUtil;

import android.content.Context;
import android.text.TextUtils;

public class PreferenceFacade {

	public static final int DISABLE = 0;
	public static final int ENABLE = 1;

	public static final String ON = "on";
	public static final String OFF = "off";

	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";

	public static String getSavededUserId(Context context) {
		return SharedUtil.getSharedString(context, "auto", "USERID");
	}

	public static boolean isAutoLogin(Context context) {
		return SharedUtil.getSharedInt(context, "auto", "AUTOLOGIN") == ENABLE;
	}

	public static boolean isSaveId(Context context) {
		return SharedUtil.getSharedInt(context, "auto", "IDSAVE") == ENABLE;
	}

	public static void setAutoLogin(Context context, String userid, String hashedPassword) {
		SharedUtil.setSharedInt(context, "auto", "AUTOLOGIN", ENABLE);
		SharedUtil.setSharedInt(context, "auto", "IDSAVE", DISABLE);
		SharedUtil.setSharedString(context, "auto", "USERID", userid);
		SharedUtil.setSharedString(context, "auto", "USERPW", hashedPassword);
	}

	public static void setSaveUserId(Context context, String userid) {
		SharedUtil.setSharedInt(context, "auto", "AUTOLOGIN", DISABLE);
		SharedUtil.setSharedInt(context, "auto", "IDSAVE", ENABLE);
		SharedUtil.setSharedString(context, "auto", "USERID", userid);
	}

	public static final void setAllowPush(Context context, boolean allow) {
		SharedUtil.setSharedString(context, "DEVICE", "push", allow ? ON : OFF);
	}

	public static final boolean isAllowPush(Context context) {
		return ON.equals(SharedUtil.getSharedString(context, "DEVICE", "push")) ? true : false;
	}
	
	public static final boolean isConfirmedAllowPush(Context context) {
		String push = SharedUtil.getSharedString(context, "DEVICE", "push");
		return !TextUtils.isEmpty(push);			
	}
}
