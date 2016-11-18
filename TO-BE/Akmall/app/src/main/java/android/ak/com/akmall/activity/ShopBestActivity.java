package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.adapter.BestAdapter;
import android.ak.com.akmall.utils.http.DataControlHttpExecutor;
import android.ak.com.akmall.utils.http.DataControlManager;
import android.ak.com.akmall.utils.http.RequestCompletionListener;
import android.ak.com.akmall.utils.http.RequestFailureListener;
import android.ak.com.akmall.utils.json.Parser;
import android.ak.com.akmall.utils.json.result.BestResult;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_shop_best)
public class ShopBestActivity extends Activity {

    @ViewById
    RecyclerView BEST_RV_LIST;
    private RecyclerView.LayoutManager mLayoutManager;

    private BestAdapter adapter;

    @AfterViews
    void afterView() {

        //서버연동 테스트
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestGoodsBest(this,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                initView(Parser.parsingBestProduct(responseData.toString()));
                            }
                        },
                        new RequestFailureListener() {
                            @Override
                            public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {

                            }
                        }
                ));
        DataControlManager.getInstance().runScheduledCommandOnAsync();

    }

    private void initView(BestResult result) {
        BEST_RV_LIST.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        BEST_RV_LIST.setLayoutManager(mLayoutManager);
        BEST_RV_LIST.setNestedScrollingEnabled(false);

        adapter = new BestAdapter(this,result.pageDatas);
        BEST_RV_LIST.setAdapter(adapter);
    }
}
