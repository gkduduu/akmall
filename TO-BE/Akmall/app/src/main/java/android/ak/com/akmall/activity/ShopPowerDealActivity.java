package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.adapter.PowerDealAdapter;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_shop_power_deal)
public class ShopPowerDealActivity extends Activity {

    @ViewById
    RecyclerView POWERDEAL_RV_LIST;
    private RecyclerView.LayoutManager mLayoutManager;

    private PowerDealAdapter adapter;

    @AfterViews
    void afterView() {
        POWERDEAL_RV_LIST.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        POWERDEAL_RV_LIST.setLayoutManager(mLayoutManager);
        POWERDEAL_RV_LIST.setNestedScrollingEnabled(false);

        ArrayList<String> a = new ArrayList<>();
        a.add("aa");
        a.add("2aa");
        a.add("34aa");
        a.add("a5a");
        a.add("a6a");

        adapter = new PowerDealAdapter(this,a);
        POWERDEAL_RV_LIST.setAdapter(adapter);
    }
}
