package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.adapter.BestAdapter;
import android.ak.com.akmall.adapter.MainCategoryAdapter;
import android.ak.com.akmall.fragment.WebviewFragment;
import android.ak.com.akmall.fragment.WebviewFragment_;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    @ViewById
    RecyclerView MAIN_TOP_CATEGORY;
    MainCategoryAdapter adapter;

    @ViewById
    DrawerLayout ACTIVITY_MAIN;
    @ViewById
    LinearLayout MAIN_SLIDEMENU;

    @ViewById
    ViewPager MAIN_PAGER;

    SectionsPagerAdapter mSectionsPagerAdapter;

    ArrayList<String> a = new ArrayList<>();

    @Click(R.id.test)
    void vlic() {
//        ACTIVITY_MAIN.openDrawer(MAIN_SLIDEMENU);
        startActivity(new Intent(this,MoreActivity.class));
    }

    @AfterViews
    void afterView() {
        //슬라이드 메뉴
        ACTIVITY_MAIN.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ACTIVITY_MAIN.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ACTIVITY_MAIN.setFocusableInTouchMode(false);


        //하단 리스트뷰
        MAIN_TOP_CATEGORY.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        MAIN_TOP_CATEGORY.setLayoutManager(layoutManager);
        MAIN_TOP_CATEGORY.setNestedScrollingEnabled(false);

        a.add("aa");
        a.add("2aa");
        a.add("2aa");
        a.add("2aa");
        a.add("a5a");
        a.add("a6a");
        adapter = new MainCategoryAdapter(this, a, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = MAIN_TOP_CATEGORY.getChildLayoutPosition(v);
                adapter.clickThis(pos);
                MAIN_PAGER.setCurrentItem(pos);
                adapter.notifyDataSetChanged();
            }
        });
        MAIN_TOP_CATEGORY.setAdapter(adapter);

        //뷰페이저 세팅
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());
        MAIN_PAGER.setOffscreenPageLimit(3);
        MAIN_PAGER.setAdapter(mSectionsPagerAdapter);
        MAIN_PAGER.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                adapter.clickThis(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //뷰페이저 어답타
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //각 화면은 Fragment로 구성된다.
            Fragment fragment = null;
                fragment = new WebviewFragment_();
            return fragment;
        }
        //전체페이지수
        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (ACTIVITY_MAIN.isDrawerOpen(MAIN_SLIDEMENU)) {
                ACTIVITY_MAIN.closeDrawer(MAIN_SLIDEMENU);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
