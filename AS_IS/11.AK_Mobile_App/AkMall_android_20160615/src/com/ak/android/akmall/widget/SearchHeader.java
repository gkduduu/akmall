package com.ak.android.akmall.widget;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.util.UriProvider;

public class SearchHeader extends LinearLayout implements OnClickListener, OnEditorActionListener,
		OnFocusChangeListener {

	public static final String TAG = "SearchHeader";
	public static final boolean DBG = false;

	private ImageView mLogoImageView;
	private EditText mSearchEditText;
	private ImageButton mQrScanButton;
	private ImageButton mSearchCancelButton;
	private ImageButton mClearTextButton;

	private OnHeaderActionListener mActionListener;
	private Context mContext;

	private boolean mQueryWasEmpty = true;
	private boolean mUpdateSearchKeyword = true;

	private boolean mIsSearchMode = false;
	private Uri mMainUri;

	private int mLogoImageViewOrgiWidth;

	public SearchHeader(Context context) {
		super(context);
		initial(context);
	}

	public SearchHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial(context);
	}

	private void initial(Context context) {
		mContext = context;
		mMainUri = UriProvider.getInstance(context).getUri(UriProvider.URI_MAIN);
		initialView(context);
	}

	private void initialView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.search_header, this);

		mLogoImageView = (ImageView) findViewById(R.id.ivLogo);
		mSearchEditText = (EditText) findViewById(R.id.etSearch);
		mQrScanButton = (ImageButton) findViewById(R.id.btnQrScan);
		mSearchCancelButton = (ImageButton) findViewById(R.id.btnSearchCancel);
		mClearTextButton = (ImageButton) findViewById(R.id.btnClearText);

		mQrScanButton.setOnClickListener(this);
		mSearchCancelButton.setOnClickListener(this);
		mClearTextButton.setOnClickListener(this);
		mLogoImageView.setOnClickListener(this);

		mSearchEditText.setOnEditorActionListener(this);
		mSearchEditText.setOnFocusChangeListener(this);
		mSearchEditText.addTextChangedListener(new SearchTextWatcher());
		if(!Const.IS_PRODUCTION) {
		    //mSearchEditText.setHint(R.string.staging_hint);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH && mActionListener != null) {
			mActionListener.onSearch(getQuery());
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnQrScan:
			AkMallFacade.startQrCodeScanActivityForResult(mContext);
			break;

		case R.id.btnSearchCancel:
			setSearchMode(false);
			break;

		case R.id.btnClearText:
			mSearchEditText.setText("");
			setQueryMode(true);
			break;

		case R.id.ivLogo:
			AkMallFacade.startMainActivity(mContext, mMainUri, true);
			break;
		}
	}

	private void clearSearchQuery() {
		mUpdateSearchKeyword = false;
		mSearchEditText.setText("");
		mSearchEditText.clearFocus();
		mUpdateSearchKeyword = true;
	}

	public void setSearchMode(boolean enable) {

		mIsSearchMode = enable;

		if (enable) { // enable search mode

			if (DBG) Log.d(TAG, "enable search mode");
			toggleLogoImageView(false);
			mQrScanButton.setVisibility(GONE);
			mSearchCancelButton.setVisibility(VISIBLE);

		} else { // disable search mode

			if (DBG) Log.d(TAG, "disable search mode");
			toggleLogoImageView(true);
			mQrScanButton.setVisibility(VISIBLE);
			mSearchCancelButton.setVisibility(GONE);
			clearSearchQuery();
			hideInputMethod();
		}

		if (mActionListener != null) {
			mActionListener.onChangedSearchMode(enable);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (mIsSearchMode) {
				mActionListener.onQueryChanged(getQuery());
			} else {
				setSearchMode(true);
			}
		}
	}

	public void setActionListener(OnHeaderActionListener listener) {
		mActionListener = listener;
	}

	private InputMethodManager getInputMethodManager() {
		return (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void hideInputMethod() {
		InputMethodManager imm = getInputMethodManager();
		if (imm != null) {
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
		}
	}

	private void showInputMethod() {
		InputMethodManager imm = getInputMethodManager();
		if (imm != null) {
		    imm.showSoftInput(mSearchEditText, 0);
		}
	}

	public boolean isSearchMode() {
		return mIsSearchMode;
	}

    public boolean isQueryMode() {
        return mIsSearchMode && mSearchEditText.hasFocus();
    }

    public void setQueryMode(boolean enable) {
        if(enable) {
            mSearchEditText.requestFocus();
            showInputMethod();
        } else {
            mSearchEditText.clearFocus();
            hideInputMethod();
        }
    }

	public String getQuery() {
		CharSequence q = mSearchEditText.getText();
		return q == null ? "" : q.toString().trim();
	}

	public void setQuery(String query) {
		if (query == null) {
			query = "";
		}
		mUpdateSearchKeyword = false;
		mSearchEditText.setText(query);
		mSearchEditText.setSelection(query.length());
		mUpdateSearchKeyword = true;
	}

    private class SearchTextWatcher implements TextWatcher {

        private String beforeText;

        @Override
        public void afterTextChanged(Editable s) {
            if(beforeText != null && beforeText.contentEquals(s)) {
                return;
            }

            final boolean isEmpty = s.length() == 0;
            if (isEmpty != mQueryWasEmpty) {
                mQueryWasEmpty = isEmpty;
                updateClearButtonUi(isEmpty);
            }

            if (mActionListener != null && mUpdateSearchKeyword && mIsSearchMode) {
                mActionListener.onQueryChanged(s.toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

	private void updateClearButtonUi(boolean isQueryEmpty) {
		mClearTextButton.setVisibility(isQueryEmpty ? GONE : VISIBLE);
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (mIsSearchMode) {
				setSearchMode(false);
				return true;
			}
			break;
		}
		return false;
	}

	private void toggleLogoImageView(boolean show) {
		int newWidth;
		if(!show) {
			mLogoImageViewOrgiWidth = mLogoImageView.getWidth();
			newWidth = 0;
		} else {
			newWidth = mLogoImageViewOrgiWidth;
		}

		Animation a = new ResizeAnimation(mLogoImageView, newWidth);
		a.setInterpolator(mContext, android.R.anim.decelerate_interpolator);
		a.setDuration(200);
		mLogoImageView.startAnimation(a);
	}

	public interface OnHeaderActionListener {
		public void onSearch(String query);

		public void onQueryChanged(String query);

		public void onChangedSearchMode(boolean enable);
	}
}
