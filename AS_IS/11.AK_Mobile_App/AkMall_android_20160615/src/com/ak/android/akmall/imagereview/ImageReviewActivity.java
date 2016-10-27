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
package com.ak.android.akmall.imagereview;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallAPI;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.CommonDialog;
import com.ak.android.akmall.common.Const;
import com.ak.android.akmall.common.ProgressBarManager;
import com.ak.android.akmall.util.ImageUtils;
import com.ak.android.akmall.util.TempFileCreator;
import com.ak.android.akmall.widget.NavigationTabView;

public class ImageReviewActivity extends Activity implements OnClickListener,
		OnRatingBarChangeListener {

	public static final String TAG = "ImageReviewActivity";
	public static final boolean DBG = false;

	private Uri mImageCaptureUri;
	private static final int REQUEST_PICK_FROM_CAMERA = 0;
	private static final int REQUEST_PICK_FROM_ALBUM = 1;

	private InputMethodManager imm = null; // 키보드 input

	private ProgressBarManager mProgressBarManager;
	private ImageView m_Img_uplod_btn = null; // 이미지 업로드 버튼
	private ImageButton m_bnt_appraisal = null; // 구매만족도 평가하기
	private LinearLayout m_ImageReview_rating = null; // 상품평가 Ratingbar
	private Button m_bnt_appraisal_cancel = null; // 상품평가 Ratingbar 취소
	private Button m_bnt_appraisal_ok = null; // 상품평가 Ratingbar 평가
	private EditText m_editText = null; // 상품평 editText
	private ImageButton m_appraisal_benefit = null; // 상품평 혜택 조회
	private NavigationTabView mTab;

	private RatingBar m_Eval1 = null;
	private RatingBar m_Eval2 = null;
	private RatingBar m_Eval3 = null;
	private RatingBar m_Eval4 = null;

	private int eval1_rating = 0;
	private int eval2_rating = 0;
	private int eval3_rating = 0;
	private int eval4_rating = 0;

	private String mClassCd = "";
	private String mGoodCd = "";
	private String mImageReviewURL = "";
	private String mContinueUrl = "";

	private File imgFile = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialView();
		init();
	}

	@Override
	protected void onDestroy() {

		if (imgFile != null && imgFile.exists()) {
			imgFile.delete();
		}

		super.onDestroy();
	}

	private void initialView() {
		setContentView(R.layout.akimagereview);

		m_ImageReview_rating = (LinearLayout) findViewById(R.id.product_write_rating);
		m_bnt_appraisal = (ImageButton) findViewById(R.id.bnt_appraisal);
		m_bnt_appraisal_cancel = (Button) findViewById(R.id.bnt_appraisal_cancel);
		m_bnt_appraisal_ok = (Button) findViewById(R.id.bnt_appraisal_ok);
		m_Img_uplod_btn = (ImageView) findViewById(R.id.bnt_img_uplod);
		m_editText = (EditText) findViewById(R.id.editText);
		m_Eval1 = (RatingBar) findViewById(R.id.RatingBar01);
		m_Eval2 = (RatingBar) findViewById(R.id.RatingBar02);
		m_Eval3 = (RatingBar) findViewById(R.id.RatingBar03);
		m_Eval4 = (RatingBar) findViewById(R.id.RatingBar04);
		m_appraisal_benefit = (ImageButton) findViewById(R.id.appraisal_benefit);
		m_appraisal_benefit.setOnClickListener(this);
		mProgressBarManager = new ProgressBarManager(this);
		((ViewGroup) findViewById(R.id.image_review)).addView(mProgressBarManager);

		// setup title
		((TextView) findViewById(R.id.header_title)).setText(R.string.review_write);
		Button btnSave = (Button) findViewById(R.id.btnAction);
		btnSave.setVisibility(View.VISIBLE);
		btnSave.setOnClickListener(this);
		findViewById(R.id.btnPrev).setOnClickListener(this);

		//setupTab();

	}

	private void setupTab() {
		mTab = (NavigationTabView) findViewById(R.id.bottomNaviTab);
		mTab.setBackwardEnabled(true).setForwardEnabled(false).setScrollTopEnabled(false)
				.setBackwardOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	public void init() {
		loginCheck();
		setOnclickListener();
		getIntestData();
	}

	private void loginCheck() {
		if (!AkMallAPI.isLogin(this)) {
			AkMallFacade.startLoginActivityForResult(this);
		}
	}

	// 팝업 뜰때 EditText 활성,비활성화
	private void setVisility() {
		if (m_ImageReview_rating.isShown() == false) {
			m_ImageReview_rating.setVisibility(View.VISIBLE);
			m_editText.setEnabled(false);
		} else {
			m_ImageReview_rating.setVisibility(View.GONE);
			m_editText.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btnPrev:
			finish();
			break;

		case R.id.bnt_appraisal:
			showAppraisalRating();
			break;

		case R.id.appraisal_benefit:
			showAppraisalBenefit();
			break;

		case R.id.bnt_appraisal_cancel:
			cancelAppraisalRating();
			break;

		case R.id.bnt_appraisal_ok:
			setAppraisalRating();
			break;

		case R.id.bnt_img_uplod:
			img_upload_Dialog();
			break;

		case R.id.btnAction:
			startPostReview();
			break;
		}
	}

	@Override
	public void finish() {
		AkMallFacade.startMainActivity(this, Uri.parse(mContinueUrl), false);
		super.finish();
	}

	public void startPostReview() {

		final ProgressDialog dialog = new ProgressDialog(ImageReviewActivity.this);
		dialog.setMessage(getString(R.string.review_posting));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				saveImageReview(dialog);
			}
		});
		t.start();
	}

	public void saveImageReview(final ProgressDialog dialog) {

		// http://203.241.162.79/goods/goods.do?act=insertGoodsCommentDo
		// 이미지 업로드 저장
		String imageReview_data = m_editText.getText().toString();
		String imageReview_title = "";
		if (eval1_rating == 0 || eval2_rating == 0 || eval3_rating == 0 || eval4_rating == 0) {
			showMessageDialog(R.string.required_rating, false);
			return;
		}

		if (imageReview_data.length() < 1) {
			showMessageDialog(R.string.required_content, false);
			return;
		} else if (imageReview_data.length() >= 16) {
			imageReview_title = imageReview_data.substring(0, 16);
		} else {
			imageReview_title = imageReview_data;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.show();
			}
		});

		String result = AkMallAPI.writeImageReview(this, imageReview_data, imageReview_title,
				eval1_rating, eval2_rating, eval3_rating, eval4_rating, mClassCd, mGoodCd, imgFile);

		dialog.dismiss();

		int message;
		boolean postFinish = false;
		if (result.equals("success")) {
			message = R.string.review_post_success;
			setResult(RESULT_OK);
			postFinish = true;
		} else if (result.equals("already")) {
			message = R.string.review_post_already;
		} else {
			message = R.string.review_post_error;
		}

		showCompleteDialog(message, postFinish);
	}

	private void showMessageDialog(final int messageId, final boolean finish) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AkMallFacade.showMessageDialog(ImageReviewActivity.this, messageId, finish);
			}
		});
	}

	private void showCompleteDialog(final int message, final boolean finish) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				CommonDialog commonDialog = new CommonDialog(ImageReviewActivity.this, true, 
		    			getString(message), getString(android.R.string.ok), "");
				commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						InputMethodManager mgr = (InputMethodManager) ImageReviewActivity.this
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.hideSoftInputFromWindow(m_editText.getWindowToken(), 0);
						if (finish) {
							setResult(RESULT_OK);
							finish();
						}
					}
				});
				commonDialog.show(); 				
			}
		});
	}

	private void setAppraisalRating() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(m_editText, 0);
		mTab.setVisibility(View.VISIBLE);
		setVisility();
	}

	private void cancelAppraisalRating() {
		eval1_rating = 0;
		eval2_rating = 0;
		eval3_rating = 0;
		eval4_rating = 0;

		m_Eval1.setRating(1);
		m_Eval2.setRating(1);
		m_Eval3.setRating(1);
		m_Eval4.setRating(1);

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(m_editText, 0);
		mTab.setVisibility(View.VISIBLE);
		setVisility();
	}

	private void showAppraisalBenefit() {
		String url = Const.URL_BASE + getString(R.string.uri_address) + getString(R.string.goodsCommentBenefit);
		AkMallFacade.startWebPopupActivity(this, url, null);
	}

	private void showAppraisalRating() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(m_editText.getWindowToken(), 0);
		mTab.setVisibility(View.GONE);
		setVisility();
	}

	// RatingBar Check!!!
	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

		eval1_rating = (int) m_Eval1.getRating();
		eval2_rating = (int) m_Eval2.getRating();
		eval3_rating = (int) m_Eval3.getRating();
		eval4_rating = (int) m_Eval4.getRating();

		if (ratingBar.equals(m_Eval1)) {
			if (m_Eval1.getRating() != eval1_rating) {
				m_Eval1.setRating(eval1_rating);
			}
		}
		if (ratingBar.equals(m_Eval2)) {
			if (m_Eval2.getRating() != eval2_rating) {
				m_Eval2.setRating(eval2_rating);
			}
		}
		if (ratingBar.equals(m_Eval3)) {
			if (m_Eval3.getRating() != eval3_rating) {
				m_Eval3.setRating(eval3_rating);
			}
		}
		if (ratingBar.equals(m_Eval4)) {
			if (m_Eval4.getRating() != eval4_rating) {
				m_Eval4.setRating(eval4_rating);
			}
		}

	}

	private void getIntestData() {
		Intent intent = getIntent();
		mImageReviewURL = intent.getStringExtra("ImageReViewURL");
		mImageReviewURL.replaceAll("insertGoodsComment", "insertGoodsCommentDo");
		mContinueUrl = mImageReviewURL.replace("insertGoodsComment", "goodsComment");

		String[] param = mImageReviewURL.split("&");
		for (int i = 0; i < param.length; i++) {
			if (param[i].startsWith("disp_class_cd")) {
				// Log.d("param", param[i]);

				mClassCd = param[i];
			} else if (param[i].startsWith("goods_cd")) {
				// Log.d("param", param[i]);
				mGoodCd = param[i];
			}
		}
	}

	public void setOnclickListener() {
		m_bnt_appraisal.setOnClickListener(this);
		m_bnt_appraisal_cancel.setOnClickListener(this);
		m_bnt_appraisal_ok.setOnClickListener(this);
		m_Img_uplod_btn.setOnClickListener(this);
		// m_Camera_btn.setOnClickListener(this);

		m_Eval1.setOnRatingBarChangeListener(this);
		m_Eval2.setOnRatingBarChangeListener(this);
		m_Eval3.setOnRatingBarChangeListener(this);
		m_Eval4.setOnRatingBarChangeListener(this);

	}

	/**
	 * 카메라 찍기
	 */
	private void doTakePhotoAction() {

		mImageCaptureUri = TempFileCreator.getOutputMediaFileUri(TempFileCreator.MEDIA_TYPE_IMAGE);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		// 특정 기기에서 이미지 저장 오류로 인해 주석처리
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_PICK_FROM_CAMERA);
	}

	/**
	 * 앨범 불러오기
	 */
	private void doTakeAlbumAction() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, REQUEST_PICK_FROM_ALBUM);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case REQUEST_PICK_FROM_ALBUM: {
			if (resultCode == RESULT_OK && data != null) {

				mImageCaptureUri = data.getData();
				imgFile = new File("/sdcard/temp.jpg");

				if (resizeImage()) {
					m_Img_uplod_btn.setImageURI(mImageCaptureUri);
				} else {
					Log.w(TAG, "fail camera image resize...");
					m_Img_uplod_btn.setImageResource(0);
					imgFile = null;
				}
			}
			break;
		}

		case REQUEST_PICK_FROM_CAMERA: {

			if (resultCode != RESULT_OK) {
				break;
			}

			imgFile = new File("/sdcard/temp.jpg");
			if (resizeImage()) {
				m_Img_uplod_btn.setImageURI(Uri.fromFile(imgFile));
			} else {
				Log.w(TAG, "fail album image resize...");
				m_Img_uplod_btn.setImageResource(0);
				imgFile = null;
			}

			break;
		}

		case AkMallFacade.REQUEST_LOGIN:
			if (RESULT_OK != resultCode) {
				Toast.makeText(this, R.string.required_login, Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				finish();
			}
			break;
		}
	}

	private boolean resizeImage() {
		try {
			if(ImageUtils.resizeImage(this, mImageCaptureUri, imgFile, 1024, 1024)) {
				return true;
			} else {
				AkMallFacade.showMessageDialog(this, R.string.not_attachment_image, false);
				return false;
			}
		} catch (OutOfMemoryError e) {
			Log.e(TAG, e.getMessage(), e);
			AkMallFacade.showMessageDialog(this, R.string.not_attachment_image_by_out_of_memory, false);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			AkMallFacade.showMessageDialog(this, R.string.not_attachment_image_by_not_found, false);
		}

		return false;
	}

	// 업로드 다이얼로그
	public void img_upload_Dialog() {

		new AlertDialog.Builder(ImageReviewActivity.this).setTitle(R.string.select_image)
				.setItems(R.array.image_upload, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							doTakePhotoAction();
						} else {
							doTakeAlbumAction();
						}
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						mgr.showSoftInput(m_editText, InputMethodManager.SHOW_IMPLICIT);
					}
				}).show();

	}

}
