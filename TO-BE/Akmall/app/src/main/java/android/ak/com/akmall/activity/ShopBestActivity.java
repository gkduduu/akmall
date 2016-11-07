package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.adapter.BestAdapter;
import android.ak.com.akmall.adapter.PowerDealAdapter;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_shop_best)
public class ShopBestActivity extends Activity {

    @ViewById
    RecyclerView BEST_RV_LIST;
    private RecyclerView.LayoutManager mLayoutManager;

    private BestAdapter adapter;

    @AfterViews
    void afterView() {
        BEST_RV_LIST.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        BEST_RV_LIST.setLayoutManager(mLayoutManager);
        BEST_RV_LIST.setNestedScrollingEnabled(false);

        ArrayList<String> a = new ArrayList<>();
        a.add("aa");
        a.add("2aa");
        a.add("34aa");
        a.add("a5a");
        a.add("a6a");

        adapter = new BestAdapter(this,a);
        BEST_RV_LIST.setAdapter(adapter);
    }
}