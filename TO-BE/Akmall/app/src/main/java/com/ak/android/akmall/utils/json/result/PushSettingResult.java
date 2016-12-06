package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-05.
 * gkduduu@naver.com
 * what is? : 설정페이지 정보 가져오기
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushSettingResult {
    public String end_hh;
    public String start_hh;
}
