
package com.ak.android.akplaza.receipt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.ak.android.akplaza.BuildConfig;
import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.ActivityTaskManager;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.HeaderClient;
import com.ak.android.akplaza.common.LoginManager;
import com.ak.android.akplaza.http.AkHttpClient;
import com.ak.android.akplaza.login.LoginSubActivity;

public class ReceiptSubActivity extends Activity implements OnClickListener {

    public static final String TAG = "ReceiptSubActivity";

    private static final int REQ_LOGIN = 2;
    /**
     * 영수증 등록 결과 0 성공, 1 영수증 번호 틀림, 2 이미 등록됨, 3 영수 금액 틀림<br>
     * 0 이외 모두 실패이므로 구분없이 서버에서 내려주는 메시지 출력함
     */
    private static final String RECIPT_REG_SUCCESS = "0";

    private InputMethodManager imm;
    private EditText mReceipt_nm = null; // 영수증 바코드 번호
    private EditText mTotal_price = null; // 총 결재 금액
    private Button mReceipt_Btn = null;
    private ReceiptRegisterTask mReceiptRegisterTask;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.receipt);
        ActivityTaskManager.getInstance().addActivity(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskManager.getInstance().deleteActivity(this);
        // AkBroadcastReceiver.getUnRegisterReiver(this);
    }

    public void init() {
        findView_make();
        getExtraData();
        
        WebView appheader = (WebView) findViewById(R.id.appheader);
        appheader.getSettings().setJavaScriptEnabled(true);
        HeaderClient headerClient = new HeaderClient();
        headerClient.setmContext(ReceiptSubActivity.this);
        appheader.setWebViewClient(headerClient);
        String url = Const.URL_LIB + this.getString(R.string.act_header);
        appheader.loadUrl(url);
    }

    private void getExtraData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("DATA");
        if (data != null) {
            Log.d("DATA", data);
            mReceipt_nm.setText(data);
            mTotal_price.setFocusable(true);
        }
    }

    public void findView_make() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mReceipt_Btn = (Button) findViewById(R.id.receipt_btn);
        mReceipt_Btn.setOnClickListener(this);

        mReceipt_nm = (EditText) findViewById(R.id.receipt_nm);
        mTotal_price = (EditText) findViewById(R.id.total_price);
        // 키패드 숫자 나오게하기기
        mReceipt_nm.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTotal_price.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void backButtonPressed(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {

        regRecepit();
    }

    private void regRecepit() {
        if (!LoginManager.isLogin(this)) {
            Intent intent = new Intent(this, LoginSubActivity.class);
            startActivityForResult(intent, REQ_LOGIN);
            return;
        }

        if (mReceiptRegisterTask == null && isValidReceiptValue()) {
            mReceiptRegisterTask = new ReceiptRegisterTask();
            mReceiptRegisterTask.execute();
        }
    }

    private boolean isValidReceiptValue() {
        boolean isValid = true;
        if (TextUtils.isEmpty(mReceipt_nm.getText())) {
            showAlertDialog("영수증 번호를 입력해 주세요.", false);
            isValid = false;
        } else if (TextUtils.isEmpty(mTotal_price.getText()) || "0".equals(mTotal_price.getText())) {
            showAlertDialog("금액을 입력해 주세요.", false);
            isValid = false;
        }
        return isValid;
    }

    private void showAlertDialog(String message, boolean isFinish) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setIcon(R.drawable.logo_small).create();
        ab.setTitle(" ").create();
        ab.setMessage(message).create();
        if (isFinish) {
            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    imm.hideSoftInputFromWindow(mReceipt_nm.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mTotal_price.getWindowToken(), 0);
                    finish();
                }
            }).create();
        } else {
            ab.setPositiveButton("확인", null).create();
        }
        ab.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQ_LOGIN:
                if (resultCode == RESULT_OK) {
                    regRecepit();
                } else {
                    showAlertDialog("영수증을 등록 하려면 로그인을 해야합니다.", false);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ReceiptRegisterTask extends AsyncTask<Void, Void, Boolean> {

        ProgressDialog mProgressDialog;
        String mResultCode;
        String mResultMessage;

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Const.URL_LIB;

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("act", "appleReceiptRegProc"));
            parameters.add(new BasicNameValuePair("recptNo", mReceipt_nm.getText().toString()));
            parameters.add(new BasicNameValuePair("recptPay", mTotal_price.getText().toString()));

            AkHttpClient client = new AkHttpClient(ReceiptSubActivity.this);

            try {
                HttpPost request = new HttpPost(url);
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
                request.setEntity(entity);

                String result = client.execute(request);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "recipt reg result: " + result);
                }

                JSONObject json = new JSONObject(result);
                mResultCode = json.getString("resultCode").toString();
                mResultMessage = json.getString("result").toString();

                return RECIPT_REG_SUCCESS.equals(mResultCode);
            } catch (Exception e) {
                Log.e(TAG, "failed receipt register..");
                mResultCode = "-1";
                mResultMessage = "시스템 장애로 영수증을 등록 할 수 없는 상태입니다.";
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(ReceiptSubActivity.this);
            mProgressDialog.setMessage(getString(R.string.receipt_registration_doing));
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            showAlertDialog(mResultMessage, result);
            mReceiptRegisterTask = null;
        }

    }

}
