/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ak.android.akplaza.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtil {

	public static String getSharedString(Context context, String name, String key){
		String result = null;
		SharedPreferences share = context.getSharedPreferences(name, 0);
		result = share.getString(key, "");
		return result;
	}


	public static String getSharedString(Context context, String name, String key, String defValue){
		String result = null;
		SharedPreferences share = context.getSharedPreferences(name, 0);
		result = share.getString(key, defValue);
		return result;
	}



	public static String getSharedString(Context context, String name, int mode, String key, String defValue){
		String result = null;
		SharedPreferences share = context.getSharedPreferences(name, mode);
		result = share.getString(key, defValue);
		return result;
	}

	public static boolean getSharedBoolean(Context context, String name, String key) {
		SharedPreferences share = context.getSharedPreferences(name, 0);
		return share.getBoolean(key, false);
	}


	public static int getSharedInt(Context context, String name, String key){
		int result = 0;
		SharedPreferences share = context.getSharedPreferences(name, 0);
		result = share.getInt(key, 0);
		return result;
	}

	public static int getSharedInt(Context context, String name, String key, int defValue){
		int result = 0;
		SharedPreferences share = context.getSharedPreferences(name, 0);
		result = share.getInt(key, defValue);
		return result;
	}

	public static int getSharedInt(Context context, String name, int mode, String key, int defValue){
		int result = 0;
		SharedPreferences share = context.getSharedPreferences(name, mode);
		result = share.getInt(key, defValue);
		return result;
	}

	public static void setSharedString(Context context, String name,  String key, String value){
		SharedPreferences share = context.getSharedPreferences(name, 0);
		Editor editor = share.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static void setSharedBoolean(Context context, String name,  String key, Boolean value){
		SharedPreferences share = context.getSharedPreferences(name, 0);
		Editor editor = share.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static void setSharedString(Context context, String name, int mode, String key, String value){
		SharedPreferences share = context.getSharedPreferences(name, mode);
		Editor editor = share.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void setSharedInt(Context context, String name,  String key, int value){
		SharedPreferences share = context.getSharedPreferences(name, 0);
		Editor editor = share.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setSharedInt(Context context, String name, int mode, String key, int value){
		SharedPreferences share = context.getSharedPreferences(name, mode);
		Editor editor = share.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void clearAllSetting(Context context, String name, int mode){
		SharedPreferences share = context.getSharedPreferences(name, mode);
		Editor editor = share.edit();
		editor.clear();
		editor.commit();
	}





}
