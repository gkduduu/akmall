package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016-11-28.
 * 대카테고리
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BigCategoryResult {
    public List<String> ctgArray;
    public String callbackJson;/*action*/
    public ProdList prodList;
    public String dp;
    public String ctgInfo;
    public String param;
    public String detailCtgSize;
    public String lastCtgSize;
    public String powerLink;
}
