
package com.ak.android.akmall.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.ak.android.akmall.R;
import com.ak.android.akmall.common.AkMallFacade;
import com.ak.android.akmall.common.StartActivityCallback;

public class OutsideUriHandler {

    private static final String URL_SHINHANCARD_APP_DOWN = "http://m.shinhancard.com/fw.jsp?c=a2";
    //private static final String URL_ISP_MOBILE_APP_DOWN = "https://play.google.com/store/apps/details?id=kvp.jjy.MispAndroid320";//p65458 20150727 isp mobile appp download url add -> play store 로 바로 이동하지 않고 브라우져도 실행된느 문제로 블럭
    private static final String URL_ISP_MOBILE_APP_DOWN = "market://details?id=kvp.jjy.MispAndroid320";//p65458 20150727 isp mobile appp download url add
    private static final String SCHEME_KAKAOLINK = "kakaolink:";
    private static final String SCHEME_GOOGLE_MARKET = "market:";

    private OutsideUriHandler() {
    }

    /**
     * 결제 모듈 제작측 가이드에 따라 url를 필터링 하고 있음.
     * contains로 확인하도록 되어 있는 부분에서 사이드 있을수 있음.
     * 예를 들어 검색어에 v3mobile이라고 입력하고 검색을 시도하면 (ex> http://주소/search?q=v3mobile)
     * url에 필터링 조건에 맞기 때문에 외부 웹 브라우저로 url를 던지게 됨. (사이드 발생!)
     *
     * @param context
     * @param url
     * @return
     */
    public static HandleResult handlePaymentUri(final Context context, String url) {
    	//test
    	if(url.startsWith("intent") || url.contains("cpy")|| url.contains("hanaansim")||url.contains("market://")|| url.contains("com.ahnlab.v3mobileplus")){
        	boolean isStarted = true;
        		isStarted = AkMallFacade.startv3mobileActivity(context, url);
            return new HandleResult(true, isStarted);
        }
    	//test
    	if (url.startsWith("shinhancard-sr-ansimclick")) {
            boolean isStarted = AkMallFacade.startExternalActivity(context, url,
                    R.string.not_found_payment_app);
            if (!isStarted) {
                isStarted = AkMallFacade.startExternalActivity(context, URL_SHINHANCARD_APP_DOWN);
            }

            return new HandleResult(true, isStarted);
        }

        /******************************************
         * 결제 처리
         * vguard : 삼성 ,신한
         * v3mobile : 롯데카드
         * driodxantivirus : 현대카드
         * 갤탭경우 : 스키마를 전부 소문자로 인식 대문자 사용불가.
         *******************************************/
        if (url.startsWith("intent://mvaccine")) { //20150508 minseok 신한 사이드로 추
        	boolean isStarted = AkMallFacade.startv3mobileActivity(context, url);
            return new HandleResult(true, isStarted);
        }
        else if (url.startsWith("ispmobile")) {


        	boolean isStarted = AkMallFacade.startExternalActivity(context, url,
                    R.string.not_found_payment_app);

        	//p65458 20150727 isp mobile app download url add
            if (!isStarted) {
            	/*
	        	new AlertDialog.Builder(context).setIcon(null).setTitle("ISP mobile 설치 화면으로 이동합니다.")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
	
					}
				}).create();
	        	*/
	        	isStarted = AkMallFacade.startExternalActivity(context, URL_ISP_MOBILE_APP_DOWN);
	        	
        	}
          //p65458 20150727 isp mobile app download url add
            return new HandleResult(true, isStarted);
        }
        else if (url.startsWith("vguard") // vguardstart://, vguard@#$@#://...
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

        	if (!url.startsWith("vguardend")){
        		boolean isStarted = AkMallFacade.startExternalActivity(context, url,
                    R.string.not_found_payment_app);
        	
        		return new HandleResult(true, isStarted);
    		}else{
    			return new HandleResult(true, true);
    		}
    		
        } else if(url.startsWith("intent") || url.contains("cpy")|| url.contains("hanaansim")||url.contains("market://")|| url.contains("com.ahnlab.v3mobileplus") || url.contains("ahnlabv3mobileplus")){
        	boolean isStarted = true;
        		isStarted = AkMallFacade.startv3mobileActivity(context, url);
            return new HandleResult(true, isStarted);
        }

        return new HandleResult(false, false);
    }

    public static HandleResult handleExternalUri(Context mContext, String url) {

    	// tel: 전화걸기..
        if (url.startsWith(WebView.SCHEME_TEL)) {
            AkMallFacade.showCallDialog(mContext, url);
            return new HandleResult(true, true);
        }
        
        if (url.startsWith(SCHEME_KAKAOLINK)) {
            boolean isStarted = AkMallFacade.startExternalActivity(mContext, url,
                    R.string.notfound_kakaotalk);
            return new HandleResult(true, isStarted);
        }

        if (url.startsWith(SCHEME_GOOGLE_MARKET)) {
    		boolean isStarted = AkMallFacade.startExternalActivity(mContext, url);
    		return new HandleResult(true, isStarted);
        }
        return new HandleResult(false, false);
    }
    
    /**
     * UriHandler의 처리 결과를 담는 클래스이다.
     */
    public static class HandleResult {

        /**
         * 핸들러에서 처리해야하는 경우 true, 아니면 false
         */
        public final boolean isSupport;

        /**
         * 핸들러에서 처리해야하고 정상 처리되었으면 true, 아니면 false
         */
        public final boolean isDone;

        public HandleResult(boolean isSupport, boolean isDone) {
            super();
            this.isSupport = isSupport;
            this.isDone = isDone;
        }
    }

}
