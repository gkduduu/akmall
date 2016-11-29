package android.ak.com.akmall.utils;

/**
 * Created by gkdud on 2016-10-31.
 * 앱내에서 사용하는 상수값들으 ㅣ 집합
 */

public class Const {

    public final static String DAUM_API_KEY = "056d36313be683772dd4d3662952304b";/* 음성인식을 위한 API KEY (gkduduu@naver.com)*/

    /* 설정 관련 상수들 */
    public final static String ALARM_RECEIVE = "ALARM_RECEIVE";/*쇼핑정보 알림*/
    public final static String ALARM_RECEIVE2 = "ALARM_RECEIVE2";/*구매정보 알림*/
    public final static String ALARM_TIME_SETTING_ = "ALARM_TIME_SETTING_";/*알림금지 시간설정*/
    public final static String ALARM_NO_SOUND = "ALARM_NO_SOUND";/*무음알림받기*/
    public final static String MOVIE_AUTO_PLAY = "MOVIE_AUTO_PLAY";/*동영상 자동 플레이*/
    public final static String LOGIN_AUTO = "LOGIN_AUTO";/*자동로그인*/

    /* 하단메뉴 url*/
    public final static String MENU_CATEGORY = "/common/menu.do";/*카테고리*/
    public final static String MENU_MYAK = "/mypage/MyPlaceMain.do";/*MY AK*/
    public final static String MENU_BAG = "/order/ShoppingCart.do";/*장바구니*/
    public final static String MENU_HOME = "/main/Main.do";/*메인*/

    /*카테고리 관련 URL*/
    public final static String BIG_CATEGORY = "/common/AppPage?forwardPage=CateLayer";/*대카테고리*/

    /*상품 메뉴 관련 url*/
    public final static String ITME_SHARE = "/goods/pSnsShare.do?isAkApp=Y"; /*공유하기*/
    public final static String ITME_HEART = "/common/AppPage?forwardPage=pWishInpt"; /*찜하기하기*/

    /*activityforresult 의 response 코드*/
    public final static int CATEGORY_BIG_REQUEST = 7001; // 대카테고리 리퀘스트
    public final static int CATEGORY_BIG_RESULT = 6001; // 대카테고리 리퀘스트
}
