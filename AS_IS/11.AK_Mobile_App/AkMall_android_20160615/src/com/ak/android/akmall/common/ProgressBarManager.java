package com.ak.android.akmall.common;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ProgressBarManager extends ProgressBar implements Runnable{

	private ProgressBarManager manger  = null;
	private boolean isAbleKeyEvent  = true;
	private String[] excuteMethodName = null;
	private String   cancelMethodName = null;
	private Object targetObject     = null;


	public ProgressBarManager(Context context) {
		super(context);
		init();
	}


	public ProgressBarManager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressBarManager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	private void init(){
		RelativeLayout.LayoutParams itemTvNoteParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        itemTvNoteParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(itemTvNoteParam);

		this.setVisibility(View.GONE);
		manger = this;
	}

	public void startProgress()
	{
		this.setVisibility(View.VISIBLE);
		new Thread(this).start();
	}

	public void stopProgress()
	{
		this.setVisibility(View.GONE);
	}

	@Override
	public void run() {
		try
		{
			if (targetObject != null)
			{
				if (excuteMethodName != null)
				{
					Class targetClass = targetObject.getClass();
					String temp = excuteMethodName[0];
					command(temp);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		handle.sendEmptyMessage(0);
	}

	public void command(String cmd) throws Exception
	{
		Class targetClass = targetObject.getClass();
		Method method = targetClass.getMethod(cmd, null);
		method.invoke(targetObject, null);
	}

	public void chageStaus(int status)
	{
		int current = manger.getProgress();
		try
		{
			for (int i = current ; i <= status ; i++)
			{
				manger.setProgress(i);
				Thread.sleep(50);
			}
		}
		catch(Exception ex)
		{
		}
	}

	public void setCommandClass(Object targetObj)
	{
		targetObject = targetObj;
	}


	public void setCommandClass(Object targetObj, String[] excuteMethod)
	{
		setCommandClass(targetObj);
		excuteMethodName = excuteMethod;
	}

	public void setCommandClass(Object targetObj, String[] excuteMethod, String cancelMethod)
	{
		setCommandClass(targetObj);
		excuteMethodName = excuteMethod;
		cancelMethodName = cancelMethod;
	}

	public void ableKeyEvent(boolean is)
	{
		isAbleKeyEvent = is;
	}

	final Handler handle = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			try
			{
				if (excuteMethodName != null)
				{
					for (int i = 1 ; i < excuteMethodName.length ; i++)
					{
						command(excuteMethodName[i]);
					}
				}
			}
			catch (Exception e)
			{

				e.printStackTrace();
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean result = false;
		if (isAbleKeyEvent)  result = super.onKeyDown(keyCode, event);
		if (keyCode == 4)
		{
			try
			{
				if (cancelMethodName != null) command(cancelMethodName);
				else command("finish");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		return result;
	}

}
