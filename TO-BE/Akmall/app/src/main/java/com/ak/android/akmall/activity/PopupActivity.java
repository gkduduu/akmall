package com.ak.android.akmall.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.SharedPreferencesManager;
import com.ak.android.akmall.utils.json.result.MainPopupResult;
import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_popup)
public class PopupActivity extends Activity {
    @ViewById
    ImageView POPUP_MAINIV;
    @ViewById
    WebView POPUP_MAINWB;
    @ViewById
    LinearLayout POPUP_AGAIN_LAYOUT;
    @ViewById
    ImageView POPUP_AGAIN_CHECK;
    @ViewById
    LinearLayout POPUP_CLOSE;

    MainPopupResult data;

    boolean isChecked = false;

    @Click(R.id.POPUP_MAINIV)
    void clickBanner() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url",data.file.get(0).content_url));
        finish();
    }

    @Click(R.id.POPUP_AGAIN_LAYOUT)
    void clickAgain() {
        Calendar ca = Calendar.getInstance();
        String today =  ca.get(Calendar.YEAR)+"" + ca.get(Calendar.MONTH)+ "" + ca.get(Calendar.DATE);
        String id = "mainpopup" + data.mobile_popup_id;
        JHYLogger.D(today);
        if(isChecked) {
            SharedPreferencesManager.setString(this,id,"");
            POPUP_AGAIN_CHECK.setImageResource(R.drawable.img_check);
        }else {
            SharedPreferencesManager.setString(this,id,today);
            POPUP_AGAIN_CHECK.setImageResource(R.drawable.img_checked);
        }
        isChecked = !isChecked;
    }

    @Click(R.id.POPUP_CLOSE)
    void ClickClose() {
        finish();
    }

    @AfterViews
    void afterView() {
        POPUP_MAINIV.setVisibility(View.VISIBLE);
        data = (MainPopupResult) getIntent().getSerializableExtra("data");
        Glide.with(this).load(BaseUtils.nvl(data.file.get(0).banner_url))
                .centerCrop()
                .into(POPUP_MAINIV);
    }
}
