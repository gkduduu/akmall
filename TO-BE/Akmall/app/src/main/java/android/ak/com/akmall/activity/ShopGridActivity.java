package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.adapter.BestAdapter;
import android.ak.com.akmall.adapter.GridAdapter;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_shop_grid)
public class ShopGridActivity extends Activity {

    @ViewById
    RecyclerView GRID_RV_LIST;

    GridAdapter adapter;

    @AfterViews
    void afterView() {
        GRID_RV_LIST.setHasFixedSize(true);
        GRID_RV_LIST.setLayoutManager(new GridLayoutManager(this,2));
        GRID_RV_LIST.setNestedScrollingEnabled(false);

        ArrayList<String> a = new ArrayList<>();
        a.add("aa");
        a.add("2aa");
        a.add("34aa");
        a.add("a5a");
        a.add("a6a");

        adapter = new GridAdapter(this,a);
        GRID_RV_LIST.setAdapter(adapter);
    }
}
