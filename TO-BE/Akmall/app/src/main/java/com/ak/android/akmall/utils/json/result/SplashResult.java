package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? : 스플레시
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplashResult {
    public String dtime;
    public String link;
    public String use_yn;
}
