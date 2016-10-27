package com.ak.android.akmall.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {

	View mView;
	int mStartW;
	int mEndW;
	int mDiff;

	public ResizeAnimation(View v, int newW) {
		mView = v;
		mStartW = v.getWidth();
		mEndW = newW;
		mDiff = mEndW - mStartW;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		mView.getLayoutParams().width = mStartW + (int) (mDiff * interpolatedTime);
		mView.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}