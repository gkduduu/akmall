package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.blurbehind.BlurBehind;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_more)
public class MoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BlurBehind.getInstance()
                .setBackground(this);
    }

    @Click(R.id.MORE_PICK)
    void clickPick() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_PICK));
        finish();
    }
    @Click(R.id.MORE_VIP)
    void clickVip() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_VIP));
        finish();
    }
    @Click(R.id.MORE_COUPON)
    void clickCoupon() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_COUPON));
        finish();
    }
    @Click(R.id.MORE_LIKE)
    void clickLike() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_LIKE));
        finish();
    }
    @Click(R.id.MORE_HISTORY)
    void clickHistory() {
//        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_SHOPPINGALIM));
        setResult(Const.MORE_RESULT);
        finish();
    }
    @Click(R.id.MORE_SEARCH)
    void clickSearch() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_SEARCH));
        finish();
    }
    @Click(R.id.MORE_CATEGORY)
    void clickCategory() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_SHOPPINGALIM));
        finish();
    }
    @Click(R.id.MORE_BAG)
    void clickBag() {
        startActivity(new Intent(this,MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
        finish();
    }

    @Click(R.id.MORE_BACKGROUND)
    void closer() {
        finish();
    }
    @Click(R.id.MORE_HOME)
    void clickHide() {
        finish();
    }
    @Click(R.id.MORE_CLOSE)
    void clickClose() {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
