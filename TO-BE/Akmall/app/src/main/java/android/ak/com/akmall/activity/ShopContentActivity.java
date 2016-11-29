package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.adapter.BestAdapter;
import android.ak.com.akmall.adapter.BigCategoryAdapter;
import android.ak.com.akmall.adapter.GridAdapter;
import android.ak.com.akmall.adapter.MainCategoryAdapter;
import android.ak.com.akmall.utils.BaseUtils;
import android.ak.com.akmall.utils.Const;
import android.ak.com.akmall.utils.DataHolder;
import android.ak.com.akmall.utils.JHYLogger;
import android.ak.com.akmall.utils.http.URLManager;
import android.ak.com.akmall.utils.json.Parser;
import android.ak.com.akmall.utils.json.result.BigCategoryResult;
import android.ak.com.akmall.utils.json.result.CheckHeightResult;
import android.ak.com.akmall.utils.json.result.PageDatas;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
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

    @ViewById
    RecyclerView CONTENT_RV_LIST;
    private RecyclerView.LayoutManager mLayoutManager;

    @ViewById
    WebView CONTENT_WV_WEBVIEW;

    @ViewById
    ScrollView CONTENT_SV_SCROLL;

    @ViewById
    RecyclerView CONTENT_RV_CATEGORY;
    BigCategoryAdapter categoryAdapter;

    BigCategoryResult mBigCatgoryResult;

    @Click(R.id.MENU_CATEGORY)
    void ClickMenuCate() {
        startActivityForResult(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_CATEGORY), Const.CATEGORY_BIG_REQUEST);
    }

    @Click(R.id.MENU_SEARCH)
    void ClickMenuSearch() {

    }

    @Click(R.id.MENU_HOME)
    void ClickMenuHome() {

    }

    @Click(R.id.MENU_MYAK)
    void ClickMenuCMyak() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_MYAK));
    }

    @Click(R.id.MENU_BAG)
    void ClickMenuBag() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
    }

    @AfterViews
    void afterView() {
        //웹뷰에 각종 옵션세팅
        CONTENT_WV_WEBVIEW.clearCache(true);
        CONTENT_WV_WEBVIEW.setInitialScale(100);
        CONTENT_WV_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        CONTENT_WV_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        CONTENT_WV_WEBVIEW.getSettings().setUseWideViewPort(true);
        CONTENT_WV_WEBVIEW.getSettings().setAppCacheEnabled(false);
        CONTENT_WV_WEBVIEW.setWebContentsDebuggingEnabled(true);
        CONTENT_WV_WEBVIEW.loadUrl(URLManager.getServerUrl() + "/display/ShopFront.do?isAkApp=Y&ctgId=10");
        CONTENT_WV_WEBVIEW.setWebViewClient(new WebViewClientClass());
        CONTENT_WV_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //스크롤 차단
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        //스크롤 리스너
        CONTENT_SV_SCROLL.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = CONTENT_SV_SCROLL.getScrollY(); // For ScrollView
                float scorllDp = BaseUtils.convertPixelsToDp(scrollY, ShopContentActivity.this);
                if (scorllDp > 45) {
                    CONTENT_RV_CATEGORY.setVisibility(View.VISIBLE);
                    CONTENT_RV_CATEGORY.bringToFront();
                } else {
                    CONTENT_RV_CATEGORY.setVisibility(View.GONE);
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
        categoryAdapter = new BigCategoryAdapter(this, result.ctgArray, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = CONTENT_RV_CATEGORY.getChildLayoutPosition(v);
                if (pos == 1) {
                    //대 카테고리 선택
                    String param = "";
                    try {
//                        JSONObject json = new JSONObject();
//                        json.put("ctgInfo", result.ctgInfo);
//                        param = json.toString().replace("\"[", "[");
//                        param = param.replace("]\"", "]");
//                        param = param.replace("\\", "");
                        JHYLogger.D(mBigCatgoryResult.ctgInfo);
                        param = mBigCatgoryResult.ctgInfo;
//                        param = URLEncoder.encode(mBigCatgoryResult.ctgInfo, "UTF-8");
//                        param = Base64.encodeToString(param.getBytes(), 0);
                    } catch (Exception e) {
                        JHYLogger.e(e.getMessage());
                    }
                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "big"), Const.CATEGORY_BIG_REQUEST);
                } else if (pos == 2) {
                    //중 카테고리 선택
                    String param = "";
                    try {
//                        JSONObject json = new JSONObject();
//                        json.put("ctgInfo", result.ctgInfo);
//                        param = json.toString().replace("\"[", "[");
//                        param = param.replace("]\"", "]");
//                        param = json.toString().replace("\"{", "{");
//                        param = param.replace("}\"", "}");

                        JHYLogger.D(param);
//                        param = URLEncoder.encode(mBigCatgoryResult.ctgInfo, "UTF-8");
//                        param = Base64.encodeToString(param.getBytes(), 0);
                        param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                    } catch (Exception e) {
                        JHYLogger.e(e.getMessage());
                    }
                    startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "mid"), Const.CATEGORY_BIG_REQUEST);
                }
            }
        });
        CONTENT_RV_CATEGORY.setAdapter(categoryAdapter);

        //TODO:테스트코드 지워야 할수도이씅ㅁ
        if(null == result.dp)return;
        //하단 상품 리스트
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
            GridAdapter adapter = new GridAdapter(this, result.prodList.bestProdList.pageDatas);
            CONTENT_RV_LIST.setAdapter(adapter);
            int size = result.prodList.bestProdList.pageDatas.size();
            float listHeight;
            if (size % 2 == 0) {
                listHeight = BaseUtils.convertDpToPixel(size / 2 * 157 + (size * 2), this);
            } else {
                size = size + 1;
                listHeight = BaseUtils.convertDpToPixel(size / 2 * 157 + (size * 2), this);
            }
            CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight * 2;
            CONTENT_RV_LIST.requestLayout();
        }
    }

    //현재 선택된 탭 변수
    String currentWhat = "best";

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
            } else if(what.equals("recom")) {
                if (mBigCatgoryResult.dp.equals("2")) {
                    //그리드 형태
                    data = mBigCatgoryResult.prodList.themeList.pageDatas;
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
            GridAdapter adapter = new GridAdapter(this, data);
            CONTENT_RV_LIST.setAdapter(adapter);
            int size = data.size();
            float listHeight;
            if (size % 2 == 0) {
                listHeight = BaseUtils.convertDpToPixel((size / 2 * 157) + (size * 2), this);
            } else {
                size = size + 1;
                listHeight = BaseUtils.convertDpToPixel(size / 2 * 157 + (size * 2), this);
            }
            CONTENT_RV_LIST.getLayoutParams().height = (int) listHeight * 2;
            CONTENT_RV_LIST.requestLayout();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //카테고리 대 갔다가 옴
        if (requestCode == Const.CATEGORY_BIG_REQUEST) {
            if (resultCode == Const.CATEGORY_BIG_RESULT) {
                String url = data.getStringExtra("url");
                if (url.contains("http")) {
                    CONTENT_WV_WEBVIEW.loadUrl(data.getStringExtra("url") + "&isAkApp=Y");
                } else {
                    CONTENT_WV_WEBVIEW.loadUrl(URLManager.getServerUrl() + data.getStringExtra("url") + "&isAkApp=Y");
                }
                CONTENT_SV_SCROLL.post(new Runnable() {
                    public void run() {
                        CONTENT_SV_SCROLL.scrollTo(0, 0);
                    }
                });
            }
        }
    }

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
                    view.loadUrl("javascript:nativeCallBack('" + result.callbackJson + "')");
                    initView(result);
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
                    changeTab(Parser.parsingChangeTab(json));
                } else if (decodeString.startsWith("akmall://openWebview")) {
                    //외부 웹 링크 연결
                    String json = decodeString.replace("akmall://openWebview?", "");
                    String link = Parser.parsingOpenWebview(json);
                    if (link.startsWith("http") && !link.contains("akmall.com")) {
                        startActivity(new Intent(ShopContentActivity.this, WebviewActivity_.class).putExtra("url", link));
                    } else {
                        startActivity(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", link));
                    }
                } else if (decodeString.startsWith("akmall://callJavascriptAll")) {
                    //콜자바스크립트
                    String json = decodeString.replace("akmall://callJavascriptAll?", "");
                    String value = Parser.parsingTPString(json);
                    if (value.equals("drawRecomBrand")) {
                        //추천브랜드
                        try {
                            String param = "";
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("ctgInfo", mBigCatgoryResult.ctgInfo);
//                            param = jsonObject.toString().replace("\"[", "[");
//                            param = param.replace("]\"", "]");
//                            param = jsonObject.toString().replace("\"{", "{");
//                            param = param.replace("}\"", "}");
//                            param = URLEncoder.encode(mBigCatgoryResult.ctgInfo, "UTF-8");
//                            param = Base64.encodeToString(param.getBytes(), 0);
                            param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                            startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "brand"), Const.CATEGORY_BIG_REQUEST);
                        } catch (Exception e) {
                            JHYLogger.e(e.getMessage());
                        }
                    }else if (value.equals("drawBigCtg")) {
                        //대 카테고리 선택
                        String param = "";
                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("ctgInfo", mBigCatgoryResult.ctgInfo);
//                            param = jsonObject.toString().replace("\"[", "[");
//                            param = param.replace("]\"", "]");
//                            param = param.replace("\\", "");
//                            param = URLEncoder.encode(mBigCatgoryResult.ctgInfo, "UTF-8");
//                            param = Base64.encodeToString(param.getBytes(), 0);
                            param = mBigCatgoryResult.ctgInfo;
                        } catch (Exception e) {
                            JHYLogger.e(e.getMessage());
                        }
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "big"), Const.CATEGORY_BIG_REQUEST);
                    }else if (value.equals("drawMidcate")) {
                        //중 카테고리 선택
                        String param = "";
                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("ctgInfo", );
//                            param = jsonObject.toString().replace("\"[", "[");
//                            param = param.replace("]\"", "]");
//                            param = jsonObject.toString().replace("\"{", "{");
//                            param = param.replace("}\"", "}");

//                            param = URLEncoder.encode(mBigCatgoryResult.ctgInfo, "UTF-8");
//                            param = Base64.encodeToString(param.getBytes(), 0);
                            param = DataHolder.putDataHolder(mBigCatgoryResult.ctgInfo);
                        } catch (Exception e) {
                            JHYLogger.e(e.getMessage());
                        }
                        startActivityForResult(new Intent(ShopContentActivity.this, MyWebviewActivity_.class).putExtra("url", Const.BIG_CATEGORY).putExtra("json", param).putExtra("cate", "mid"), Const.CATEGORY_BIG_REQUEST);
                    }
                }else if(decodeString.startsWith("akmall://goBack")) {
                    view.goBack();
                }
                return true;
            } else {
                view.loadUrl(url);
                return false;
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
}
