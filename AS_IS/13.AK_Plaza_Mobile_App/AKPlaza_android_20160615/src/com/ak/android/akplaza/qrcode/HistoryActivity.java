
package com.ak.android.akplaza.qrcode;

import com.ak.android.akplaza.R;
import com.ak.android.akplaza.common.ActivityTaskManager;
import com.ak.android.akplaza.common.AkPlazaFacade;
import com.ak.android.akplaza.common.Const;
import com.ak.android.akplaza.common.HeaderClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends Activity implements OnClickListener, OnItemClickListener {

    private static final String TAG = HistoryActivity.class.getSimpleName();

    private ListView mHistoryListView = null;
    List<Result> items = null;
    List<String> dialogItems = null;

    private static final int MAX_ITEMDAY = 90;


    private static final String[] ID_COL_PROJECTION = {
            DBHelper.ID_COL, DBHelper.TIMESTAMP_COL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akbarcodehistroty);
        ActivityTaskManager.getInstance().addActivity(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityTaskManager.getInstance().deleteActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        findViews();
        trimHistory();
        getData();
        setListViewData();
        
        WebView appheader = (WebView) findViewById(R.id.appheader);
        appheader.getSettings().setJavaScriptEnabled(true);
        HeaderClient headerClient = new HeaderClient();
        headerClient.setmContext(HistoryActivity.this);
        appheader.setWebViewClient(headerClient);
        String url = Const.URL_LIB + this.getString(R.string.act_header);
        appheader.loadUrl(url);
    }

    private void findViews() {
        mHistoryListView = (ListView) findViewById(R.id.barcode_history_list);
        mHistoryListView.setOnItemClickListener(this);
//        findViewById(R.id.btn_top_back).setOnClickListener(this);
    }

    private void getData() {
        SQLiteOpenHelper helper = new DBHelper(this);
        items = new ArrayList<Result>();
        dialogItems = new ArrayList<String>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
//		      cursor = db.query(DBHelper.TABLE_NAME, GET_ITEM_COL_PROJECTION, null, null, null, null,
//		          DBHelper.TIMESTAMP_COL + " DESC");

            String sql = "SELECT text, display, format, timestamp from history where (format=='QR_CODE')";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {

                Result result = new Result(cursor.getString(0), null, null,
                        BarcodeFormat.valueOf(cursor.getString(2)), cursor.getLong(3));
                items.add(result);
                String display = cursor.getString(1);
                if (display == null || display.length() == 0) {
                    display = result.getText();
                }
                dialogItems.add(display);
                Date date = new Date(result.getTimestamp());
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                String strDate = df.format(date);
                Log.d("HISTORY", result.getText() + " , " + strDate + " , "
                        + result.getBarcodeFormat().toString());
            }
        } catch (SQLiteException sqle) {
            Log.w(TAG, "Error while opening database", sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        Resources res = this.getResources();
        dialogItems.add(res.getString(R.string.history_send));
        dialogItems.add(res.getString(R.string.history_clear_text));
    }

    private void setListViewData() {
        if (items != null) {
            HistoryAdapter hAdapter = new HistoryAdapter(this, items);
            mHistoryListView.setAdapter(hAdapter);
        }
    }

    public void trimHistory() {
        SQLiteOpenHelper helper = new DBHelper(this);
        SQLiteDatabase db;
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException sqle) {
            Log.w(TAG, "Error while opening database", sqle);
            return;
        }

        Cursor cursor = null;
        try {
            cursor = db.query(DBHelper.TABLE_NAME,
                    ID_COL_PROJECTION,
                    null, null, null, null,
                    DBHelper.TIMESTAMP_COL + " DESC");

            while (cursor.moveToNext()) {
                long ct = System.currentTimeMillis();
                long dt = cursor.getLong(1);
                Log.d("HISTORY",
                        String.valueOf(ct) + " , " + String.valueOf(dt) + " , "
                                + String.valueOf((ct - dt) / (60 * 60 * 1000)));
                if ((ct - dt) / (60 * 60 * 1000) > MAX_ITEMDAY) {
                    db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COL + '=' + cursor.getString(0),
                            null);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.btn_top_back) {
//            finish();
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Result result = items.get(position);
        String url = result.getText();
        String[] akUrls = getResources().getStringArray(R.array.akurl_array);

        boolean isAkUrl = false;
        for (String akUrl : akUrls) {
            if (url.startsWith(akUrl)) {
                isAkUrl = true;
                break;
            }
        }

        if (isAkUrl) {
            AkPlazaFacade.startMainActivity(this, Uri.parse(url));
        } else {
            AkPlazaFacade.startExternalActivity(this, url);

        }

        finish();
    }
}
