/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ak.android.akmall.qrcode;

import java.io.IOException;
import java.util.Collection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.mtracker.mea.sdk.MTrackerManager;//p65458 20150716 mtracker 연동 add

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class QrCodeScanActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {

	private static final String TAG = "QrCodeScanActivity";
	
	public static final String EXTRA_SCAN_MODE = "extra.SCAN_MODE";
    public static final int SCAN_MODE_QRCODE = 1; // or null
    public static final int SCAN_MODE_BARCODE = 2;

	private CameraManager mCameraManager;
	private CaptureActivityHandler mHandler;
	private Result mSavedResultToShow;
	private ViewfinderView mViewfinderView;
	private boolean mHasSurface;
	private Collection<BarcodeFormat> mDecodeFormats;
	private InactivityTimer mInactivityTimer;
	private int mScanMode;
	private Handler mStopHandler;
	
	ViewfinderView getViewfinderView() {
		return mViewfinderView;
	}

	public Handler getHandler() {
		return mHandler;
	}

	CameraManager getCameraManager() {
		return mCameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.qrcode_capture);

		mHasSurface = false;
		mInactivityTimer = new InactivityTimer(this);
		mStopHandler = new Handler();
		
		Intent intent = getIntent();
        mScanMode = intent.getIntExtra(EXTRA_SCAN_MODE, SCAN_MODE_QRCODE);
		
		findViewById(R.id.btnPrev).setOnClickListener(this);
		TextView titleText = (TextView) findViewById(R.id.header_title);
		
		if (mScanMode == SCAN_MODE_BARCODE) {
            mDecodeFormats = DecodeFormatManager.ONE_D_FORMATS;
            mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
            titleText.setText(R.string.barcode_scan);
            mViewfinderView.setMsgText(R.string.msg_barcode_auto_scan);
        } else { // SCAN_MODE_QRCODE
            mDecodeFormats = DecodeFormatManager.QR_CODE_FORMATS;
            mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
            mViewfinderView.setMsgText(R.string.msg_qrcode_auto_scan);
        }

		/*mDecodeFormats = DecodeFormatManager.QR_CODE_FORMATS;
		mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mViewfinderView.setMsgText(R.string.msg_qrcode_auto_scan);*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		MTrackerManager.startActivityAnalyze(this);//p65458 20150716 mtracker 연동 add
		MTrackerManager.startActivityAnalyze(this, "AIzaSyCVzqZfdndlsivLh83zoe2Uol9MvOwtl4I");//p65458 20150720 mtracker 연동 add
		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		mCameraManager = new CameraManager(getApplication());
		mViewfinderView.setCameraManager(mCameraManager);

		mHandler = null;

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (mHasSurface) {
			// The activity was paused but not stopped, so the surface still exists.
			// Therefore surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		mInactivityTimer.onResume();
		startBarcodeScanStopper();

	}

	@Override
	protected void onPause() {
		if (mHandler != null) {
			mHandler.quitSynchronously();
			mHandler = null;
		}
		mInactivityTimer.onPause();
		mCameraManager.closeDriver();
		if (!mHasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		stopBarcodeScanStopper();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mInactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
			// Handle these events so they don't launch the Camera app
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (mHandler == null) {
			mSavedResultToShow = result;
		} else {
			if (result != null) {
				mSavedResultToShow = result;
			}
			if (mSavedResultToShow != null) {
				Message message = Message.obtain(mHandler, R.id.decode_succeeded, mSavedResultToShow);
				mHandler.sendMessage(message);
			}
			mSavedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!mHasSurface) {
			mHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 *
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {

        mInactivityTimer.onActivity();
        stopBarcodeScanStopper();
        ParsedResult result = ResultParser.parseResult(rawResult);

        if (mScanMode == SCAN_MODE_BARCODE) {
            handleBarcodeMode(rawResult, barcode, result);
        } else { // SCAN_MODE_QRCODE
            handleQrcodeMode(rawResult, barcode, result);
        }
    }

    private void handleBarcodeMode(Result rawResult, Bitmap barcode, ParsedResult result) {
        // This is from history -- no saved barcode
        if (barcode != null) {
            // beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, rawResult);
        }
        mViewfinderView.drawResultBitmap(barcode);
        Message message = createScnResultMessage(rawResult);
        mHandler.sendMessage(message);
    }

    private void handleQrcodeMode(Result rawResult, Bitmap barcode, ParsedResult result) {
        // only support uri type
        if (result.getType() != ParsedResultType.URI) {
            Toast.makeText(this, R.string.msg_unsupported_qrcode, Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // This is from history -- no saved barcode
        if (barcode != null) {
            // beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, rawResult);
        }

        mViewfinderView.drawResultBitmap(barcode);

        Message message = createScnResultMessage(rawResult);
        mHandler.sendMessage(message);

    }

    private Message createScnResultMessage(Result rawResult) {
        Intent intent = new Intent(getIntent().getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(ZxingIntents.Scan.RESULT, rawResult.toString());
        intent.putExtra(ZxingIntents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
            intent.putExtra(ZxingIntents.Scan.RESULT_BYTES, rawBytes);
        }
        Message message = Message.obtain(mHandler, R.id.return_scan_result);
        message.obj = intent;
        return message;
    }

	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of
	 * the barcode.
	 *
	 * @param barcode
	 *            A bitmap of the captured image.
	 * @param rawResult
	 *            The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2, barcode.getHeight() - 2);
			canvas.drawRect(border, paint);

			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
							.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			mCameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (mHandler == null) {
				mHandler = new CaptureActivityHandler(this, mDecodeFormats, null, mCameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(android.R.string.ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public void drawViewfinder() {
		mViewfinderView.drawViewfinder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPrev:
			finish();
			break;
		}
	}
	
	private void startBarcodeScanStopper() {
		mStopHandler.postDelayed(mBarcodeScanStopper, 10 * 1000);
    }

    private void stopBarcodeScanStopper() {
    	mStopHandler.removeCallbacks(mBarcodeScanStopper);
    }

    private Runnable mBarcodeScanStopper = new Runnable() {

        @Override
        public void run() {
            showRegReceiptConfirmDialog();
        }
    };

    private void showRegReceiptConfirmDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setIcon(R.drawable.logo_small).create();
        ab.setTitle(" ").create();
        ab.setMessage("정상적으로 인식이 되지 않았습니다. 다시 시도하시겠습니까?").create();
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
                startBarcodeScanStopper();
            }
        }).create();
        ab.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
        	@Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
            
        }).create();
        ab.show();
    }
}
