package com.ak.android.akmall.utils.json.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by 하영 on 2016-12-05.
 * gkduduu@naver.com
 * what is? : 설정페이지 정보 가져오기
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushSettingResult {
    //"BUY_ALARM_YN":"","END_HH":"00","SHOPPING_ALARM_YN":"Y","NOSOUND_YN":"","START_HH":"00"}
    public String end_hh;
    public String start_hh;
    public String BUY_ALARM_YN;
    public String NOSOUND_YN;
    public String SHOPPING_ALARM_YN;
}
