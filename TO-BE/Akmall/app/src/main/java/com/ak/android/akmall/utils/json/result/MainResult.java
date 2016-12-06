package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by 하영 on 2016-11-29.
 * gkduduu@naver.com
 * what is? : main.do 를 찌르고 결과값
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainResult {
    public String resultMsg;
    public String resultCode;
    public List<TemplateData> templateList;
}
