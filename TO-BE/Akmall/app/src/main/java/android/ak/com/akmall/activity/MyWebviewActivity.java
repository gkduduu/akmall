package android.ak.com.akmall.activity;

import android.ak.com.akmall.R;
import android.ak.com.akmall.utils.BaseUtils;
import android.ak.com.akmall.utils.Const;
import android.ak.com.akmall.utils.DataHolder;
import android.ak.com.akmall.utils.JHYLogger;
import android.ak.com.akmall.utils.http.URLManager;
import android.ak.com.akmall.utils.json.Parser;
import android.ak.com.akmall.utils.json.result.BigCategoryResult;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

//akmall내의 웹뷰
@EActivity(R.layout.activity_mywebview)
public class MyWebviewActivity extends Activity {

    @ViewById
    WebView WEB_WEBVIEW;

    @Click(R.id.WEB_CLOSE)
    void clickClose() {
        finish();
    }

    String json ="";
    String cate = "";

    @AfterViews
    void afterView() {
        String url = URLManager.getServerUrl() + getIntent().getStringExtra("url");
        //카테고리일때 파라미터로 쓸 sjon

        cate = BaseUtils.nvl(getIntent().getStringExtra("cate"));
        if(cate.equals("mid") || cate.equals("brand")) {
            String holderId = getIntent().getStringExtra("json");
            json = (String) DataHolder.popDataHolder(holderId);
        }else {
            json = getIntent().getStringExtra("json");
        }
        //웹뷰에 각종 옵션세팅
        WEB_WEBVIEW.clearCache(true);
        WEB_WEBVIEW.setInitialScale(100);
        WEB_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEB_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        WEB_WEBVIEW.setWebContentsDebuggingEnabled(true);
        WEB_WEBVIEW.getSettings().setUseWideViewPort(true);
        WEB_WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEB_WEBVIEW.loadUrl(url);
        WEB_WEBVIEW.setWebViewClient(new WebViewClientClass());
//        WEB_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });
        JHYLogger.d(json);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.d(url);
            if (url.startsWith("akmall://")) {
                //URL DECODE
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                JHYLogger.D(decodeString);

                if (decodeString.startsWith("akmall://closePopup")) {
                    MyWebviewActivity.this.finish();
                }else if(decodeString.startsWith("akmall://openWebview")) {
                    //대카테고리페이지에서 카테고리 클릭하면 창 끄고  이전페이지 새 카테고리로 로드함
                    if(decodeString.contains("/display/ShopFront.do")) {
                        //대카테고리
                        String link = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",link));
                        finish();
                    }else if(decodeString.contains("/display/CtgMClsf.do")) {
                        //중카테고리
                        String link = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",link));
                        finish();
                    }else if(decodeString.contains("/display/BrandShopSClsf.do")) {
                        //브랜드카테고리
                        String link = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",link));
                        finish();
                    }
                }

                return true;
            }else if(url.contains("/display/ShopFront.do")) {
                //대카테고리
                setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",url));
                finish();
            }else if(url.contains("/display/CtgMClsf.do")) {
                //중카테고리
                setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",url));
                finish();
            }else if(url.contains("/display/BrandShopSClsf.do")) {
                //중카테고리
                setResult(Const.CATEGORY_BIG_RESULT,new Intent().putExtra("url",url));
                finish();
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        boolean isFirst = true;
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(isFirst) {
                if (cate.equals("big")) {
                    view.loadUrl("javascript:drawBigCtg('" + json + "')");
                }else if(cate.equals("mid")) {
                    view.loadUrl("javascript:drawMidcate('" + json + "')");
                }else if(cate.equals("brand")) {
                    JHYLogger.D(json);
                    view.loadUrl("javascript:drawRecomBrand('" + json + "')");
                }
            }
            isFirst = false;
        }
    }
}
