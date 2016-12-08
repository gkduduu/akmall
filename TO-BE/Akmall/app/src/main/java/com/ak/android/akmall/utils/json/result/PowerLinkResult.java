package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-06.
 * gkduduu@naver.com
 * what is? : 파워링크 리스트
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PowerLinkResult {
    public String title;
    public String desc;
    public String cUrl;
    public String vUrl;
}
