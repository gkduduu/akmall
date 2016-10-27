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
package com.ak.android.akplaza.common.sns.twitter;

import android.app.Activity;
import android.content.SharedPreferences;

public class Twitt_Util {
		   // 전체 어플에 공용으로 적용되는 SharedPreferences에 Key, Value로 값을 저장한다. Value는 String이다.
		  public static void setAppPreferences(Activity context, String key, String value)
		  {
		    SharedPreferences pref = null;
		    pref = context.getSharedPreferences(C.LOG_TAG, 0);
		    SharedPreferences.Editor prefEditor = pref.edit();
		    prefEditor.putString(key, value);
		    
		    prefEditor.commit();
		  }
		  
		  // 전체 어플에 공용으로 적용되는 SharedPreferences에서 String 값을 가져온다.  
		  public static String getAppPreferences(Activity context, String key)
		  {
		    String returnValue = null;
		    
		    SharedPreferences pref = null;
		    pref = context.getSharedPreferences(C.LOG_TAG, 0);
		    
		    returnValue = pref.getString(key, "");
		    
		    return returnValue;
		  }
		}

