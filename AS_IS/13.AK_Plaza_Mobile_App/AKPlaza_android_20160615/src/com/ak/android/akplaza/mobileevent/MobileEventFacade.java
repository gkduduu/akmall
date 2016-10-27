package com.ak.android.akplaza.mobileevent;

import com.ak.android.akplaza.mobileevent.activity.PhotoEventActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MobileEventFacade {

	private static final String SCHEME_AKPLAZA = "akplaza";
	private static final String PATH_MOBILE_EVENT = "/mevent/";

	public static final String ACTION_ENTRY = "com.ak.android.akplaza.intent.action.ENTRY";
	public static final String ACTION_ENTRY_MODIFY = "com.ak.android.akplaza.intent.action.ENTRY_MODIFY";

	private static final String PARAM_ACT_JOIN_FORM = "viewEventJoinForm";
	private static final String PARAM_ACT_UPDATE_FORM = "updateEntryForm";

	private static final String TYPE_CD_PHOTO = "photo";

	private Context mContext;

	public MobileEventFacade(Context context) {
		mContext = context;
	}

	public boolean handleMobileEvent(Uri uri) {

		if(!isMobileEventUri(uri)) {
			return false;
		}

		return actionMobileEvent(uri);
	}

	/**
	 * scheme와 path를 확인한다.
	 * valid uri example
	 * akplaza://app/mevent/?act=viewEventJoinForm&type_cd=photo&event_index=39&event_token=cqChBCXOEwWsIJV
	 */
	private boolean isMobileEventUri(Uri uri) {
		return SCHEME_AKPLAZA.equals(uri.getScheme()) && PATH_MOBILE_EVENT.equals(uri.getPath());
	}

	private boolean actionMobileEvent(Uri uri) {

		String act = uri.getQueryParameter("act");

		if(PARAM_ACT_JOIN_FORM.equals(act)) {
			return startEventJoinActivity(uri);
		} else if(PARAM_ACT_UPDATE_FORM.equals(act)) {
			return startEventUpdateActivity(uri);
		}
		// else if ...

		return false;
	}

	private boolean startEventUpdateActivity(Uri uri) {
		return startMobileEvnetActivity(ACTION_ENTRY_MODIFY, uri);
	}

	private boolean startEventJoinActivity(Uri uri) {
		return startMobileEvnetActivity(ACTION_ENTRY, uri);
	}

	private boolean startMobileEvnetActivity(String intentAction, Uri uri) {

		String type_cd = uri.getQueryParameter("type_cd");

		if(TYPE_CD_PHOTO.equals(type_cd)) {
			Intent intent = new Intent(mContext, PhotoEventActivity.class);
			intent.setData(uri);
			intent.setAction(intentAction);
			mContext.startActivity(intent);
			return true;
		}
		// else if ...

		return false;
	}
}
