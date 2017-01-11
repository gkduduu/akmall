package com.ak.android.akmall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.ak.android.akmall.fragment.SelectPopup;
import com.ak.android.akmall.utils.BaseUtils;
import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.DataHolder;
import com.ak.android.akmall.utils.Feature;
import com.ak.android.akmall.utils.JHYLogger;
import com.ak.android.akmall.utils.RealPathUtil;
import com.ak.android.akmall.utils.UriProvider;
import com.ak.android.akmall.utils.WebViewImageUploadHelper;
import com.ak.android.akmall.utils.blurbehind.BlurBehind;
import com.ak.android.akmall.utils.blurbehind.OnBlurCompleteListener;
import com.ak.android.akmall.utils.http.URLManager;
import com.ak.android.akmall.utils.json.Parser;
import com.ak.android.akmall.utils.json.result.LogStateResult;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;
import com.ak.android.akmall.utils.json.result.SMSResult;
import com.ak.android.akmall.utils.json.result.SharedResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

//akmall내의 웹뷰
@EActivity(R.layout.activity_mywebview)
public class MyWebviewActivity extends Activity {

    @ViewById
    WebView WEB_WEBVIEW;

    @ViewById
    DrawerLayout MY_SLIDELAYOUT;
    @ViewById
    WebView SLIDE_WEBVIEW;
    @ViewById
    LinearLayout MY_SLIDEMENU;

    @ViewById
    LinearLayout MENU_LAYOUT;
    @ViewById
    RelativeLayout FLOATING_LAYOUT;

    @ViewById
    ImageView img_webview_logo;

    private Context context = MyWebviewActivity.this;
    private ProgressBar mBarProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Feature.addAcitivty(this);
        CookieSyncManager.createInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Feature.removeAcitivty(this);
    }

    @Click(R.id.WEB_CLOSE)
    void clickClose() {
        finish();
    }

    @Click(R.id.FLOATING_MORE)
    void clickMore() {
//        BlurBehind.getInstance().execute(this, new OnBlurCompleteListener() {
//                    @Override
//                    public void onBlurComplete() {
//                        Intent intent = new Intent(MyWebviewActivity.this, MoreActivity_.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        startActivityForResult(intent, Const.MORE_REQUEST);
//                    }
//                }
//        );
//        startActivityForResult(new Intent(MyWebviewActivity.this, WebviewActivity_.class)
//                .putExtra("url", URLManager.getServerUrl()+Const.MENU_HISTORY), Const.CATEGORY_BIG_REQUEST);
    }

    @Click(R.id.FLOATING_TOP)
    void clickTop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WEB_WEBVIEW.scrollTo(0, 0);
            }
        }, 10);
    }

    @Click(R.id.FLOATING_BACK)
    void clickBack() {
        if (WEB_WEBVIEW.canGoBack()) {
            WEB_WEBVIEW.goBack();
        } else {
            finish();
        }
    }

    @Click(R.id.MENU_CATEGORY)
    void ClickMenuCate() {
        startActivityForResult(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_SHOPPINGALIM), Const.CATEGORY_BIG_REQUEST);
    }

    @Click(R.id.MENU_LIKEIT)
    void ClickMenuSearch() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_LIKEIT));
    }

    @Click(R.id.MENU_HOME)
    void ClickMenuHome() {
        Feature.closeAllActivity();
        Feature.currentMain.refreshWeb();
    }

    @Click(R.id.MENU_MYAK)
    void ClickMenuCMyak() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_MYAK));
    }

    @Click(R.id.MENU_BAG)
    void ClickMenuBag() {
        startActivity(new Intent(this, MyWebviewActivity_.class).putExtra("url", Const.MENU_BAG));
    }

    String json = "";
    String cate = "";

    private static final String TYPE_IMAGE = "image/*";
    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;


    @AfterViews
    void afterView() {
        JHYLogger.d("<< MyWebviewActivity afterView() >>");

        if(Build.VERSION.SDK_INT >= 17) {
            WEB_WEBVIEW.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        String url = "";

        //isp 결제 후 return url을 받는 과정 from asis
        Uri data = getIntent().getData();
        if (data != null) {
            if(!UriProvider.SCHEME_AKMALL.equals(data.getScheme())) {
                String tmpURL = data.toString();
                String lastURL = "";

                if (tmpURL.startsWith("mtracker")) {
                    lastURL = tmpURL.substring(tmpURL.indexOf("http"));
                } else {
                    lastURL = tmpURL;
                }
                url = lastURL;
            } else {
                url= data.getQueryParameter("returnUrl");
            }
        }

        String extraUrl = getIntent().getStringExtra("url");
        JHYLogger.d("extraUrl >> " + extraUrl);
        if(url.equals("")) {
            if (extraUrl.contains(URLManager.getServerUrl()) || extraUrl.contains("recopick.com")) {
                url = extraUrl;

            } else if(extraUrl.contains("http://www.akmall.com/") || extraUrl.startsWith("https:")){
                url = extraUrl;

            } else  {
                url = URLManager.getServerUrl() + extraUrl;
            }
        }

        //카테고리일때 파라미터로 쓸 sjon
        cate = BaseUtils.nvl(getIntent().getStringExtra("cate"));
        if (!cate.equals("")) {
            //카테고리일 경우 하단 레이아웃과 플로팅 버튼 gone
            MENU_LAYOUT.setVisibility(View.GONE);
            FLOATING_LAYOUT.setVisibility(View.GONE);
        }

        if (cate.equals("mid") || cate.equals("brand") || cate.equals("small") || cate.equals("small2") || cate.equals("filter")) {
            String holderId = getIntent().getStringExtra("json");
            json = (String) DataHolder.popDataHolder(holderId);
        } else {
            json = getIntent().getStringExtra("json");
        }

        JHYLogger.d("MyWebiew 최종 url = "+url);

        this.mBarProgressBar = (ProgressBar) findViewById(R.id.barProgressBar);

        //웹뷰에 각종 옵션세팅
        WEB_WEBVIEW.getSettings().setTextZoom(100);
        WEB_WEBVIEW.clearCache(true);
//        WEB_WEBVIEW.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WEB_WEBVIEW.getSettings().setJavaScriptEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WEB_WEBVIEW.setWebContentsDebuggingEnabled(true);
        }
        WEB_WEBVIEW.getSettings().setUseWideViewPort(true);
//        WEB_WEBVIEW.getSettings().setAppCacheEnabled(false);
        WEB_WEBVIEW.loadUrl(url);
        WEB_WEBVIEW.setWebViewClient(new WebViewClientClass());
        WEB_WEBVIEW.setWebChromeClient(new ChromeClient());
        WEB_WEBVIEW.requestFocus(View.FOCUS_DOWN | View.FOCUS_UP);
        WEB_WEBVIEW.getSettings().setLightTouchEnabled(true);
        WEB_WEBVIEW.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        WEB_WEBVIEW.getSettings().setSupportMultipleWindows(true);

        WEB_WEBVIEW.getSettings().setBuiltInZoomControls(true);
        WEB_WEBVIEW.getSettings().setSupportZoom(true);

        if(Build.VERSION.SDK_INT >= 17) {
            WEB_WEBVIEW.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            WEB_WEBVIEW.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(WEB_WEBVIEW, true);
        }

        WEB_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

//        SLIDE_WEBVIEW.setInitialScale(300);
        SLIDE_WEBVIEW.getSettings().setTextZoom(100);
        SLIDE_WEBVIEW.getSettings().setJavaScriptEnabled(true);
        SLIDE_WEBVIEW.getSettings().setUseWideViewPort(true);

        SLIDE_WEBVIEW.getSettings().setBuiltInZoomControls(true);
        SLIDE_WEBVIEW.getSettings().setSupportZoom(true);
        SLIDE_WEBVIEW.setWebViewClient(new WebViewClientClass());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SLIDE_WEBVIEW.setWebContentsDebuggingEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SLIDE_WEBVIEW.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(SLIDE_WEBVIEW, true);
        }

        SLIDE_WEBVIEW.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        //슬라이드 메뉴
        MY_SLIDELAYOUT.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        MY_SLIDELAYOUT.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        MY_SLIDELAYOUT.setFocusableInTouchMode(false);

        JHYLogger.d(json);
    }

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.VOICE_REQUEST) {
            if (resultCode == Const.VOICE_RESULT) {
                String voiceResult = BaseUtils.nvl(data.getStringExtra("result"));
                voiceResult = voiceResult.replace("+"," ");
                JHYLogger.D("MyWebviewActivity() <<--- onActivityResult : voiceResult --->> "+voiceResult);
                try {
                    voiceResult = URLEncoder.encode(voiceResult, "UTF-8");
                    voiceResult = Base64.encodeToString(voiceResult.getBytes(), 0);
                } catch (Exception e) {
                    JHYLogger.e(e.getMessage());
                }
                WEB_WEBVIEW.loadUrl("javascript:voiceSearch('" + voiceResult + "')");
            }
        } else if (requestCode == Const.MORE_REQUEST) {
            if (resultCode == Const.MORE_RESULT) {
                SLIDE_WEBVIEW.loadUrl(URLManager.getServerUrl() + Const.MENU_HISTORY);
                MY_SLIDELAYOUT.openDrawer(MY_SLIDEMENU);
            }

        } else if (requestCode == INPUT_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mFilePathCallback == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri[] results = new Uri[]{getResultUri(data)};

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            } else {
                if (mUploadMessage == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri result = getResultUri(data);

                Log.d(getClass().getName(), "openFileChooser : "+result);
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else {
            if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
            if (mUploadMessage != null) mUploadMessage.onReceiveValue(null);
            mFilePathCallback = null;
            mUploadMessage = null;
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Uri getResultUri(Intent data) {
        Uri result = null;
        if(data == null || TextUtils.isEmpty(data.getDataString())) {
            // If there is not data, then we may have taken a photo
            if(mCameraPhotoPath != null) {
                result = Uri.parse(mCameraPhotoPath);
            }
        } else {
            String filePath = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                filePath = data.getDataString();
            } else {
                filePath = "file:" + RealPathUtil.getRealPath(this, data.getData());
            }
            result = Uri.parse(filePath);
        }

        return result;
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            mBarProgressBar.setProgress(progress);

            if(progress > 10) {
                SLIDE_WEBVIEW.setVisibility(View.VISIBLE);
                WEB_WEBVIEW.setVerticalScrollBarEnabled(true);
                WEB_WEBVIEW.setHorizontalScrollBarEnabled(true);

            } else {
                SLIDE_WEBVIEW.setVisibility(View.GONE);
            }

            if(progress > 30) {
                img_webview_logo.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {

            WebView.HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            JHYLogger.D("My _blank >> "+data);
            Context context = view.getContext();
            Intent browserIntent = new Intent(MyWebviewActivity.this,WebviewActivity_.class ).putExtra("url",data);
            context.startActivity(browserIntent);

            final WebSettings settings = view.getSettings();
            settings.setDomStorageEnabled(true);
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(true);
            view.setWebChromeClient(this);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView w) {
            super.onCloseWindow(w);
            finish();
        }

//        @Override
//        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
//
//            return false;
//        }

        // For Android Version < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            //System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(TYPE_IMAGE);
            startActivityForResult(intent, INPUT_FILE_REQUEST_CODE);
        }

        // For 3.0 <= Android Version < 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            //System.out.println("WebViewActivity 3<A<4.1, OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU,aT), n=2");
            openFileChooser(uploadMsg, acceptType, "");
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.d(getClass().getName(), "openFileChooser : "+acceptType+"/"+capture);
            openFileChooser( uploadMsg, "" );
        }

        // For 4.1 <= Android Version < 5.0
//        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
//            Log.d(getClass().getName(), "openFileChooser : "+acceptType+"/"+capture);
//            mUploadMessage = uploadFile;
//            imageChooser();
//        }

        // For Android Version 5.0+
        // Ref: https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            System.out.println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3");
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;
            imageChooser();
            return true;
        }

        private void imageChooser() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(getClass().getName(), "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType(TYPE_IMAGE);

            Intent[] intentArray;
            if(takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
        }
    }

    private static final String URL_SHINHANCARD_APP_DOWN = "http://m.shinhancard.com/fw.jsp?c=a2";
    private static final String URL_ISP_MOBILE_APP_DOWN = "market://details?id=kvp.jjy.MispAndroid320";//p65458 20150727 isp mobile appp download url add


    private class WebViewClientClass extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            JHYLogger.d("MyWebviewActivity >> " + url);

            if(url.startsWith("newtab:")) {
                url = url.replace("newtab:", "");
                startActivity(new Intent(MyWebviewActivity.this, WebviewActivity_.class).putExtra("url", url));
                return true;
            }

            if (url.contains("/main/Main.do")) {
                if(url.startsWith("http://m2.akmall.com/")) {
                    startActivity(new Intent(MyWebviewActivity.this, MainActivity_.class).putExtra("url", url));
                    finish();
                } else {
                    Feature.closeAllActivity();
                    Feature.currentMain.refreshWebMoveTab(url);
                }
                return true;
            }

            if (url.startsWith("akmall://")) {
                //URL DECODE
                String decodeString = "";
                try {
                    decodeString = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    JHYLogger.e(e.getMessage());
                }
                JHYLogger.D("MyWebviewActivity() akmall --->> " + decodeString);

                if (decodeString.startsWith("akmall://closePopup")) {
                    MyWebviewActivity.this.finish();

                } else if (decodeString.startsWith("akmall://openWebview")) {
                    //대카테고리페이지에서 카테고리 클릭하면 창 끄고  이전페이지 새 카테고리로 로드함
                    if (decodeString.contains("/display/ShopFront.do")) {
                        //대카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/CtgMClsf.do")) {
                        //중카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/CtgSClsf.do")) {
                        //소카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/BrandCtgMClsf.do")) {
                        //브랜드카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);
                    } else if (decodeString.contains("/display/BrandCtgSClsf.do")) {
                        //브랜드카테고리
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        goContentWebview(openReult.url);

                    } else if (decodeString.contains("/planshop/PlanShopView.do") || decodeString.contains("/event/EventDetail.do")
                            || decodeString.contains("/display/BrandCtgMClsf.do") || decodeString.contains("/display/CtgSClsf.do")
                            || decodeString.contains("/special/ChanelMain.do") || decodeString.contains("/special/Special.do")) {
                        //추천브랜드
                        OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openWebview?", ""));
                        view.loadUrl(URLManager.getServerUrl() + openReult.url);
                    }
                }

                else if (decodeString.startsWith("akmall://voiceSearch")) {
                    startActivityForResult(new Intent(MyWebviewActivity.this, VoiceActivity_.class), Const.VOICE_REQUEST);

                } else if (decodeString.startsWith("akmall://setup")) {
                    startActivity(new Intent(MyWebviewActivity.this, SettingActivity_.class));

                } else if (decodeString.startsWith("akmall://changeFilter")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://changeFilter?", ""));
                    goContentWebview(openReult.url);

                } else if (decodeString.startsWith("akmall://openBrowser")) {
                    OpenWebViewResult openReult = Parser.parsingOpenWebview(decodeString.replace("akmall://openBrowser?", ""));
                    goOpenBrowser(openReult.url);

                }  else if(decodeString.startsWith("akmall://sms")) {
                    //문자보내기
                    SMSResult openReult = Parser.parsingSMS(decodeString.replace("akmall://sms?", ""));

                    String txt = openReult.t;
                    JHYLogger.d("sms body => " + txt);

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", txt); // 보낼 문자
                    sendIntent.putExtra("address", ""); // 받는사람 번호
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(sendIntent);

                } else if (decodeString.startsWith("akmall://goBack")) {
                    if (WEB_WEBVIEW.canGoBack()) {
                        WEB_WEBVIEW.goBack();
                    } else {
                        finish();
                    }
//                } else if (decodeString.startsWith("akmall://showGNB")) {
//                    MENU_LAYOUT.setVisibility(View.VISIBLE);
//                } else if (decodeString.startsWith("akmall://hideGNB")) {
//                    MENU_LAYOUT.setVisibility(View.GONE);
//                } else if (decodeString.startsWith("akmall://showFloat")) {
//                    FLOATING_LAYOUT.setVisibility(View.VISIBLE);
//                } else if (decodeString.startsWith("akmall://hideFloat")) {
//                    FLOATING_LAYOUT.setVisibility(View.GONE);
                } else if (decodeString.startsWith("akmall://callpWishInpt")) {
                    String json = decodeString.replace("akmall://callpWishInpt?", "");
                    SharedResult result = Parser.parsingShared(json);

                    if(!MainActivity_.loginCheck) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.setTitle("AK MALL");
                        alert.setMessage("로그인 후 이용해주세요.");
                        alert.show();

                    } else {
                        new SelectPopup(context, Const.ITME_HEART + "&goods_id=" + result.goodsId).show();
                    }

                } else if (decodeString.startsWith("akmall://callShareLayer")) {
                    String json = decodeString.replace("akmall://callShareLayer?", "");
                    SharedResult result = Parser.parsingShared(json);

                    new SelectPopup(context, Const.ITME_SHARE + "&goods_id=" + result.goodsId).show();

                } else if (decodeString.startsWith("akmall://callShoopingCart")) {
                    String json = decodeString.replace("akmall://callShoopingCart?", "");
                    SharedResult result = Parser.parsingShared(json);

                    view.loadUrl(URLManager.getServerUrl() + "/goods/GoodsDetail.do?goods_id="+result.goodsId);

//                    startActivity(new Intent(MyWebviewActivity.this, MyWebviewActivity_.class).putExtra("url", "/goods/GoodsDetail.do?goods_id="+result.goodsId));

                } else if (decodeString.startsWith("akmall://callWishPopup")) {
                    new SelectPopup(context, Const.ITME_HEART + "&goods_id=" + decodeString.replace("akmall://callWishPopup?", "")).show();

                }else if (decodeString.contains("logState")) {
                    BaseUtils.updateWidget(MyWebviewActivity.this);
                    LogStateResult logStateResult = Parser.parsingLogState(decodeString.replace("akmall://logState?", ""));

                    if(logStateResult.r.equals("Y"))
                        MainActivity_.loginCheck = true;
                    else
                        MainActivity_.loginCheck = false;
                    return true;
                } else if (decodeString.startsWith("akmall://clipboard")) {
                    //클립보드 복사
                    String link = Parser.parsingTString(decodeString.replace("akmall://clipboard?", ""));
                    ClipboardManager clipboardManager = (ClipboardManager) MyWebviewActivity.this.getSystemService(MyWebviewActivity.this.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", link);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MyWebviewActivity.this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (url.contains("/display/ShopFront.do")) {
                //대카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                JHYLogger.d("<< Url >> " + url);
                goContentWebview(url);
            } else if (url.contains("/display/CtgMClsf.do") || url.contains("/display/CtgSClsf.do")) {
                //중카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                JHYLogger.d("<< Url >> " + url);
                goContentWebview(url);
            } else if (url.contains("/display/BrandCtgMClsf.do") || url.contains("/display/BrandCtgSClsf.do")) {
                //브랜드카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                JHYLogger.d("<< Url >> " + url);
                goContentWebview(url);

            } else if (url.contains("/common/AppPage.do")) {
                //대카테고리
                url = url.replace(URLManager.getServerUrl(), "");
                JHYLogger.d("<< Url >> " + url);
                goContentWebview(url);
            }

            //결제모듈 by asis
            if (url.startsWith("intent") || url.contains("cpy") || url.contains("hanaansim") || url.contains("market://") || url.contains("com.ahnlab.v3mobileplus") )  {
                boolean isStarted = true;
                isStarted = BaseUtils.startv3mobileActivity(MyWebviewActivity.this, url);
                return true;
            }
            //test
            if (url.startsWith("shinhancard-sr-ansimclick")) {
                boolean isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, url, R.string.abc_action_bar_home_description);
                if (!isStarted) {
                    isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, URL_SHINHANCARD_APP_DOWN, R.string.abc_action_bar_home_description);
                }
                return true;
            }
            /******************************************
             * 결제 처리
             * vguard : 삼성 ,신한
             * v3mobile : 롯데카드
             * driodxantivirus : 현대카드
             * 갤탭경우 : 스키마를 전부 소문자로 인식 대문자 사용불가.
             *******************************************/
            if (url.startsWith("intent://mvaccine")) { //20150508 minseok 신한 사이드로 추
                boolean isStarted = BaseUtils.startv3mobileActivity(MyWebviewActivity.this, url);
                return true;

            } else if (url.startsWith("ispmobile")) {
                boolean isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, url,
                        R.string.abc_action_bar_home_description);

                if (!isStarted) {
                    isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, URL_ISP_MOBILE_APP_DOWN, R.string.abc_action_bar_home_description);
                }
            } else if (url.startsWith("vguard") // vguardstart://, vguard@#$@#://...
                    || url.contains("droidxantivirus")
                    //|| url.contains("v3mobile")
                    || url.startsWith("smshinhanansimclick")
                    || url.startsWith("smshinhancardusim")
                    // || url.contains("vbv")
                    // 현대카드. 2012.02.29일. From: 이정진(NewBiz마케팅팀, jbaram@hyundaicard.com)
                    || url.startsWith("smhyundaiansimclick")
                    || url.contains("ansimclick")
                    || url.startsWith("Lottesmartpay")
                    || url.startsWith("lottesmartpay")
                    || url.startsWith("lotteappcard")) {

                if (!url.startsWith("vguardend")) {
                    boolean isStarted = BaseUtils.startExternalActivity(MyWebviewActivity.this, url, R.string.abc_action_bar_home_description);
                    return true;

                } else {
                    return true;
                }
            } else if (url.startsWith("intent") ||
                    url.contains("cpy") ||
                    url.contains("hanaansim") ||
                    url.contains("market://") ||
                    url.contains("com.ahnlab.v3mobileplus") ||
                    url.contains("ahnlabv3mobileplus")) {

                boolean isStarted = true;
                isStarted = BaseUtils.startv3mobileActivity(MyWebviewActivity.this, url);
                return true;

            } else {
                view.loadUrl(url);
            }

            return true;
        }

        @Override
        public void onReceivedError(final WebView view, int errorCode, String description, final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            switch (errorCode) {
                case ERROR_AUTHENTICATION:               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:                            // 잘못된 URL
                case ERROR_CONNECT:                           // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:     // SSL handshake 수행 실패
                case ERROR_FILE:                                   // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:                // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:            // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:                               // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:                // 너무 많은 리디렉션
                case ERROR_TIMEOUT:                          // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:            // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:                         // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:  // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:

                    if( !(failingUrl.startsWith(URLManager.getServerUrl()) || failingUrl.contains("/search/PowerLink.do")) ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyWebviewActivity.this);
                        builder.setPositiveButton("앱종료", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.finishAffinity(MyWebviewActivity.this);
                            }
                        });

                        builder.setNegativeButton("재시도", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                view.loadUrl(failingUrl);
                            }
                        });
                        builder.setTitle("AK MALL");
                        builder.setMessage("네트워크에 접속할 수 없습니다. 네트워크 연결상태를 확인해 주세요.");
                        builder.show();
                    }

                    break;          // URI가 지원되지 않는 방식
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mBarProgressBar.setVisibility(View.VISIBLE);
        }

        boolean isFirst = true;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mBarProgressBar.setVisibility(View.GONE);

            CookieSyncManager.getInstance().sync();
            if (isFirst) {
                if (cate.equals("big")) {
                    view.loadUrl("javascript:drawBigCtg('" + json + "')");
                } else if (cate.equals("mid")) {
                    view.loadUrl("javascript:drawMidcate('" + json + "')");
                } else if (cate.equals("brand")) {
                    view.loadUrl("javascript:drawRecomBrand('" + json + "')");
                } else if (cate.equals("small")) {
                    view.loadUrl("javascript:drawSamCate('" + json + "')");
                } else if (cate.equals("small2")) {
                    view.loadUrl("javascript:drawSam2Cate('" + json + "')");
                } else if (cate.equals("filter")) {
                    view.loadUrl("javascript:drawFilter('" + json + "')");
                }
            }
            isFirst = false;
        }

    }

    // 오픈브라우저
    private void goOpenBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //상품 리스트 페이지로 이동
    private void goContentWebview(String link) {
        if(link.startsWith("https://m2.akmall.com")) {
            link = link.replace("https://m2.akmall.com", "");
        }

        JHYLogger.d("goContentWebview() >> link = " + link);

        if (getIntent().getBooleanExtra("isReload", false)) {
            JHYLogger.d("goContentWebview() if() >> ");
            setResult(Const.CATEGORY_BIG_RESULT, new Intent().putExtra("url", URLManager.getServerUrl() + link));
            finish();

        } else {
            JHYLogger.d("goContentWebview() else() >> ");
            startActivity(new Intent(MyWebviewActivity.this, ShopContentActivity_.class).putExtra("url", link));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (MY_SLIDELAYOUT.isDrawerOpen(MY_SLIDEMENU)) {
                MY_SLIDELAYOUT.closeDrawer(MY_SLIDEMENU);
                return true;
            } else {
                if(WEB_WEBVIEW.canGoBack()) {
                    WEB_WEBVIEW.goBack();
                } else {
                    finish();
                }
            }
//            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
