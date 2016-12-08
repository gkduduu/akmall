package com.ak.android.akmall.utils;

/**
 * Created by gkdud on 2016-10-31.
 * 앱내에서 사용하는 상수값들으 ㅣ 집합
 */

public class Const {

    public final static String DAUM_API_KEY = "d7ab783d2fd4a8f22bfe3af11796d227";/* 음성인식을 위한 API KEY (gkduduu@naver.com)*/

    public final static String BOUNDARY = "hayoung2cute";

    public static final String GCM_SENDER_ID;
    public static final String SERVER_ADDR;

    static {
        if (!Feature.DEBUG_MODE) {
            GCM_SENDER_ID = "290048048463";
            //GCM_SENDER_ID = "828409247060";
            SERVER_ADDR = "m.akmall.com";
        } else {
            GCM_SENDER_ID = "828409247060";
            //SERVER_ADDR = "91.3.200.68";
            //민석선임
            //SERVER_ADDR = "91.3.115.209";
            //창욱전임
            //SERVER_ADDR = "91.3.115.184:8082";
            //SERVER_ADDR = "91.3.115.179:8082";
            SERVER_ADDR = "devm.akmall.com";
            //SERVER_ADDR = "91.3.115.209:8082";//실제 동작 url p65458 todo
        }
    }

    public final static String GCM_PUSH_TOKEN = "GCM_PUSH_TOKEN";

    /* 설정 관련 상수들 */
    public final static String ALARM_RECEIVE = "ALARM_RECEIVE";/*쇼핑정보 알림*/
    public final static String ALARM_RECEIVE2 = "ALARM_RECEIVE2";/*구매정보 알림*/
    public final static String ALARM_TIME_SETTING_ = "ALARM_TIME_SETTING_";/*알림금지 시간설정*/
    public final static String ALARM_NO_SOUND = "ALARM_NO_SOUND";/*무음알림받기*/
    public final static String MOVIE_AUTO_PLAY = "MOVIE_AUTO_PLAY";/*동영상 자동 플레이*/
    public final static String LOGIN_AUTO = "LOGIN_AUTO";/*자동로그인*/

    public final static String AUTO_LOGIN_YN = "AUTO_LOGIN_YN";/*자동로그인 여부*/

    /* 하단메뉴 url*/
    public final static String MENU_CATEGORY = "/common/menu.do?isAkApp=Android";/*카테고리*/
    public final static String MENU_MYAK = "/mypage/MyPlaceMain.do?isAkApp=Android;";/*MY AK*/
    public final static String MENU_BAG = "/order/ShoppingCart.do?isAkApp=Android";/*장바구니*/
    public final static String MENU_HOME = "/main/Main.do";/*메인*/
    public final static String MENU_SEARCH = "/search/SearchMain.do?isAkApp=Android";/*검색*/

    /*flaoting menu url*/
    public final static String MENU_PICK = "/mypage/SmartPickVoucherList.do"; /*pic*/
    public final static String MENU_VIP = "/event/VipRounge.do"; /*vip라운지*/
    public final static String MENU_COUPON = "/event/CouponZone.do";/*쿠폰존*/
    public final static String MENU_LIKE = "/mypage/MyBelongingGoods.do";/*라이크잇*/
    public final static String MENU_HISTORY = "/common/AppPage?forwardPage=HistoryLayer";/*히스토리*/

    /*카테고리 관련 URL*/
    public final static String BIG_CATEGORY = "/common/AppPage?forwardPage=CateLayer";/*대카테고리*/
    public final static String MOVE_FILTER = "/common/AppPage?forwardPage=FilterLayer";/*필터*/

    /*widget*/
    public final static String WIDGET_COUPON = "/mypage/DiscCoupon.do?isAkApp=Android";/*보유쿠폰*/
    public final static String WIDGET_CHECK = "/event/RightVisit.do?isAkApp=Android";/*추럭체크*/
    public final static String WIDGET_EVENT = "/event/EventMain.do?isAkApp=Android";/*이벤트*/
    public final static String WIDGET_DELIVERY = "/mypage/OrderDeliInquiry.do?isAkApp=Android";/*주문배송조회*/
    public final static String WIDGET_MY = " /mypage/MyPlaceMain.do?isAkApp=Android";/*나만의공간*/

    /*상품 메뉴 관련 url*/
    public final static String ITME_SHARE = "/goods/pSnsShare.do?isAkApp=Android&native=Y"; /*공유하기*/
    public final static String ITME_HEART = "/common/AppPage?forwardPage=pWishInpt"; /*찜하기하기*/

    /*activityforresult 의 response 코드*/
    public final static int CATEGORY_BIG_REQUEST = 7001; // 대카테고리 리퀘스트
    public final static int CATEGORY_BIG_RESULT = 6001; // 대카테고리 리퀘스트
    public final static int VOICE_REQUEST = 7002; // 음성인식 리퀘스트
    public final static int VOICE_RESULT = 6002; // 음성인식 리퀘스트
    public final static int CLICK_GO_HOME_SO_CLOSE_REQUEST = 7003; // 홈버튼을 눌렀을 경우 모든 액티비티스택 FINISH
    public final static int CLICK_GO_HOME_SO_CLOSE_RESULT = 6003; // 홈버튼을 눌렀을 경우 모든 액티비티스택
    public final static int MORE_REQUEST = 7004; //
    public final static int MORE_RESULT = 6004; //

    /* 액티비티간 통신시 extra 상수(안쓰다가 중간부터 사용해서 안쓸떄도 있음..) */
    public final static String REQUEST_URL = "REQUEST_URL"; /* 액티비티 전환시 전달하는 url*/

    /*상품 페이지 대카테고리인지 중소카테고리인지 브랜듼지 */
    public final static String CONTENT_BIG_CATEGORY = "CONTENT_BIG_CATEGORY";
    public final static String CONTENT_MID_CATEGORY = "CONTENT_MID_CATEGORY";
    public final static String CONTENT_SMALL_CATEGORY = "CONTENT_SMALL_CATEGORY";
    public final static String CONTENT_SMALL2_CATEGORY = "CONTENT_SMALL2_CATEGORY";
    public final static String CONTENT_BRAND_SHOP = "CONTENT_BRAND_SHOP";
}
