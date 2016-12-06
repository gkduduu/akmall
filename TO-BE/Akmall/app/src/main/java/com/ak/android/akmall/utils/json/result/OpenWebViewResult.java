package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-05.
 * gkduduu@naver.com
 * what is? :
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWebViewResult {
    public String tp;
    public String url;
}
