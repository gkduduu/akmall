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

import java.util.ArrayList;

import android.app.Activity;
 
public class ActivityTaskManager
{
    private static  ActivityTaskManager  instance = null;
    private ArrayList<Activity>   mActivityArr;
     
    private ActivityTaskManager()
    {
        // Constructor
        mActivityArr = new ArrayList<Activity>();
    }
     
    public static ActivityTaskManager getInstance()
    {
        if(ActivityTaskManager.instance == null) {
            synchronized(ActivityTaskManager.class) {
                if(ActivityTaskManager.instance == null) {
                	ActivityTaskManager.instance = new ActivityTaskManager();
                }
            }
        }
        return ActivityTaskManager.instance;
    }
     
    /**
     * Activity 추가 Method
     * @param activity
     */
    public void addActivity(Activity activity)
    {
        if(!isActivity(activity))
            mActivityArr.add(activity);
    }
     
    /**
     * Activity 삭제 Method
     * @param activity
     */
    public void deleteActivity(Activity activity)
    {
        if(isActivity(activity)) {
            activity.finish();
            mActivityArr.remove(activity);
        }
    }
     
    /**
     * Parameter로 들어온 Activity가 현재 리스트에 있는지 체크하는 Method
     * @param activity
     * @return
     */
    public boolean isActivity(Activity activity)
    {
        for(Activity chkActivity:mActivityArr) {
            if(chkActivity == activity)
                return true;
        }
        return false;
    }
     
    /**
     * 등록되어 있는 모든 Activity 종료
     */
    public void allEndActivity()
    {
        for(Activity activity:mActivityArr) {
            activity.finish();
        }
    }
}
