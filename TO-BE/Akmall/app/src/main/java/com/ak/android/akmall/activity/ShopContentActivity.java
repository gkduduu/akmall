package com.ak.android.akmall.activity;

import com.ak.android.akmall.R;
import com.ak.android.akmall.adapter.BestAdapter;
import com.ak.android.akmall.adapter.BigCategoryAdapter;
import com.ak.android.akmall.adapter.GallaryAdapter;
import com.ak.android.akmall.adapter.GridAdapter;
import com.ak.android.akmall.fragment.SelectDialog;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.DataHolder;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.FullDrawerLayout;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.http.DataControlHttpExecutor;
import com.ak.android.akmall.utils.http.DataControlManager;
import com.ak.android.akmall.utils.http.RequestCompletionListener;
import com.ak.android.akmall.utils.http.RequestFailureListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.AddingResult;
import com.ak.android.akmall.utils.json.result.BigCategoryResult;
import com.ak.android.akmall.utils.json.result.ChangeParamResult;
import com.ak.android.akmall.utils.json.result.CheckHeightResult;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;
import com.ak.android.akmall.utils.json.result.PageDatas;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? : 상품 보여주는 액티비티(카테고리)
 */

@EActivity(R.layout.activity_shop_content)
public class ShopContentActivity extends Activity {

    //대카테고리인지 중소세인지 브랜드샵인지(상단카테고리없음)
    static String WHAT_PAGE;

    @ViewById
    RecyclerView CONTENT_RV_LIST;
    private RecyclerView.LayoutManager mLayoutManager;
    GridAdapter gridAdapter;
    BestAdapter bestAdapter;
    GallaryAdapter gallaryAdapter;

    @ViewById
    WebView CONTENT_WV_WEBVIEW;

    @ViewById
    ScrollView CONTENT_SV_SCROLL;

    @ViewById
    LinearLayout CONTENT_SLIDEMENU;
    @ViewById
    WebView CONTENT_SLIDE_WEBVIEW;

    @ViewById
    DrawerLayout ACTIVITY_CONTENT;

    @ViewById
    RecyclerView CONTENT_RV_CATEGORY;
    BigCategoryAdapter categoryAdapter;

    BigCategoryResult mBigCatgoryResult;

    float listHeight = 0;

    boolean isAdding = false;/*상품 추가하기위한 서버연동 진행중인지 여부*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Feature.addAcitivty(this);
    }

    @Click(R.id.MENU_CATEGORY)
    void ClickMenuCate() {
        startActivityForResult(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_CATEGORY), Const.CATEGORY_BIG_REQUEST);
    }

    @Click(R.id.MENU_SEARCH)
    void ClickMenuSearch() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_SEARCH));
    }

    @Click(R.id.MENU_HOME)
    void ClickMenuHome() {
        Feature.closeAllActivity();
    }

    @Click(R.id.MENU_MYAK)
    void ClickMenuCMyak() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_MYAK));
    }

    @Click(R.id.MENU_BAG)
    void ClickMenuBag() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
    }

    private String settingWhatPage(String url) {
        if (url.contains("display/ShopFront.do")) {
            //위의 경우 대카테고리
            return Const.CONTENT_BIG_CATEGORY;
        } else if (url.contains("/akplaza/DeptStore.do")) {
            return Const.CONTENT_BRAND_SHOP;
        } else if (url.contains("/display/CtgMClsf.do")) {
            return Const.CONTENT_MID_CATEGORY;
        } else if (url.contains("/display/CtgSClsf.do")) {
            return Const.CONTENT_SMALL_CATEGORY;
        } else {
            return Const.CONTENT_MID_CATEGORY;
        }
    }


    @AfterViews
    void afterView() {
        String goUrl = getIntent().getStringExtra("url");
        //받은 url에 따라 현재 페이지를 전역으로 저장해놓음ㅎ
        WHAT_PAGE = settingWhatPage(goUrl);

        if (goUrl.contains(URLManager.getServerUrl())) {
            goUrl = goUrl;
        } else {
            goUrl = URLManager.getServerUrl() + goUrl;
        }
        //웹뷰에 각종 옵션세팅
//        CONTENT_WV_WEBVIEW.setInitialScale(100);
        CONTENT_WV_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        CONTENT_WV_WEBVIEW.getSettings().setUseWideViewPort(true);
        CONTENT_WV_WEBVIEW.setWebContentsDebuggingEnabled(true);
        CONTENT_WV_WEBVIEW.loadUrl(goUrl + "&isAkApp=Y");
        CONTENT_WV_WEBVIEW.setWebChromeClient(new ChromeClient());
        CONTENT_WV_WEBVIEW.setWebViewClient(new WebViewClientClass());

        //필터 웨뷰에 각종 옵션세팅
        CONTENT_SLIDE_WEBVIEW.setInitialScale(100);
        CONTENT_SLIDE_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        CONTENT_SLIDE_WEBVIEW.getSettings().setUseWideViewPort(true);
        CONTENT_SLIDE_WEBVIEW.setWebContentsDebuggingEnabled(true);
        CONTENT_SLIDE_WEBVIEW.loadUrl(URLManager.getServerUrl() + Const.MOVE_FILTER);
        CONTENT_SLIDE_WEBVIEW.setWebChromeClient(new ChromeClient());
        CONTENT_SLIDE_WEBVIEW.setWebViewClient(new FilterWebViewClient());

        //슬라이드 메뉴
        ACTIVITY_CONTENT.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ACTIVITY_CONTENT.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ACTIVITY_CONTENT.setFocusableInTouchMode(false);

        //스크롤 리스너
        CONTENT_SV_SCROLL.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = CONTENT_SV_SCROLL.getScrollY(); // For ScrollView
                //상단 카테고리 리스트
                if (WHAT_PAGE.equals(Const.CONTENT_BRAND_SHOP)) {
                    //브랜드샵일때는 안보여줌
                    CONTENT_RV_CATEGORY.setVisibility(View.GONE);
                } else {
                    float scorllDp = BaseUtils.convertPixelsToDp(scrollY, ShopContentActivity.this);
                    if (scorllDp > 45) {
                        CONTENT_RV_CATEGORY.setVisibility(View.VISIBLE);
                        CONTENT_RV_CATEGORY.bringToFront();
                    } else {
                        CONTENT_RV_CATEGORY.setVisibility(View.GONE);
                    }
                }
                if (isAdding) return;
                View view = (View) CONTENT_SV_SCROLL.getChildAt(CONTENT_SV_SCROLL.getChildCount() - 1);
                int diff = (view.getBottom() - (CONTENT_SV_SCROLL.getHeight() + CONTENT_SV_SCROLL.getScrollY()));
                if (diff == 0) {
                    //스크롤뷰 하단 도착, 페이지 목록 추가
                    if(null == mBigCatgoryResult)return;
                    if (mBigCatgoryResult.dp.equals("1")) {

                    } else if (mBigCatgoryResult.dp.equals("2")) {
                        //그리드 형태
                        if (WHAT_PAGE.equals(Const.CONTENT_BIG_CATEGORY)) {
                            //best , new , recom
                            if (currentWhat.equals("best")) {
                                if (Integer.parseInt(BaseUtils.nvl(mBigCatgoryResult.prodList.bestProdList.totalcnt, "0")) > gridAdapter.getItemCount()) {
                                    //목록 추가
                                    isAdding = true;
                                    String idx = mBigCatgoryResult.prodList.bestProdList.pageIdx;
                                    DataControlManager.getInstance().addSchedule(
                                            new DataControlHttpExecutor().requestAddingContent(ShopContentActivity.this, "/display/selectGoodsByBestAjax.do?"
                                                    , (Integer.parseInt(idx) + 1) + "", mBigCatgoryResult.param,
                                                    new RequestCompletionListener() {
                                                        @Override
                                                        public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                                            JHYLogger.d(responseData.toString());
                                                            AddingResult adResult = Parser.parsingAddingContent(responseData.toString());
                                                            addingList(adResult);
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
                            } else if(currentWhat.equals("new")) {
                                if (Integer.parseInt(BaseUtils.nvl(mBigCatgoryResult.prodList.newProdList.totalcnt, "0")) > gridAdapter.getItemCount()) {
                                    //목록 추가
                                    isAdding = true;
                                    String idx = mBigCatgoryResult.prodList.newProdList.pageIdx;
                                    DataControlManager.getInstance().addSchedule(
                                            new DataControlHttpExecutor().requestAddingContent(ShopContentActivity.this, "/display/selectGoodsByBestAjax.do?"
                                                    , (Integer.parseInt(idx) + 1) + "", mBigCatgoryResult.param,
                                                    new RequestCompletionListener() {
                                                        @Override
                                                        public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                                            JHYLogger.d(responseData.toString());
                                                            AddingResult adResult = Parser.parsingAddingContent(responseData.toString());
                                                            addingList(adResult);
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
                            }
                        }
                    }

                }
            }
        });
//        //서버연동 테스트
//        DataControlManager.getInstance().addSchedule(
//                new DataControlHttpExecutor().requestGoodsBest(this,
//                        new RequestCompletionListener() {
//                            @Override
//                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
//                                initView(Parser.parsingBestProduct(responseData.toString()));
//                            }
//                        },
//                        new RequestFailureListener() {
//                            @Override
//                            public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
//
//                            }
//                        }
//                ));
//        DataControlManager.getInstance().runScheduledCommandOnAsync();
    }

    //서버연동 후 뷰세팅
    private void initView(final BigCategoryResult result) {
        mBigCatgoryResult = result;
        //상단 카테고리 리스트
        CONTENT_RV_CATEGORY.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        CONTENT_RV_CATEGORY.setLayoutManager(layoutManager);
        CONTENT_RV_CATEGORY.setNestedScrollingEnabled(false);
        categoryAdapter = new BigCategoryAdapter(this, result.ctgArray, Integer.parseInt(BaseUtils.nvl(mBigCatgoryResult.detailCtgSize, "0")),
                Integer.parseInt(BaseUtils.nvl(mBigCatgoryResult.lastCtgSize, "0")), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = CONTENT_RV_CATEGORY.getChildLayoutPosition(v);
                if (pos == 1) {
                    //대 카테고리 선택
                    String param = "";
                    try {
                        JHYLogger.D(mBigCatgoryResult.ctgInfo);
                        param = mBigCatgoryResult.ctgInfo;
                    } catch (Exception e) {
                        JHYLogger.e(e.getMessage());
                    }
                    //동일한 DEPT의 카테고리를 선택할 경우 새창띄우지말고 리로드함
                    boolean isReload = false;
                    if (WHAT_PAGE.equals(Const.CONTENT_BIG_CATEGORY)) {
                        isReload = true;
                    }
                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class)
                            .putExtra("url", Const.BIG_CATEGORY).putExtra("json", param)
                            .putExtra("cate", "big").putExtra("isReload", isReload), Const.CATEGORY_BIG_REQUEST);
                } else if (pos == 2) {
                    //중 카테고리 선택
                    String param = "";
                    try {
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                    } catch (Exception e) {
                        JHYLogger.e(e.getMessage());
                    }
                    boolean isReload = false;
                    if (WHAT_PAGE.equals(Const.CONTENT_MID_CATEGORY)) {
                        isReload = true;
                    }
                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class)
                            .putExtra("url", Const.BIG_CATEGORY).putExtra("json", param)
                            .putExtra("cate", "mid").putExtra("isReload", isReload), Const.CATEGORY_BIG_REQUEST);
                } else if (pos == 3) {
                    //소 카테고리 선택
                    String param = "";
                    try {
                        JHYLogger.D(param);
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                    } catch (Exception e) {
                        JHYLogger.e(e.getMessage());
                    }
                    boolean isReload = false;
                    if (WHAT_PAGE.equals(Const.CONTENT_SMALL_CATEGORY)) {
                        isReload = true;
                    }
                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class)
                            .putExtra("url", Const.BIG_CATEGORY).putExtra("json", param)
                            .putExtra("cate", "small").putExtra("isReload", isReload), Const.CATEGORY_BIG_REQUEST);
                } else if (pos == 4) {
                    //세 카테고리 선택
                    String param = "";
                    try {
                        JHYLogger.D(param);
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                    } catch (Exception e) {
                        JHYLogger.e(e.getMessage());
                    }
                    boolean isReload = false;
                    if (WHAT_PAGE.equals(Const.CONTENT_SMALL_CATEGORY)) {
                        isReload = true;
                    }
                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class)
                            .putExtra("url", Const.BIG_CATEGORY).putExtra("json", param)
                            .putExtra("cate", "small").putExtra("isReload", isReload), Const.CATEGORY_BIG_REQUEST);
                }
                overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
            }
        });
        CONTENT_RV_CATEGORY.setAdapter(categoryAdapter);

        //TODO:테스트코드 지워야 할수도이씅ㅁ
        if (null == result.dp) return;
        //하단 상품 리스트
        //대 카테고리
        if (WHAT_PAGE.equals(Const.CONTENT_BIG_CATEGORY)) {
            if (result.dp.equals("2")) {
                //그리드 형태
                CONTENT_RV_LIST.setHasFixedSize(true);
                CONTENT_RV_LIST.setLayoutManager(new GridLayoutManager(this, 2));
                CONTENT_RV_LIST.setNestedScrollingEnabled(false);
                if (null == result.prodList.bestProdList.pageDatas || result.prodList.bestProdList.pageDatas.size() == 0) {
                    CONTENT_RV_LIST.setVisibility(View.GONE);
                    return;
                }
                CONTENT_RV_LIST.setVisibility(View.VISIBLE);
                gridAdapter = new GridAdapter(this, result.prodList.bestProdList.pageDatas, goDetailListener);
                CONTENT_RV_LIST.setAdapter(gridAdapter);
                int size = result.prodList.bestProdList.pageDatas.size();
                if (size % 2 == 0) {
                    listHeight = BaseUtils.convertDpToPixel(size / 2 * 158 + (size * 2), this);
                } else {
                    size = size + 1;
                    listHeight = BaseUtils.convertDpToPixel(size / 2 * 158 + (size * 2), this);
                }
                CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight * 2;
                CONTENT_RV_LIST.requestLayout();
            }
        } else {//중소카테고리
            if (result.dp.equals("1")) {
                CONTENT_RV_LIST.setHasFixedSize(true);
                CONTENT_RV_LIST.setLayoutManager(new LinearLayoutManager(this));
                CONTENT_RV_LIST.setNestedScrollingEnabled(false);
                if (null == result.prodList.pageDatas || result.prodList.pageDatas.size() == 0) {
                    CONTENT_RV_LIST.setVisibility(View.GONE);
                    return;
                }
                CONTENT_RV_LIST.setVisibility(View.VISIBLE);
                bestAdapter = new BestAdapter(this, result.prodList.pageDatas, goDetailListener);
                CONTENT_RV_LIST.setAdapter(bestAdapter);
                int size = result.prodList.pageDatas.size();
                listHeight = BaseUtils.convertDpToPixel(size * 158 + (size * 2), this);
                CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight;
                CONTENT_RV_LIST.requestLayout();
            } else if (result.dp.equals("2")) {
                CONTENT_RV_LIST.setHasFixedSize(true);
                CONTENT_RV_LIST.setLayoutManager(new GridLayoutManager(this, 2));
                CONTENT_RV_LIST.setNestedScrollingEnabled(false);
                if (null == result.prodList.pageDatas || result.prodList.pageDatas.size() == 0) {
                    CONTENT_RV_LIST.setVisibility(View.GONE);
                    return;
                }
                CONTENT_RV_LIST.setVisibility(View.VISIBLE);
                gridAdapter = new GridAdapter(this, result.prodList.pageDatas, goDetailListener);
                CONTENT_RV_LIST.setAdapter(gridAdapter);
                int size = result.prodList.pageDatas.size();
                if (size % 2 == 0) {
                    listHeight = BaseUtils.convertDpToPixel(size * 158 / 2 + (size * 2), this);
                } else {
                    size = size + 1;
                    listHeight = BaseUtils.convertDpToPixel(size * 158 / 2 + (size * 2), this);
                }
                CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight;
                CONTENT_RV_LIST.requestLayout();
            } else if (result.dp.equals("3")) {
                CONTENT_RV_LIST.setHasFixedSize(true);
                CONTENT_RV_LIST.setLayoutManager(new LinearLayoutManager(this));
                CONTENT_RV_LIST.setNestedScrollingEnabled(false);
                if (null == result.prodList.pageDatas || result.prodList.pageDatas.size() == 0) {
                    CONTENT_RV_LIST.setVisibility(View.GONE);
                    return;
                }
                CONTENT_RV_LIST.setVisibility(View.VISIBLE);
                gallaryAdapter = new GallaryAdapter(this, result.prodList.pageDatas, goDetailListener);
                CONTENT_RV_LIST.setAdapter(gallaryAdapter);
                int size = result.prodList.pageDatas.size();
                listHeight = BaseUtils.convertDpToPixel(size * 534 + (size * 2), this);
                CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight;
                CONTENT_RV_LIST.requestLayout();
            }
            if (WHAT_PAGE.equals(Const.CONTENT_BRAND_SHOP))
                CONTENT_RV_LIST.setVisibility(View.GONE);
        }
        requestPowerLinkList(result.powerLink);
    }

    //상품 상세보기로 ㄱㄱ
    View.OnClickListener goDetailListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int pos = CONTENT_RV_LIST.getChildLayoutPosition(v);
            ///goods/GoodsDetail.do?goods_id=72125005
            if (WHAT_PAGE.equals(Const.CONTENT_BIG_CATEGORY)) {
                if (currentWhat.equals("best")) {
                    startActivity(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).
                            putExtra("url", "/goods/GoodsDetail.do?goods_id=" +
                                    mBigCatgoryResult.prodList.bestProdList.pageDatas.get(pos).goods_id));
                } else if (currentWhat.equals("new")) {
                    startActivity(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).
                            putExtra("url", "/goods/GoodsDetail.do?goods_id=" +
                                    mBigCatgoryResult.prodList.newProdList.pageDatas.get(pos).goods_id));
                }
            } else {
                startActivity(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).
                        putExtra("url", "/goods/GoodsDetail.do?goods_id=" +
                                mBigCatgoryResult.prodList.pageDatas.get(pos).goods_id));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.CATEGORY_BIG_REQUEST) {
            if (resultCode == Const.CATEGORY_BIG_RESULT) {
                if (null != data) {
                    CONTENT_WV_WEBVIEW.loadUrl(data.getStringExtra("url") + "&isAkApp=Y");
                }
            }
        }
    }

    //현재 선택된 탭 변수
    private String currentWhat = "best";

    //대카테고리의 탭을 전환합니다.
    private void changeTab(String what) {
        //탭 눌렀을때 best , new , recom
        //똑같은거 눌렀으면 안바꿈
        List<PageDatas> data = new ArrayList<>();
        if (!what.equals(currentWhat)) {
            currentWhat = what;
            if (what.equals("best")) {
                if (mBigCatgoryResult.dp.equals("2")) {
                    //그리드 형태
                    data = mBigCatgoryResult.prodList.bestProdList.pageDatas;
                }
            } else if (what.equals("new")) {
                if (mBigCatgoryResult.dp.equals("2")) {
                    //그리드 형태
                    data = mBigCatgoryResult.prodList.newProdList.pageDatas;
                }
            }
            if (null == data || data.size() == 0) {
                CONTENT_RV_LIST.setVisibility(View.GONE);
                return;
            }
            CONTENT_RV_LIST.setVisibility(View.VISIBLE);
            CONTENT_RV_LIST.setHasFixedSize(true);
            CONTENT_RV_LIST.setLayoutManager(new GridLayoutManager(this, 2));
            CONTENT_RV_LIST.setNestedScrollingEnabled(false);
//            gridAdapter.clear();
            gridAdapter = new GridAdapter(this, data, goDetailListener);
            CONTENT_RV_LIST.setAdapter(gridAdapter);
            int size = data.size();
            if (size % 2 == 0) {
                listHeight = BaseUtils.convertDpToPixel((size / 2 * 158) + (size * 2), this);
            } else {
                size = size + 1;
                listHeight = BaseUtils.convertDpToPixel(size / 2 * 158 + (size * 2), this);
            }
            CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight * 2;
            CONTENT_RV_LIST.requestLayout();
        }
    }

    //목록 맨 하단갔을때 추가로 불러올 항목이 있을 경우 서버연동후 일로옴
    private void addingList(AddingResult result) {
        if (WHAT_PAGE.equals(Const.CONTENT_BIG_CATEGORY)) {
            //대카테일땐 그리드만 나옴
            List<PageDatas> data = new ArrayList<>();
            if (currentWhat.equals("best")) {
                mBigCatgoryResult.prodList.bestProdList.pageIdx = result.resultDatas.pageIdx;
                 data = result.resultDatas.pageDatas;

            }else if (currentWhat.equals("new")) {
                mBigCatgoryResult.prodList.newProdList.pageIdx = result.resultDatas.pageIdx;
                data = result.resultDatas.pageDatas;
            }
            CONTENT_RV_LIST.getLayoutParams().height = (int) (listHeight * 2 * Integer.parseInt(BaseUtils.nvl(result.resultDatas.pageIdx, "1")));
            CONTENT_RV_LIST.requestLayout();
            for (int i = 0; i < data.size(); i++) {
                gridAdapter.add(data.get(i));
            }
            gridAdapter.notifyDataSetChanged();
            isAdding = false;
        }
    }

    //대표적으로 브랜드샵에서 정렬 변경했을 경우
    private void sortChangeList(AddingResult result) {
        if (refreshDP.equals("1")) {
            CONTENT_RV_LIST.setHasFixedSize(true);
            CONTENT_RV_LIST.setLayoutManager(new LinearLayoutManager(this));
            CONTENT_RV_LIST.setNestedScrollingEnabled(false);
            if (null == result.resultDatas.pageDatas || result.resultDatas.pageDatas.size() == 0) {
                CONTENT_RV_LIST.setVisibility(View.GONE);
                return;
            }
            CONTENT_RV_LIST.setVisibility(View.VISIBLE);
            bestAdapter = new BestAdapter(this,result.resultDatas.pageDatas, goDetailListener);
            CONTENT_RV_LIST.setAdapter(bestAdapter);
            int size = result.resultDatas.pageDatas.size();
            listHeight = BaseUtils.convertDpToPixel(size * 158 + (size * 2), this);
            CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight;
            CONTENT_RV_LIST.requestLayout();
        } else if (refreshDP.equals("2")) {
            CONTENT_RV_LIST.setHasFixedSize(true);
            CONTENT_RV_LIST.setLayoutManager(new GridLayoutManager(this, 2));
            CONTENT_RV_LIST.setNestedScrollingEnabled(false);
            if (null == result.resultDatas.pageDatas || result.resultDatas.pageDatas.size() == 0) {
                CONTENT_RV_LIST.setVisibility(View.GONE);
                return;
            }
            CONTENT_RV_LIST.setVisibility(View.VISIBLE);
            gridAdapter = new GridAdapter(this, result.resultDatas.pageDatas, goDetailListener);
            CONTENT_RV_LIST.setAdapter(gridAdapter);
            int size = result.resultDatas.pageDatas.size();
            if (size % 2 == 0) {
                listHeight = BaseUtils.convertDpToPixel(size * 158 / 2 + (size * 2), this);
            } else {
                size = size + 1;
                listHeight = BaseUtils.convertDpToPixel(size * 158 / 2 + (size * 2), this);
            }
            CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight;
            CONTENT_RV_LIST.requestLayout();
        } else if (refreshDP.equals("3")) {
            CONTENT_RV_LIST.setHasFixedSize(true);
            CONTENT_RV_LIST.setLayoutManager(new LinearLayoutManager(this));
            CONTENT_RV_LIST.setNestedScrollingEnabled(false);
            if (null == result.resultDatas.pageDatas || result.resultDatas.pageDatas.size() == 0) {
                CONTENT_RV_LIST.setVisibility(View.GONE);
                return;
            }
            CONTENT_RV_LIST.setVisibility(View.VISIBLE);
            gallaryAdapter = new GallaryAdapter(this, result.resultDatas.pageDatas, goDetailListener);
            CONTENT_RV_LIST.setAdapter(gallaryAdapter);
            int size = result.resultDatas.pageDatas.size();
            listHeight = BaseUtils.convertDpToPixel(size * 534 + (size * 2), this);
            CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight;
            CONTENT_RV_LIST.requestLayout();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //카테고리 대 갔다가 옴
//        if (requestCode == Const.CATEGORY_BIG_REQUEST) {
//            if (resultCode == Const.CATEGORY_BIG_RESULT) {
//                String url = data.getStringExtra("url");
//                if (url.contains("http")) {
//                    CONTENT_WV_WEBVIEW.loadUrl(data.getStringExtra("url") + "&isAkApp=Y");
//                } else {
//                    CONTENT_WV_WEBVIEW.loadUrl(URLManager.getServerUrl() + data.getStringExtra("url") + "&isAkApp=Y");
//                }
//                CONTENT_SV_SCROLL.post(new Runnable() {
//                    public void run() {
//                        CONTENT_SV_SCROLL.scrollTo(0, 0);
//                    }
//                });
//            }
//        }
//    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    String refreshParam = "";
    String refreshDP = "";

    //커스텀웹뷰클래스(존나길것같은데 따로 뺴야하나?)
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.D(url);
            if (url.startsWith("akmall://")) {
                //URL DECODE
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                JHYLogger.D(decodeString);

                //스키마에 따른 분기처리!
                if (decodeString.startsWith("akmall://prodList")) {
                    //prodList : 상품 리스트
                    String json = decodeString.replace("akmall://prodList?", "");
                    BigCategoryResult result = Parser.parsingBigCategory(json);
                    JHYLogger.d(result.callbackJson);
                    if (null != result.callbackJson) {
                        String param = "";
                        try {
                            param = URLEncoder.encode(result.callbackJson, "UTF-8");
                            param = Base64.encodeToString(param.getBytes(), 0);
                        } catch (Exception e) {
                            JHYLogger.e(e.getMessage());
                        }
                        view.loadUrl("javascript:nativeCallBack('" + param + "')");
                    }
                    initView(result);
                } else if (decodeString.startsWith("akmall://changeParam")) {
                    ChangeParamResult param = Parser.parsingChangeParam(decodeString.replace("akmall://changeParam?", ""));
                    refreshParam = param.param;
                    refreshDP = param.dp;
                    if (null != param.act) {
                        String act = "";
                        try {
                            act = URLEncoder.encode(param.act, "UTF-8");
                            act = Base64.encodeToString(act.getBytes(), 0);
                        } catch (Exception e) {
                            JHYLogger.e(e.getMessage());
                        }
                        view.loadUrl("javascript:nativeCallBack('" + act + "')");
                    }
                } else if (decodeString.startsWith("akmall://drawList")) {
                    DataControlManager.getInstance().addSchedule(
                            new DataControlHttpExecutor().requestAddingContent(ShopContentActivity.this,
                                    "/akplaza/DeptStoreGoodsListAjax.do?"
                                    , "1", refreshParam,
                                    new RequestCompletionListener() {
                                        @Override
                                        public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                            JHYLogger.d(responseData.toString());
                                            AddingResult adResult = Parser.parsingAddingContent(responseData.toString());
                                            sortChangeList(adResult);
                                        }
                                    },
                                    new RequestFailureListener() {
                                        @Override
                                        public void onDataControlFailed(@Nullable Object responseData, @Nullable Object error) {
                                        }
                                    }
                            ));
                    DataControlManager.getInstance().runScheduledCommandOnAsync();
                } else if (decodeString.startsWith("akmall://checkHeight")) {
                    //checkHeight : 웹뷰 크기 조정
                    String json = decodeString.replace("akmall://checkHeight?", "");
                    CheckHeightResult result = Parser.parsingCheckHeight(json);
                    int height = (int) BaseUtils.convertDpToPixel(Float.parseFloat(result.h), ShopContentActivity.this);
                    CONTENT_WV_WEBVIEW.getLayoutParams().height = height;
                    CONTENT_WV_WEBVIEW.requestLayout();
                } else if (decodeString.startsWith("akmall://changeTab")) {
                    //탭 전환
                    String json = decodeString.replace("akmall://changeTab?", "");
                    String result = Parser.parsingChangeTab(json);
                    int height = (int) BaseUtils.convertDpToPixel(Float.parseFloat(BaseUtils.nvl(result.split(Const.BOUNDARY)[1])), ShopContentActivity.this);
                    CONTENT_WV_WEBVIEW.getLayoutParams().height = height;
                    CONTENT_WV_WEBVIEW.requestLayout();
                    changeTab(BaseUtils.nvl(result.split(Const.BOUNDARY)[0]));
                } else if (decodeString.startsWith("akmall://openWebview")) {
                    //외부 웹 링크 연결
                    String json = decodeString.replace("akmall://openWebview?", "");
                    OpenWebViewResult result = Parser.parsingOpenWebview(json);
                    if (BaseUtils.nvl(result.tp).equals("C")) {
                        //필터 또 클릭일떄
                        ACTIVITY_CONTENT.openDrawer(CONTENT_SLIDEMENU);
                    } else {
                        String link = result.url;
                        if (link.startsWith("http") && !link.contains("akmall.com")) {
                            startActivity(new Intent(ShopContentActivity.this, WebviewActivity_.class).putExtra("url", link));
                        } else {
                            if (link.contains("/display/ShopFront.do") || link.contains("/display/CtgMClsf.do") || link.contains(" /display/CtgSClsf.do")) {
                                view.loadUrl(URLManager.getServerUrl() + link);
                            } else {
                                startActivity(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", link));
                            }
                        }
                    }
                } else if (decodeString.startsWith("akmall://callJavascriptAll")) {
                    //콜자바스크립트
                    String json = decodeString.replace("akmall://callJavascriptAll?", "");
                    String value = Parser.parsingTPString(json);
                    if (value.equals("drawRecomBrand")) {
                        //추천브랜드
                        String param = "";
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "brand"), Const.CATEGORY_BIG_REQUEST);
                        overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
                    } else if (value.equals("drawBigCtg")) {
                        //대 카테고리 선택
                        String param = "";
                        param = mBigCatgoryResult.ctgInfo;
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "big"), Const.CATEGORY_BIG_REQUEST);
                        overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
                    } else if (value.equals("drawMidcate")) {
                        //중 카테고리 선택
                        String param = "";
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "mid"), Const.CATEGORY_BIG_REQUEST);
                        overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
                    } else if (value.equals("drawSamCate")) {
                        //소 카테고리 선택
                        String param = "";
                        JHYLogger.D(param);
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "small"), Const.CATEGORY_BIG_REQUEST);
                        overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
                    } else if (value.equals("drawSam2Cate")) {
                        //세 카테고리 선택
                        String param = "";
                        JHYLogger.D(param);
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "small2"), Const.CATEGORY_BIG_REQUEST);
                        overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
                    }
                } else if (decodeString.startsWith("akmall://goBack")) {
                    view.goBack();
                } else if (decodeString.startsWith("akmall://callSelectPop")) {
                    new SelectDialog(ShopContentActivity.this, decodeString.replace("akmall://callSelectPop?", "")).show();
                } else if (decodeString.startsWith("akmall://callLayerFilter")) {
                    ACTIVITY_CONTENT.openDrawer(CONTENT_SLIDEMENU);
//                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.MOVE_FILTER).putExtra("json", param).putExtra("cate", "filter"), Const.CATEGORY_BIG_REQUEST);
//                    overridePendingTransition(R.anim.anim_messege_in, R.anim.anim_page_out_right);
                    CONTENT_SLIDE_WEBVIEW.loadUrl("javascript:drawFilter('" + decodeString.replace("akmall://callLayerFilter?", "") + "')");
                } else if (decodeString.contains("showNative")) {
                    //scrolltop action일 경우 스크롤 최상단으로 올려줌
                    if (decodeString.contains("scrollTop")) {
                        CONTENT_SV_SCROLL.post(new Runnable() {
                            public void run() {
                                CONTENT_SV_SCROLL.scrollTo(0, 0);
                            }
                        });
                    }
                    CONTENT_RV_LIST.setVisibility(View.VISIBLE);
                } else if (decodeString.contains("hideNative")) {
                    CONTENT_RV_LIST.setVisibility(View.GONE);
                } else if (decodeString.startsWith("akmall://changeFilter")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://changeFilter?", ""));
                    view.loadUrl(URLManager.getServerUrl() + openReult.url + "&isAkApp=Y");
                }
                return true;
            } else if (url.split("\\?")[0].contains("/main/Main.do")) {
                Feature.closeAllActivity();
                return true;
            } else {
                view.loadUrl(url);
                startActivity(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", url));
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    //필터(drawer layout)에 사용되는 웹뷰 클라이언트
    private class FilterWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("akmall://")) {
                //URL DECODE
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                JHYLogger.D(decodeString);

                if (decodeString.startsWith("akmall://changeFilter")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://changeFilter?", ""));
                    CONTENT_WV_WEBVIEW.loadUrl(URLManager.getServerUrl() + openReult.url + "&isAkApp=Y");
                    ACTIVITY_CONTENT.closeDrawer(CONTENT_SLIDEMENU);
                } else if (decodeString.startsWith("akmall://openWebview")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                    CONTENT_WV_WEBVIEW.loadUrl(URLManager.getServerUrl() + openReult.url + "&isAkApp=Y");
                    ACTIVITY_CONTENT.closeDrawer(CONTENT_SLIDEMENU);
                } else if (decodeString.startsWith("akmall://closePopup")) {
                    ACTIVITY_CONTENT.closeDrawer(CONTENT_SLIDEMENU);
                } else if (decodeString.startsWith("akmall://sendFilter")) {
                    //브랜드필터에서 필터변경
                    CONTENT_WV_WEBVIEW.loadUrl("javascript:sendFilter('" + decodeString.replace("akmall://sendFilter?", "") + "')");
                    ACTIVITY_CONTENT.closeDrawer(CONTENT_SLIDEMENU);
                }
            }
            return true;
        }
    }

    //어뎁터에서 호출하는것 상품 장바구니에 담기
    public void callJavascript(String goodsId) {
        try {
            JSONObject json = new JSONObject();
            json.put("goods_id", goodsId);
            JHYLogger.D(json.toString());
            String param = URLEncoder.encode(json.toString(), "UTF-8");
            param = Base64.encodeToString(param.getBytes(), 0);
            CONTENT_WV_WEBVIEW.loadUrl("javascript:checkShoopingCart('" + param + "')");
        } catch (Exception e) {
            JHYLogger.e(e.getMessage());
        }
    }

    //어뎁터에서 호출하는것 중카테에서 정렬 변경
    public void callJavascriptChangeSort(String action) {
        JHYLogger.d(action);
        CONTENT_WV_WEBVIEW.loadUrl("javascript:" + action);
    }

    //파워링크 가여좀
    private void requestPowerLinkList(String url) {
        DataControlManager.getInstance().addSchedule(
                new DataControlHttpExecutor().requestPowerLink(ShopContentActivity.this,url,
                        new RequestCompletionListener() {
                            @Override
                            public void onDataControlCompleted(@Nullable Object responseData) throws Exception {
                                JHYLogger.d(responseData.toString());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (ACTIVITY_CONTENT.isDrawerOpen(CONTENT_SLIDEMENU)) {
                ACTIVITY_CONTENT.closeDrawer(CONTENT_SLIDEMENU);
                return true;
            }
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
