package com.ak.android.akplaza.mobileevent.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.AkPlazaAPI;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.ContentUtils;
import com.ak.android.akplaza.common.DrawableBackgroundDownloader;
import com.ak.android.akplaza.common.HeaderClient;
import com.ak.android.akplaza.common.WebViewActivity;
import com.ak.android.akplaza.login.LoginSubActivity;
import com.ak.android.akplaza.mobileevent.MobileEventFacade;
import com.ak.android.akplaza.mobileevent.model.PhotoEventResult;
import com.ak.android.akplaza.mobileevent.model.PhotoEventSource;
import com.ak.android.akplaza.ui.AttachmentImageSelectorAdapter;
import com.ak.android.akplaza.utils.TempFileCreator;

public class PhotoEventActivity extends Activity implements OnClickListener {

	public static final String TAG = "PhotoEventActivity";
	public static final boolean DBG = true;

	private static final String RESULT_SUCCESS = "success";

	public static final int REQUEST_CODE_ATTACH_IMAGE = 100;
    public static final int REQUEST_CODE_TAKE_PICTURE = 101;
	public static final int REQUEST_CODE_LOGIN = 110;

	private EditText mEtTitle;
	private EditText mEtContent;
	private ImageView mContentImage;
	private Button mBtnSubmit;
	private Button mBtnCancel;
	private Button mBtnSelectImage;

	private Dialog mProgressDialog;

	private Uri mImageFileUri;
	private String mAction;

	private AttachmentImageSelectorAdapter mAttachmentImageSelectorAdapter;

	private AkPlazaAPI mAPI;
	private PhotoEventSource mSource;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(DBG) Log.d(TAG, "onCreate");
	    super.onCreate(savedInstanceState);

	    initialView();
	    initialEventData();

	    mAPI = new AkPlazaAPI(this);
	    mAction = getIntent().getAction();

	    if(DBG) Log.d(TAG, "action is " + mAction);
	    
	    WebViewActivity.mIsReloadable = true;

	    if(MobileEventFacade.ACTION_ENTRY.equals(mAction)) {
	    	// nothing..
	    	// wait user action..
	    } else if(MobileEventFacade.ACTION_ENTRY_MODIFY.equals(mAction)) {
	    	loadEntryData();
	    	// wait user action..
	    } else {
	    	if(DBG) Log.d(TAG,"unknow action so finish... ");
	    	finish();
	    }
	}

	private void initialView() {
		setContentView(R.layout.photo_event);

		// load view
		mEtTitle = (EditText) findViewById(R.id.etTitle);
		mEtContent = (EditText) findViewById(R.id.etContent);
		mContentImage = (ImageView) findViewById(R.id.imgContent);
		mBtnSubmit = (Button) findViewById(R.id.btnSubmit);
		mBtnCancel = (Button) findViewById(R.id.btnCancel);
		mBtnSelectImage = (Button) findViewById(R.id.btnSelectImg);

		// set onclick listner
		mBtnSubmit.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mBtnSelectImage.setOnClickListener(this);
		
        WebView appheader = (WebView) findViewById(R.id.appheader);
        appheader.getSettings().setJavaScriptEnabled(true);
        HeaderClient headerClient = new HeaderClient();
        headerClient.setmContext(PhotoEventActivity.this);
        appheader.setWebViewClient(headerClient);
        String url = Const.URL_LIB + this.getString(R.string.act_header);
        appheader.loadUrl(url);
	}

	/**
	 * 삼성 갤럭시S, S2 모델에서 startActivityForResult 호출시 문제 발생.
	 */
	private void setContentImage(Bundle bundle) {
		if(bundle == null) {
			return;
		}
		mImageFileUri = bundle.getParcelable("mImageFileUri");
		mContentImage.setImageURI(mImageFileUri);
	}

	/**
	 * 이벤트 응모 정보를 조회하여 표시함.
	 * 이벤트 응모 정보를 조회할 수 없거나 오류가 있는 경우
	 * 다이얼로그로 알림.
	 */
	private void loadEntryData() {
		Thread loadEntryDataThread = new Thread(new Runnable() {
			@Override
			public void run() {
				showProgressDialog(R.string.event_entry_info_checking);
				final PhotoEventResult entryData = mAPI.getPhotoEventDetial(mSource.getEventIndex(), mSource.getEntryIndexno());
				final String result = entryData.getResult();


				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dismissProgressDialog();
						if(RESULT_SUCCESS.equals(result)) {
							String name = mSource.getName();
							String phone = mSource.getPhone();
							mSource = entryData.getEntry();
							mEtTitle.setText(mSource.getTitle());
							mEtContent.setText(mSource.getContent());
							mSource.setName(name);
							mSource.setPhone(phone);
							DrawableBackgroundDownloader downloader = new DrawableBackgroundDownloader();
							downloader.loadDrawable(mSource.getContentImageUri().toString(), mContentImage
									, getResources().getDrawable(R.drawable.logo));
						} else {
							showAlertDialog(R.string.alert, entryData.getMessage(), false);
						}
					}
				}); // ui thread
			} // thread
		});

		loadEntryDataThread.start();
	}

	private void initialEventData() {
		Intent intent = getIntent();
		Uri uri = intent.getData();
		mSource = new PhotoEventSource();
		mSource.setEventIndex(uri.getQueryParameter("event_index"));
		mSource.setEventToken(uri.getQueryParameter("event_token"));
		mSource.setEntryIndexno(uri.getQueryParameter("entry_indexno"));
		mSource.setName(uri.getQueryParameter("name"));
		mSource.setPhone(uri.getQueryParameter("phone"));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSelectImg :
			showAddImageAttachmentDialog();
			break;
		case R.id.btnSubmit :
			submitEventEntryData();
			break;
		case R.id.btnCancel :
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(DBG) Log.d(TAG, "onActivityResult intent data : " + data);

		switch (requestCode) {

		case REQUEST_CODE_TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
				Bitmap bitmap = getBitmapReSize(mImageFileUri.getPath());
				mContentImage.setImageBitmap(bitmap);
				mContentImage.setVisibility(View.VISIBLE);
			}
			break;

		case REQUEST_CODE_ATTACH_IMAGE:
			if (resultCode == RESULT_OK && data != null) {
				mImageFileUri = data.getData();
				Bitmap bitmap = getBitmapReSize(ContentUtils.getPath(this.getApplicationContext(), mImageFileUri));
				mContentImage.setImageBitmap(bitmap);
				mContentImage.setVisibility(View.VISIBLE);
			}
			break;

		case REQUEST_CODE_LOGIN:
			if (resultCode != RESULT_OK) {
				showAlertDialog(R.string.alert, getString(R.string.event_required_login), true);
			}
			break;

		default:
			if (DBG) Log.d(TAG, "bail due to unknown requestCode=" + requestCode);
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("mImageFileUri", mImageFileUri);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		 setContentImage(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void submitEventEntryData() {

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if(MobileEventFacade.ACTION_ENTRY.equals(mAction)) {
					showProgressDialog(R.string.event_entry_doing);
					final PhotoEventResult result = mAPI.joinPhotoEvent(getPhotoEventSource());
					dismissProgressDialog();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							handlePhotoEventResult(result);
						}
					});

				} else if (MobileEventFacade.ACTION_ENTRY_MODIFY.equals(mAction)) {
					showProgressDialog(R.string.event_entry_modifying);
					final PhotoEventResult result = mAPI.updatePhotoEvent(getPhotoEventSource());
					dismissProgressDialog();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							handlePhotoEventResult(result);
						}
					});
				}

			}
		});

		hideInputMethod();
		t.start();
	}

	private void handlePhotoEventResult(PhotoEventResult result) {
		String strResult = result.getResult();
		if(RESULT_SUCCESS.equals(strResult)) {
			showAlertDialog(R.string.alert, result.getMessage(), true);
		} else if("no auth".equals(strResult)) {
			showLoginAlertDialog(R.string.alert, result.getMessage());
		} else {
			showAlertDialog(R.string.alert, result.getMessage(), false);
		}
	}

	private PhotoEventSource getPhotoEventSource() {
		mSource.setTitle(mEtTitle.getText().toString());
		mSource.setContent(mEtContent.getText().toString());
		mSource.setContentImageUri(mImageFileUri);
		return mSource;
	}

	@Override
	protected void onDestroy() {
		if(DBG) Log.d(TAG, "onDestroy........");
		super.onDestroy();
	}

	private void showAddImageAttachmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo_small);
        builder.setTitle(R.string.add_image_attachment);

        if (mAttachmentImageSelectorAdapter == null) {
            mAttachmentImageSelectorAdapter = new AttachmentImageSelectorAdapter(this);
        }
        builder.setAdapter(mAttachmentImageSelectorAdapter, new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {
                addAttachment(mAttachmentImageSelectorAdapter.buttonToCommand(which));
                dialog.dismiss();
            }
        });

        builder.show();
    }

	private void showAlertDialog(int title, String message, final boolean isAfterFinish) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.logo_small);
        builder.setTitle(title);
        builder.setMessage(message);
    	builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(isAfterFinish) {
					finish();
				}
			}
		});

        builder.show();
	}

	private void showLoginAlertDialog(int title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.logo_small);
        builder.setTitle(title);
        builder.setMessage(message);
    	builder.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(PhotoEventActivity.this, LoginSubActivity.class);
				startActivityForResult(intent, REQUEST_CODE_LOGIN);
			}
		});
    	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

        builder.show();
	}

	private void showProgressDialog(final int messageId) {

		if(mProgressDialog != null) {
			return;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
					mProgressDialog = ProgressDialog.show(PhotoEventActivity.this, "",
							getString(messageId), true);
			}
		});
	}

	private void dismissProgressDialog() {
		if(mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	private void addAttachment(int type) {

        switch (type) {
            case AttachmentImageSelectorAdapter.ADD_IMAGE :
            	addImage();
                break;

            case AttachmentImageSelectorAdapter.TAKE_PICTURE: {
                addTakeCameraPicture();
                break;
            }

            default:
                break;
        }
    }

	private void addTakeCameraPicture() {
		mImageFileUri = TempFileCreator.getOutputMediaFileUri(TempFileCreator.MEDIA_TYPE_IMAGE);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageFileUri);
		startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		if(DBG) Log.d(TAG, "create image capture tmep file uri : " + mImageFileUri);
	}

	private void addImage() {
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.setType("image/jpeg");
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, REQUEST_CODE_ATTACH_IMAGE);
	}

	public void backButtonPressed(View view) {
		finish();
	}

	private void hideInputMethod() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEtContent.getWindowToken(), 0);
	}
	
	private Bitmap getBitmapReSize( String fileName ){
	    try {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(fileName, options);
	        
	        int MAX_SIZE = 2000 * 1000;
			int size = options.outWidth * options.outHeight;
			int scale = 1;
			
			if(size > MAX_SIZE) {
				scale = (int)Math.pow(2, (int)Math.round(Math.log(size/MAX_SIZE))); 
			}
			options.inJustDecodeBounds = false;
			options.inSampleSize = scale;
	        
	        
	        return BitmapFactory.decodeFile(fileName, options);
	    } catch(Exception e) {
	    	return null;
	    }
	 }
	
}
