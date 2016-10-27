package com.ak.android.akmall.common;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ak.android.akmall.R;


public class CommonDialog extends Dialog implements View.OnClickListener, DialogInterface.OnKeyListener{
	
	private boolean isOk = false;
	private OnDismissListener onDismissListener;
	
	private TextView messageTextView;
	private TextView yesTextView;
	private TextView noTextView;
	private View spaceView;
	
	private Button yesButton;
	private Button noButton;
	
	private RelativeLayout noLayout;
	
	public CommonDialog(Context context, boolean isOnlyOk, String message, String yesText, String noText) {
		// TODO Auto-generated constructor stub
		super(context, R.style.transparentView);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		/** Design the dialog in main.xml file */ 
		
		
		setContentView(R.layout.common_dialog);
		
		this.messageTextView = (TextView) findViewById(R.id.messageTextView);
		this.yesButton = (Button) findViewById(R.id.yesButton);
		this.yesButton.setOnClickListener(this);
		this.yesTextView = (TextView) findViewById(R.id.yesTextView);
		this.noButton = (Button) findViewById(R.id.noButton);
		this.spaceView = (View) findViewById(R.id.spaceView);
		this.noLayout = (RelativeLayout) findViewById(R.id.noLayout);
		this.noButton.setOnClickListener(this);
		this.noTextView = (TextView) findViewById(R.id.noTextView);
		
		this.messageTextView.setText(message);
		this.yesTextView.setText(yesText);
		this.noTextView.setText(noText);
		
		if(isOnlyOk) {
			this.spaceView.setVisibility(View.GONE);
			this.noLayout.setVisibility(View.GONE);
		}
	}

	public boolean IsOk() {
		return this.isOk;
	}
	
	public void setOnDismissListener(OnDismissListener onDismissListener ) {  
        this.onDismissListener = onDismissListener;  
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.yesButton:
			this.isOk = true;
			if(this.onDismissListener == null) {} else  {
	    		this.onDismissListener.onDismiss(this);
			}
			dismiss();
			break;
		case R.id.noButton:
			this.isOk = false;
			if(this.onDismissListener == null) {} else  {
	    		this.onDismissListener.onDismiss(this);
			}
			dismiss();
			break;
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
