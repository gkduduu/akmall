package com.ak.android.akplaza.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class EqualMarginLinearLayout extends LinearLayout {

	public static final String TAG = "EqualMarginLinearLayout";

	public EqualMarginLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial();
	}

	public EqualMarginLinearLayout(Context context) {
		super(context);
		initial();
	}

	private void initial() {
		setChildPosition();
	}

	private void setChildPosition() {

		final int childCount = getChildCount();
		final int childWidthSpace = getMeasuredWidth();
		int childTotalWidth = 0;
		int childDevideWidth = 0;

		for(int i = 0; i < childCount; i++) {
			childTotalWidth += getChildAt(i).getMeasuredWidth();
		}

		if(childWidthSpace > childTotalWidth) {
			childDevideWidth = (childWidthSpace - childTotalWidth) / (childCount + 1);
		}

		View child;
		for(int i = 0, leftOffset = childDevideWidth; i < childCount; i++, leftOffset += childDevideWidth) {
			child = getChildAt(i);
			child.layout(child.getLeft() + leftOffset, child.getTop(), child.getRight() + leftOffset, child.getBottom());
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		setChildPosition();
	}
}
