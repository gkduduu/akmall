package android.ak.com.akmall.utils.json;

import android.ak.com.akmall.utils.json.result.PageDatas;
import android.ak.com.akmall.utils.json.result.ProductResult;
import android.ak.com.akmall.utils.json.result.BigCategoryResult;
import android.ak.com.akmall.utils.json.result.CheckHeightResult;

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
//            result.ctgInfo.bigJson = res.getJSONObject("ctgInfo").getJSONArray("bigCtg");
//            result.ctgInfo.midJson = res.getJSONObject("ctgInfo").getJSONObject("midCtg");
//            result.ctgInfo.brandJson = res.getJSONObject("ctgInfo").getJSONArray("recomBrandList");

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
            return res.getString("key");
        }  catch (JSONException e) {
            e.getMessage();
        }
        return "";
    }

    //openwebview url 가져오기
    public static String parsingOpenWebview(String response) {
        try {
            JSONObject res = new JSONObject(response);
            return res.getString("url");
        }  catch (JSONException e) {
            e.getMessage();
        }
        return "";
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

}
