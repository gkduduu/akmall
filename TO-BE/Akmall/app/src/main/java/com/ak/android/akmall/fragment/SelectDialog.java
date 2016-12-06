package com.ak.android.akmall.fragment;

import com.ak.android.akmall.R;
import com.ak.android.akmall.adapter.SelectAdapter;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.CheckHeightResult;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? : 셀렉트 다이얼로그
 */

public class SelectDialog extends Dialog {

    RelativeLayout DIALOG_SHARE;
    Context context;
    RecyclerView DIALOG_RECYCLE;
    String json;
    String call ="";
    String idx ="";
    List<String> list = new ArrayList<>();
    List<String> value = new ArrayList<>();

    public SelectDialog(Context context, String json) {
        super(context);
        this.context = context;
        //{"idx":0,
        // "menu":[{"신상품순":"regDate"},{"판매순":"popular"},{"낮은가격순":"lowPrice"},{"높은가격순":"highPrice"}],
        // call":"sortChange"}
        this.json = json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_select);

        DIALOG_SHARE = (RelativeLayout) findViewById(R.id.DIALOG_SHARE);
        DIALOG_RECYCLE = (RecyclerView)findViewById(R.id.DIALOG_RECYCLE);

        try {
            JSONObject js = new JSONObject(json);
            call = js.getString("call");
            idx = js.getString("idx");
            JSONArray array = js.getJSONArray("menu");
            for(int i=0;i<array.length();i++) {
                Iterator iter = array.getJSONObject(i).keys();
                String key = iter.next().toString();
                list.add(key);
                value.add(array.getJSONObject(i).getString(key));
            }

        }catch(Exception e) {
            e.getMessage();
        }


//        for (Iterator iterator = key.iterator(); iterator.hasNext();) {
//            String keyName = (String) iterator.next();
//            list.add(keyName);
//            String valueName = (String) rs.get(keyName);
//        }

        DIALOG_SHARE.getLayoutParams().height = (int) BaseUtils.convertDpToPixel(300, context);
        DIALOG_SHARE.requestLayout();
//
        DIALOG_RECYCLE.setHasFixedSize(true);
        DIALOG_RECYCLE.setLayoutManager(new LinearLayoutManager(context));
        DIALOG_RECYCLE.setNestedScrollingEnabled(false);
        SelectAdapter adapter = new SelectAdapter(context, list,this,call,value,Integer.parseInt(idx));
        DIALOG_RECYCLE.setAdapter(adapter);
    }

}
