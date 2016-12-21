package com.ak.android.akmall.utils.json;

import com.ak.android.akmall.utils.Const;
import com.ak.android.akmall.utils.json.result.AddingResult;
import com.ak.android.akmall.utils.json.result.ChangeParamResult;
import com.ak.android.akmall.utils.json.result.MainPopupResult;
import com.ak.android.akmall.utils.json.result.MainResult;
import com.ak.android.akmall.utils.json.result.OpenWebViewResult;
import com.ak.android.akmall.utils.json.result.PageDatas;
import com.ak.android.akmall.utils.json.result.PowerLinkResult;
import com.ak.android.akmall.utils.json.result.ProductResult;
import com.ak.android.akmall.utils.json.result.BigCategoryResult;
import com.ak.android.akmall.utils.json.result.CheckHeightResult;
import com.ak.android.akmall.utils.json.result.PushSettingResult;
import com.ak.android.akmall.utils.json.result.SMSResult;
import com.ak.android.akmall.utils.json.result.SplashResult;
import com.ak.android.akmall.utils.json.result.UserInfoResult;

import com.ak.android.akmall.utils.json.result.WidgetResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkduuu on 2016-11-15.
 * 서버 결과 파싱해줌
 */
public class Parser {

    public static MainResult parsingMain(String response) {
        MainResult result = new MainResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.toString(), MainResult.class);
        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();
        }
        return result;
    }

    //베스트 파
    public static ProductResult parsingBestProduct(String response) {
        ProductResult result = new ProductResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.getString("productList"), ProductResult.class);
        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();
        }
        return result;
    }

    //대카테고리 파싱
    public static BigCategoryResult parsingBigCategory(String response) {
        BigCategoryResult result = new BigCategoryResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.toString(), BigCategoryResult.class);
            if(!res.isNull("action")) {
                result.callbackJson = res.getJSONObject("action").toString();
            }

            //테마리스트 담기
            List<PageDatas> themeData = new ArrayList<>();
            if(!res.getJSONObject("prodList").isNull("themeList_TH")){
                JSONArray themelist = res.getJSONObject("prodList").getJSONArray("themeList_TH");
                for (int i=0;i<themelist.length();i++) {
                    JSONArray themeitem = themelist.getJSONObject(i).getJSONArray("themeItemList_TH");
                    for(int j=0;j<themeitem.length();j++) {
                        PageDatas data =  new ObjectMapper().readValue(themeitem.getJSONObject(j).toString(), PageDatas.class);
                        themeData.add(data);
                    }
                }
            }
            result.prodList.themeList = new ProductResult();
            result.prodList.themeList.pageDatas = themeData;

        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();
        }
        return result;
    }

    public static AddingResult parsingAddingContent(String response) {
        AddingResult result = new AddingResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.toString(), AddingResult.class);
        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();
        }
        return result;
    }

    //웹뷰 크기 및 타입 가져오기
    public static CheckHeightResult parsingCheckHeight(String response) {
        CheckHeightResult result = new CheckHeightResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.toString(), CheckHeightResult.class);
        } catch (IOException e) {
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();
        }
        return result;
    }

    //탭 전환 클릭
    public static String parsingChangeTab(String response) {
        try {
            JSONObject res = new JSONObject(response);
            return res.getString("key") + Const.BOUNDARY + res.getString("h");
        }  catch (JSONException e) {
            e.getMessage();
        }
        return "";
    }

    //openwebview url 가져오기
    public static OpenWebViewResult parsingOpenWebview(String response) {
        try {
            JSONObject res = new JSONObject(response);
            return new ObjectMapper().readValue(res.toString(), OpenWebViewResult.class);
        }  catch (Exception e) {
            e.getMessage();
        }
        return new OpenWebViewResult();
    }

    //sms 내용 가져오기
    public static SMSResult parsingSMS(String response) {
        try {
            JSONObject res = new JSONObject(response);
            return new ObjectMapper().readValue(res.toString(), SMSResult.class);
        }  catch (Exception e) {
            e.getMessage();
        }
        return new SMSResult();
    }

    //openwebview url 가져오기
    public static ChangeParamResult parsingChangeParam(String response) {
        ChangeParamResult result = new ChangeParamResult();
        try {
            JSONObject res = new JSONObject(response);
            result = new ObjectMapper().readValue(res.toString(), ChangeParamResult.class);
            result.act = res.getString("action");
        }  catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    //t 가져오기(공유에서 sms나 클립보드
    public static String parsingTString(String response) {
        try {
            JSONObject res = new JSONObject(response);
            return res.getString("t");
        }  catch (JSONException e) {
            e.getMessage();
        }
        return "";
    }
    //tp가져오기
    public static String parsingTPString(String response) {
        try {
            JSONObject res = new JSONObject(response);
            return res.getString("tp");
        }  catch (JSONException e) {
            e.getMessage();
        }
        return "";
    }

    public static PushSettingResult parsingPushSetting(String response) {
        PushSettingResult result = new PushSettingResult();
        try {
            JSONObject res = new JSONObject(response);
            result.end_hh = res.getJSONObject("resultDatas").getJSONArray("denytime").getJSONObject(0).getString("END_HH");
            result.start_hh = res.getJSONObject("resultDatas").getJSONArray("denytime").getJSONObject(0).getString("START_HH");
            result.BUY_ALARM_YN = res.getJSONObject("resultDatas").getJSONArray("denytime").getJSONObject(0).getString("BUY_ALARM_YN");
            result.SHOPPING_ALARM_YN = res.getJSONObject("resultDatas").getJSONArray("denytime").getJSONObject(0).getString("SHOPPING_ALARM_YN");
            result.NOSOUND_YN = res.getJSONObject("resultDatas").getJSONArray("denytime").getJSONObject(0).getString("NOSOUND_YN");
        }  catch (JSONException e) {
            e.getMessage();
        }
        return result;
    }

    //설정진입시 유저 정보
    public static UserInfoResult parsingUserInfo(String response) {
        UserInfoResult result = new UserInfoResult();
        try {
            JSONObject res = new JSONObject(response);
            if(!res.isNull("userInfo")) {
                result = new ObjectMapper().readValue(res.getString("userInfo"), UserInfoResult.class);
            }else {
                result.custId = "";
            }
        }  catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    //스플레시
    public static SplashResult parsingSplash(String response) {
        SplashResult result = new SplashResult();
        try {
            JSONObject res = new JSONObject(response);
            if(!res.isNull("resultDatas")) {
                result = new ObjectMapper().readValue(res.getString("resultDatas"), SplashResult.class);
            }else {
                result.link = "";
            }
        }  catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    //메인팝업
    public static List<MainPopupResult> parsingMainPopupList(String response) {
        List<MainPopupResult> result = new ArrayList<>();
        try {
            JSONArray res = new JSONArray(response);
            for(int i =0;i<res.length();i++) {
                MainPopupResult info = new ObjectMapper().readValue(res.getJSONObject(i).toString(), MainPopupResult.class);
                result.add(info);
            }
        }  catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    //파워링크 파싱
    public static List<PowerLinkResult> parsingPowerLink(String response) {
        List<PowerLinkResult> result = new ArrayList<>();
        try {
            JSONObject res = new JSONObject(response);
            JSONArray array = res.getJSONObject("resultDatas").getJSONObject("response").getJSONArray("ads");
            for(int i =0;i<array.length();i++) {
                PowerLinkResult info = new ObjectMapper().readValue(array.getJSONObject(i).toString(), PowerLinkResult.class);
                result.add(info);
            }
        }  catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    //파워링크 파싱
    public static WidgetResult parsingWidget(String response) {
        WidgetResult result = new WidgetResult();
        try {
            JSONObject res = new JSONObject(response);
            result.hasLogin = res.getString("hasLogin");
            result.feed_msg = res.getJSONObject("myfeed").getString("feed_msg");
        }  catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

}
