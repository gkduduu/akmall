package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_shop_power_deal)
public class ShopPowerDealActivity extends Activity {

    @ViewById
    RecyclerView POWERDEAL_RV_LIST;
    private RecyclerView.LayoutManager mLayoutManager;

    @AfterViews
    void afterView() {
        POWERDEAL_RV_LIST.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        POWERDEAL_RV_LIST.setLayoutManager(mLayoutManager);
        POWERDEAL_RV_LIST.setNestedScrollingEnabled(false);
    }
}
