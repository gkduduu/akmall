package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 하영 on 2016-11-28.
 * gkduduu@naver.com
 * what is? : 카테고리 상황
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CtgInfo {
    public JSONArray bigJson;
    public JSONObject midJson;
    public JSONArray brandJson;
    public String ctgId;
    public String pCtgId;
    public String areaCode;
    public String ctgInfo;
}
